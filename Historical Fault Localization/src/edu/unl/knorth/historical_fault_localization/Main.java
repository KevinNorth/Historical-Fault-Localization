/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.knorth.historical_fault_localization;

import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;
import edu.unl.knorth.historical_fault_localization.test_executor.TestExecutor;

/**
 *
 * @author knorth
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String testHarnessPath = "/Users/ThePondermatic/Documents/Programming/historical_fault_localization_ruby_app/test_harness/code_coverage.rb";
        String workingDirectory = "/Users/ThePondermatic/Documents/Programming/historical_fault_localization_ruby_app";
        String commitHash = "asdf";
        String timestamp = "asdf";
        
        TestExecutionData results = new TestExecutor()
                .executeTests(testHarnessPath, workingDirectory, commitHash,
                        timestamp, 30000L);
        
        System.out.println(results);
    }
}
