/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.knorth.historical_fault_localization;

import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;
import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.SuspiciousnessCalculator;
import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.SuspiciousnessScore;
import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.proximity_based_weighting.ProximityBasedWeightingSuspiciousnessCalculator;
import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.proximity_based_weighting.ThresholdType;
import edu.unl.knorth.historical_fault_localization.visualizer.SuspiciousnessScoresFromCommit;
import edu.unl.knorth.historical_fault_localization.visualizer.SuspiciousnessVisualizer;
import java.util.ArrayList;
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
//        File currentDir = new File(System.getProperty("user.dir"));
//        File parentDir = currentDir.getParentFile();
//
//        String gitArguments = "--reverse --date-order faults ^master";
//        String testHarnessPath = parentDir.getAbsolutePath() + "/historical-fault-localization-target-ruby-app/test_harness/code_coverage.rb";
//        String workingDirectory = parentDir.getAbsolutePath() + "/historical-fault-localization-target-ruby-app";
//
//        
//        List<TestExecutionDataFromCommit> results =
//                new TargetProgramHandler().handleProgram(gitArguments,
//                        testHarnessPath, workingDirectory, 60000);
//
        List<SuspiciousnessScoresFromCommit> allSuspiciousnessScores =
                new ArrayList<>();
//        for(TestExecutionDataFromCommit result : results) {
//            try {
            SuspiciousnessCalculator calculator =
                    new ProximityBasedWeightingSuspiciousnessCalculator(
                            ThresholdType.TAIL, ThresholdType.TAIL);
            
//            List<SuspiciousnessScore> suspiciousnessScores = 
//                    calculator.calculateSuspiciousness(
//                            result.getTestExecutionData());
//            
//            allSuspiciousnessScores.add(new SuspiciousnessScoresFromCommit(
//                    result.getCommitHash(), result.getOrderProcessed(),
//                    suspiciousnessScores));
//            } catch(IndexOutOfBoundsException err) {
//                err.printStackTrace();
//                System.err.println(result.getCommitHash());
//                System.err.println(result.getOrderProcessed());
//                System.err.println(result.getTestExecutionData());
//            }
//        }
        
       TestExecutionData data = DummyData.getDummyData();
       List<SuspiciousnessScore> ss = calculator.calculateSuspiciousness(data);
       
       SuspiciousnessScoresFromCommit ssfc =
               new SuspiciousnessScoresFromCommit("abcdef", 1, ss);
        
       allSuspiciousnessScores.add(ssfc);
       
        SuspiciousnessVisualizer visualizer = new SuspiciousnessVisualizer();
        visualizer.visualizeSuspiciousnessForAllCommits(10, 50, 10, 12, "out/",
                allSuspiciousnessScores);
    }
}
