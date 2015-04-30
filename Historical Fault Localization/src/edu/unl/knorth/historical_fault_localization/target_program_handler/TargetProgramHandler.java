package edu.unl.knorth.historical_fault_localization.target_program_handler;

import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;
import edu.unl.knorth.historical_fault_localization.target_program_handler.test_executor.TestExecutor;
import edu.unl.knorth.historical_fault_localization.utility.StreamGobbler;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Interacts with the target program in order to get test coverage data on all
 * of the requested commits. This class is responsible for interacting with the
 * target program's git repository and running the target program's test harness
 * on different versions of the target program.
 */
public class TargetProgramHandler {
    /**
     * Collects data from the target program by running the test harness script
     * on it once per requested commit. To do so, this method:
     * <ol><li>Runs <code>git log</code> in order to get a list of commits to
     * process on the target program</li>
     * <li>For each of the commits performs the following:
     * <ol><li>Runs <code>git checkout</code> to set the target program's
     * repository to the version corresponding to the commit</li>
     * <li>Runs the target program's test harness to get test coverage
     * information</li>
     * <li>Aggregates the test coverage data</li></ol>
     * <li>Aggregates all of the commits' test coverage information.</li></ol>
     * <p/>
     * This method has two major side effects. First, it creates new processes
     * to run git commands and the test harness script, all of which will have
     * significant effects on the target program's files. Second, it outputs
     * to STDOUT. This output can be hundreds or thousands of lines long and
     * be megabytes in size!
     * @param gitArguments To get a list of git commits, this method will
     * execute <code>git log --format=%H-%cD [gitArguments]</code>. Every
     * commit that is returned will have the test harness run against it.
     * @param testHarnessPath The path to the script that will be executed to
     * collect test coverage data.
     * @param workingDirectoryPath The path to the root directory of the target
     * program's git repository. git commands will be executed from this
     * directory, and this directory will be passed as an argument to the test
     * harness script.
     * @param timeoutLength The amount of time, in milliseconds, to allow the
     * test harness to run before killing it. This timeout period is amount of
     * time to run per commit, not amount of time to run total across all
     * commits.
     * @return A list of <code>TestExeuctionDataFromCommit</code>s containing
     * all the test coverage data from each of the test harness runs. If there
     * were problems getting the initial commit list, the method will return 
     * null instead. If there were any problems getting test coverage
     * information for a single test, the list this method returns will simply
     * omit that test and any others that have problems.
     */
    public List<TestExecutionDataFromCommit> handleProgram(String gitArguments,
            String testHarnessPath, String workingDirectoryPath,
            long timeoutLength) {
        return handleProgram(gitArguments, testHarnessPath,
                workingDirectoryPath, timeoutLength,
                TestExecutor.DEFAULT_OUTPUT_FILE_LOCATION);
    }
    
    /**
     * Collects data from the target program by running the test harness script
     * on it once per requested commit. To do so, this method:
     * <ol><li>Runs <code>git log</code> in order to get a list of commits to
     * process on the target program</li>
     * <li>For each of the commits performs the following:
     * <ol><li>Runs <code>git checkout</code> to set the target program's
     * repository to the version corresponding to the commit</li>
     * <li>Runs the target program's test harness to get test coverage
     * information</li>
     * <li>Aggregates the test coverage data</li></ol>
     * <li>Aggregates all of the commits' test coverage information.</li></ol>
     * <p/>
     * This method has two major side effects. First, it creates new processes
     * to run git commands and the test harness script, all of which will have
     * significant effects on the target program's files. Second, it outputs
     * to STDOUT. This output can be hundreds or thousands of lines long and
     * be megabytes in size!
     * @param gitArguments To get a list of git commits, this method will
     * execute <code>git log --format=%H-%cD [gitArguments]</code>. Every
     * commit that is returned will have the test harness run against it.
     * @param testHarnessPath The path to the script that will be executed to
     * collect test coverage data.
     * @param workingDirectoryPath The path to the root directory of the target
     * program's git repository. git commands will be executed from this
     * directory, and this directory will be passed as an argument to the test
     * harness script.
     * @param timeoutLength The amount of time, in milliseconds, to allow the
     * test harness to run before killing it. This timeout period is amount of
     * time to run per commit, not amount of time to run total across all
     * commits.
     * @param outputPath The path to the location the test harness script should
     * save its output file.
     * @return A list of <code>TestExeuctionDataFromCommit</code>s containing
     * all the test coverage data from each of the test harness runs. If there
     * were problems getting the initial commit list, the method will return 
     * null instead. If there were any problems getting test coverage
     * information for a single test, the list this method returns will simply
     * omit that test and any others that have problems.
     */
    public List<TestExecutionDataFromCommit> handleProgram(String gitArguments,
            String testHarnessPath, String workingDirectoryPath,
            long timeoutLength, String outputPath) {
        List<String[]> hashesAndDates;
        try {
            hashesAndDates = getCommitsList(gitArguments, workingDirectoryPath);
        } catch(IOException err) {
            System.out.println("Could not run `git log` to get commits list:");
            System.out.println(err.getMessage());
            err.printStackTrace(System.out);
            return null;
        }
        
        int numCommits = hashesAndDates.size();
        
        System.out.println("Found " + numCommits + " commits to process.");
        System.out.println();

        TestExecutor testExecutor = new TestExecutor();
        List<TestExecutionDataFromCommit> results = new ArrayList<>();

        int currentCommitNumber = 1;
        for(String[] hashAndDate : hashesAndDates) {
            String hash = hashAndDate[0];
            String date = hashAndDate[1];
            
            System.out.println("Getting test coverage information for commit "
                    + hash + " (" + currentCommitNumber + "/" + numCommits +
                    ") ...");

            try {
                checkoutGitCommit(hash, workingDirectoryPath);
            
                System.out.println("Running test harness");
                TestExecutionData singleResult =  testExecutor.executeTests(
                        testHarnessPath, workingDirectoryPath, hash, date,
                        timeoutLength, outputPath);
                
                results.add(new TestExecutionDataFromCommit(singleResult, hash,
                        currentCommitNumber));
            } catch(IOException err) {
                System.out.println("Could not get test coverage information "
                        + "for commit " + hash + ":");
                System.out.println(err.getMessage());
                System.out.println("Continuing with the next commit...");
                err.printStackTrace(System.out);
            }

            System.out.println();
            currentCommitNumber++;
        }
        
        return results;
    }
    
