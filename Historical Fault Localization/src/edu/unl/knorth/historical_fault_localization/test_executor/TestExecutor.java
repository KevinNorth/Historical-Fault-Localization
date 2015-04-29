package edu.unl.knorth.historical_fault_localization.test_executor;

import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;
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
    private final String DEFAULT_OUTPUT_FILE_LOCATION = "temp/test_out.txt";
    
    /**
     * Executes the test harness script, using a default output file path.
     * @param testHarnessPath The path to the test harness script.
     * @param workingDirectoryPath The path to the target program repository
     * that will be passed as a parameter to the test harness script.
     * @param commitHash The hash of the commit being processed in the
     * target program's git repository. Passed as an argument to the test
     * harness script.
     * @param timestamp
     * @param timeoutLength
     * @return
     * @throws IOException 
     */
    public TestExecutionData executeTests(String testHarnessPath,
            String workingDirectoryPath, String commitHash, String timestamp,
            long timeoutLength)
        throws IOException {
        return executeTests(testHarnessPath, workingDirectoryPath, commitHash,
                timestamp, timeoutLength, DEFAULT_OUTPUT_FILE_LOCATION);
    }
    
    public TestExecutionData executeTests(String testHarnessPath,
            String workingDirectoryPath, String commitHash, String timestamp,
            long timeoutLength, String outputFilePath) throws IOException {
        Runtime rt = Runtime.getRuntime();
        
        String commandLineString = buildCommandLineString(testHarnessPath,
                workingDirectoryPath, commitHash, timestamp, outputFilePath);
        Process pr = rt.exec(commandLineString);
        
        try {
            boolean success = pr.waitFor(timeoutLength, TimeUnit.MILLISECONDS);
        
            if(success) {
                return new TestOutputParser()
                        .parseTestOutputFile(outputFilePath);
            } else {
                throw new IOException("Test harness timed out while executing"
                        + " on commit" + commitHash);
            }
        } catch(InterruptedException err) {
            throw new IOException("Test harness was interrupted while executing"
                    + " on commit " + commitHash, err);
        }
    }
    
    protected String buildCommandLineString(String testHarnessPath,
            String workingDirectoryPath, String commitHash, String timestamp,
            String outputFilePath) {
        StringBuilder str = new StringBuilder();
        
        str.append("./");
        str.append(testHarnessPath);
        str.append(" --work-directory=");
        str.append(workingDirectoryPath);
        str.append(" --commit-hash=");
        str.append(commitHash);
        str.append(" --commit-timestamp=");
        str.append(timestamp);
        str.append(" --output_file=");
        str.append(outputFilePath);
        
        return str.toString();
    }
    
}