package edu.unl.knorth.historical_fault_localization.test_executor;

import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;
import edu.unl.knorth.historical_fault_localization.utility.StreamGobbler;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Creates a new process to run the test harness provided as input to the
 * Historical Fault Localization tool. Runs the test harness for a single
 * commit of the target program's history. Then, parses the test harness'
 * output file into a <code>TestExectionData</code> for use elsewhere in the
 * program.
 * <p/>
 * Be aware that every time <code>executeTests</code> is called, this class
 * creates a new process.
 * <p/>
 * When the test harness is executed, it will be called with the following
 * command line:
 * <p/>
 * <code>
 * ./[path to test harness]  --work-directory=[workingDirectoryPath]
 * --commit-hash=[commitHash] --commit-timestamp=[timestamp]
 * --output_file=[outputFilePath]
 * </code>
 * <p/>
 * Where:
 * <ol>
 * <li><code>workingDirectoryPath</code> is the path to the directory of the
 * program to test</li>
 * <li><code>commitHash</code> is hash of the git commit being tested</li>
 * <li><code>timestamp</code> is the the timestamp of when the git commit being
 * tested was committed, formatted as output by the
 * <code>git show --format="%cD"</code> command</li>
 * <li><code>outputFilePath</code> is the path to where the test harness should
 * output its file. Note that the test harness should NOT give the output
 * intended for the Historical Fault Localization tool to receive to STDOUT.
 * Instead, it should be saved to a file formatted as described in
 * <code>TestOutputParser.java</code>.</li></ol>
 */
public final class TestExecutor {
    private final String DEFAULT_OUTPUT_FILE_LOCATION
            = System.getProperty("user.dir") + "/temp/test_out.txt";
    
    /**
     * Executes the test harness script, using a default output file path.
     * <p/>
     * As a side effect, this method will output to STDOUT anything that the
     * test harness outputs to its STDOUT or STDERR streams.
     * @param testHarnessPath The path to the test harness script.
     * @param workingDirectoryPath The path to the target program repository
     * that will be passed as a parameter to the test harness script.
     * @param commitHash The hash of the commit being processed in the
     * target program's git repository. Passed as an argument to the test
     * harness script.
     * @param timestamp The timestamp for when the commit being processed was
     * committed. It should be formatted in the format produced by executing
     * <code>git show --format="%cD"</code>
     * @param timeoutLength The amount of time, in milliseconds, to allow the
     * test harness to run before timing it out. If the test harness times out,
     * its process will be killed and an IOException will be thrown.
     * @return A <code>TestExecutionData</code> representing data collected from
     * the test harness.
     * @throws IOException If there was a problem running the test harness or
     * parsing the output file it generates.
     */
    public TestExecutionData executeTests(String testHarnessPath,
            String workingDirectoryPath, String commitHash, String timestamp,
            long timeoutLength)
        throws IOException {
        return executeTests(testHarnessPath, workingDirectoryPath, commitHash,
                timestamp, timeoutLength, DEFAULT_OUTPUT_FILE_LOCATION);
    }
    
    /**
     * Executes the test harness script.
     * <p/>
     * As a side effect, this method will output to STDOUT anything that the
     * test harness outputs to its STDOUT or STDERR streams.
     * @param testHarnessPath The path to the test harness script.
     * @param workingDirectoryPath The path to the target program repository
     * that will be passed as a parameter to the test harness script.
     * @param commitHash The hash of the commit being processed in the
     * target program's git repository. Passed as an argument to the test
     * harness script.
     * @param timestamp The timestamp for when the commit being processed was
     * committed. It should be formatted in the format produced by executing
     * <code>git show --format="%cD"</code>
     * @param timeoutLength The amount of time, in milliseconds, to allow the
     * test harness to run before timing it out. If the test harness times out,
     * its process will be killed and an IOException will be thrown.
     * @param outputFilePath The path to the location where the test harness
     * should save its output file.
     * @return A <code>TestExecutionData</code> representing data collected from
     * the test harness.
     * @throws IOException If there was a problem running the test harness or
     * parsing the output file it generates.
     */
    public TestExecutionData executeTests(String testHarnessPath,
            String workingDirectoryPath, String commitHash, String timestamp,
            long timeoutLength, String outputFilePath) throws IOException {
        Runtime rt = Runtime.getRuntime();
        
        String[] commandLineString = buildCommandLineStrings(testHarnessPath,
                workingDirectoryPath, commitHash, timestamp, outputFilePath);
        Process pr = rt.exec(commandLineString);

        // Output the process's output to STDOUT so the end user can see it
        StreamGobbler outputGobbler = new StreamGobbler(pr.getInputStream());
        StreamGobbler errorGobbler = new StreamGobbler(pr.getErrorStream());

        outputGobbler.start();
        errorGobbler.start();
        
        try {            
            boolean success = pr.waitFor(timeoutLength, TimeUnit.MILLISECONDS);
            
            if(success) {
                return new TestOutputParser()
                        .parseTestOutputFile(outputFilePath);
            } else {
                pr.destroy();
                throw new IOException("Test harness timed out while executing"
                        + " on commit" + commitHash);
            }
        } catch(InterruptedException err) {
            throw new IOException("Test harness was interrupted while executing"
                    + " on commit " + commitHash, err);
        } finally {
            try {
                outputGobbler.join(100);
            } catch(InterruptedException err) { /* Swallow on purpose */ }
            try {
                errorGobbler.join(100);
            } catch(InterruptedException err) { /* Swallow on purpose */ }
        }
    }
    
    /**
     * Prepares the strings that will be used in a command to tell the OS to run
     * the test harness.
     * @param testHarnessPath The path to the test harness script.
     * @param workingDirectoryPath The path to the target program repository
     * that will be passed as a parameter to the test harness script.
     * @param commitHash The hash of the commit being processed in the
     * target program's git repository. Passed as an argument to the test
     * harness script.
     * @param timestamp The timestamp for when the commit being processed was
     * committed. It should be formatted in the format produced by executing
     * <code>git show --format="%cD"</code>
     * @param outputFilePath The path to the location where the test harness
     * should save its output file.
     * @return An array of Strings that can be passed to
     * <code>Runtime.exec()</code> to run the test harness.
     */
    private String[] buildCommandLineStrings(String testHarnessPath,
            String workingDirectoryPath, String commitHash, String timestamp,
            String outputFilePath) {
        String[] str = new String[5];
        
        str[0] = testHarnessPath;
        str[1] = "--work-directory=" + workingDirectoryPath;
        str[2] = "--commit-hash=" + commitHash;
        str[3] = "--commit-timestamp=" + timestamp;
        str[4] = "--output_file=" + outputFilePath;
        
        return str;
    }
    
}