    /**
     * Runs <code>git log</code> to get a list of commit hashes and their dates.
     * In particular, the function calls
     * <p/>
     * <code>git log  --format=%H-%cD [gitArguments]</code>
     * <p/>
     * where <code>[gitArguments]</code> is the string that is passed to this
     * function. Therefore, you can use the <code>gitArgument</code> command to
     * control which commits are selected and in which order they are presented.
     * @param gitArguments A string with options to pass to <code>git log</code>
     * in order to control which commits are selected and in which order they
     * are processed.
     * @param workingDirectoryPath The path to the root of the target program's
     * git repository.
     * @return A List of String arrays. Each array in the list has two elements.
     * The first is a commit hash, and the second is the timestamp the commit
     * was committed. The list is in the order the commits were presented by
     * <code>git log</code>.
     * @throws IOException If there was a problem running <code>git log</code>.
     */
    private List<String[]> getCommitsList(String gitArguments,
            String workingDirectoryPath) throws IOException {
        List<String[]> hashesAndDates = new LinkedList<>();
        
        File workingDirectory = new File(workingDirectoryPath);
        
        String commandLineString = "git log --format=%H-%cD " +gitArguments;
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec(commandLineString.trim().split("\\s+"), null,
                workingDirectory);

        System.out.println("Running `" + commandLineString + "` in order to "
                + "find git commits...");
        
        // Output the process's output to STDOUT so the end user can see it
        StreamGobbler errorGobbler = new StreamGobbler(pr.getErrorStream());
        errorGobbler.start();
        
        try {            
            boolean success = pr.waitFor(10, TimeUnit.SECONDS);
            
            if(success) {
            InputStreamReader isr = new InputStreamReader(pr.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ( (line = br.readLine()) != null)
                hashesAndDates.add(line.trim().split("-", 2));

            return hashesAndDates;
            } else {
                pr.destroy();
                throw new IOException("Timed out while getting commits from git");
            }
        } catch(InterruptedException err) {
            throw new IOException("Interrupted while getting commits from git",
                    err);
        } finally {
            try {
                errorGobbler.join(100);
            } catch(InterruptedException err) { /* Swallow on purpose */ }
        }
    }
    
    /**
     * Runs <code>git checkout</code> in order to checkout a particular commit
     * in the target program's repository.
     * <p/>
     * Notice that if the checkout command takes more than 30 seconds to
     * complete, this method will throw an exception, but the git process
     * created will not be killed. You may want to manually inspect what the
     * process is doing and end it yourself.
     * @param commitHash The hash of the commit to checkout.
     * @param workingDirectoryPath The path to the root of the target program's
     * git repository.
     * @throws IOException If there was a problem checking out the commit.
     */
    private void checkoutGitCommit(String commitHash,
            String workingDirectoryPath) throws IOException {
        File workingDirectory = new File(workingDirectoryPath);
        
        String commandLineString = "git checkout " + commitHash;
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec(commandLineString.trim().split("\\s+"), null,
                workingDirectory);        
        System.out.println("Running `"+ commandLineString + "` to checkout "
                + "commit...");
        
        // Output the process's output to STDOUT so the end user can see it
        StreamGobbler outputGobbler = new StreamGobbler(pr.getInputStream());
        StreamGobbler errorGobbler = new StreamGobbler(pr.getErrorStream());

        errorGobbler.start();
        
        try {            
            boolean success = pr.waitFor(30, TimeUnit.SECONDS);
            
            if(success) {
                return; // As a side effect of this method running is that
                // the target program's repository is now checked out to a
                // different branch. There's no need to return anything once
                // that's done.
            } else {
                // Notice that we do NOT kill the spawned process in this 
                // method. We let it keep running so that, if 
                throw new IOException("Timed out while checking out commit "
                        + commitHash + ". The process that git is running on "
                        + "has not been killed - it is running! You may need "
                        + "to kill it manually.");
            }
        } catch(InterruptedException err) {
            throw new IOException("Interrupted while getting commits from git",
                    err);
        } finally {
            try {
                outputGobbler.join(100);
            } catch(InterruptedException err) { /* Swallow on purpose */ }
            try {
                errorGobbler.join(100);
            } catch(InterruptedException err) { /* Swallow on purpose */ }
        }
    }
}