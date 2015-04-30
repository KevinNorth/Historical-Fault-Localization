/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.knorth.historical_fault_localization;

import edu.unl.knorth.historical_fault_localization.target_program_handler.TargetProgramHandler;
import edu.unl.knorth.historical_fault_localization.target_program_handler.TestExecutionDataFromCommit;
import java.io.File;
import java.util.List;

/**
 *
 * @author knorth
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        File currentDir = new File(System.getProperty("user.dir"));
        File parentDir = currentDir.getParentFile();

        String gitArguments = "--reverse --date-order faults ^master";
        String testHarnessPath = parentDir.getAbsolutePath() + "/historical-fault-localization-target-ruby-app/test_harness/code_coverage.rb";
        String workingDirectory = parentDir.getAbsolutePath() + "/historical-fault-localization-target-ruby-app";

        
        List<TestExecutionDataFromCommit> results =
                new TargetProgramHandler().handleProgram(gitArguments,
                        testHarnessPath, workingDirectory, 60000);
        
        for(TestExecutionDataFromCommit result : results) {
            System.out.println(result.getOrderProcessed());
            System.out.println(result.getCommitHash());
            System.out.println(result.getTestExecutionData());
        }
    }
}
