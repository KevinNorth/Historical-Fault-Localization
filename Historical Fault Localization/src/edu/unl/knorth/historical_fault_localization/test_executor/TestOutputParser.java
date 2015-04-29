package edu.unl.knorth.historical_fault_localization.test_executor;

import edu.unl.knorth.historical_fault_localization.intermediate_data.StatementData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.TestData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Parses the data that is produced by the test harness script that the
 * Historical Fault Localization tool runs.
 * <p/>
 * The file should be in the following format. Whenever you use this tool to
 * analyze your target program, you must ensure that your test harness script
 * produces output in the following format:
 * <ol>
 * <li>Each line of the program must contain information representing exactly
 * one test case in your test suite.
 *  <ol>
 *   <li>Empty lines and lines containing only whitespace are ignored.</li>
 *   <li>On non-empty lines, leading and trailing whitespace is stripped before
 *   processing.</li>
 *   <li>Non-empty lines representing test cases should be whitespace-delimited
 *   lists of strings.</li>
 * </ol></li>
 * <li>Each line must start with either the string <code>passed</code> or
 * <code>failed</code> (case insensitive).
 * <ol>
 *   <li>If the string is <code>passed</code>, it indicates that the test case
 *   passed.</li>
 *   <li>The string <code>failed</code> has the obvious meaning.</li>
 * </ol></li>
 * <li>After the first string in each line, statements that were executed by
 * the test should be represented by pairs of strings.
 * <ol>
 *   <li>The first string represents the file that the executed statement can be
 *   found in.</li>
 *   <li>The second string represents the line number within the file of the
 *   executed statement.
 *   <ol><li>The second string should match the format for a Java
 *     integer, as determined by the <code>Integer.parseInt</code>
 *     function.</li></ol></li>
 *   <li>For example, the pair of strings <code>app.rb 15</code> would mean that
 *   the test case executed line 15 of the file app.rb.</li>
 *   <li>It is acceptable for a test case line to have the same file-line number
 *   pair multiple times, but it is not necessary. If a file-line number pair
 *   appears multiple times, it will only be recorded once when the file is
 *   parsed.</li>
 * </ol></li>
 * </ol>
 * <p/>
 * This is an example of the contents of a correctly formatted test harness
 * output file:
 * <p/>
 * <code>
 * passed app.rb 1 app.rb 2 app.rb 4 app.rb 6 utility.rb 23 utility.rb 24 utility.rb 26 app.rb 11<br/>
 * passed app.rb 1 app.rb 1 app.rb 2 app.rb 3 app.rb 6 utility.rb 23 utility.rb 25 app.rb 11<br/>
 * failed app.rb 1 app.rb 2 app.rb 4 app.rb 5 utility.rb 23 utility.rb 24 utility.rb 25 app.rb 11
 * </code>
 */
public final class TestOutputParser {
    /**
     * Parses the data from the output file that a test script outputs.
     * @param outputFilePath The path to the file that the test script output.
     * @return A <code>TestExecutionData</code> containing the output file's
     * information.
     * @throws IOException If there was a problem reading the file.
     */
    public TestExecutionData parseTestOutputFile(String outputFilePath)
            throws IOException {
        List<String> fileContents = Files.readAllLines(Paths.get(outputFilePath),
                Charset.defaultCharset());
        
        try {
            return parseTestOutput(fileContents);
        } catch(IOException err) {
            throw new IOException("Problem parsing output file "
                    + outputFilePath + ": " + err.getMessage(), err);
        }
    }
    
    /**
     * Parses the data from the test script output.
     * @param output The contents of the test script output file.
     * @return A <code>TestExecutionData</code> containing the information from
     * <code>output</code>.
     * @throws IOException If the file is not in the right format.
     */
    protected TestExecutionData parseTestOutput(List<String> output)
            throws IOException {
        TestExecutionData data = new TestExecutionData();
        
        for(String testOutput : output) {
            if(!testOutput.trim().isEmpty()) {
                data.addTest(parseSingleTestOutput(testOutput));
            }
        }
        
        return data;
    }
    
    /**
     * Parses the data from a single line of the test script output.
     * @param output The line from the output file to parse.
     * @return A <code>TestData</code> containing the information from
     * <code>output</code>.
     * @throws IOException If <code>output</code> is not in the right format.
     */
    protected TestData parseSingleTestOutput(String output) throws IOException {
        String[] tokens = output.trim().split("\\s+");
        
        boolean passed;
        Set<StatementData> statements = new HashSet<>();
        
        if(tokens[0].equalsIgnoreCase("passed")) {
            passed = true;
        } else if (tokens[0].equalsIgnoreCase("failed")) {
            passed = false;
        } else {
            throw new IOException("First string in output line \"" + output
                    + "\" was not \"passed\" or \"failed\".");
        }
        
        try {
            for(int i = 1; // Skip the first token, which is passed/failed
                    i < tokens.length - 1; // We will look at two tokens at once
                    i += 2) { // Two tokes at once
                String file = tokens[i];
                int lineNumber = Integer.parseInt(tokens[i+1]);
                
                statements.add(new StatementData(lineNumber, file));
            }
        }
        catch(NumberFormatException err) {
            throw new IOException("The output line \"" + output
                    + "\" contained an invalid line number.", err);
        }
        
        return new TestData(passed, statements);
    }
}