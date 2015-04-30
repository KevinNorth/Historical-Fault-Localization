/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.knorth.historical_fault_localization;

import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.SuspiciousnessCalculator;
import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.SuspiciousnessScore;
import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.ochiai.OchiaiSuspiciousnessCalculator;
import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.proximity_based_weighting.ProximityBasedWeightingSuspiciousnessCalculator;
import edu.unl.knorth.historical_fault_localization.target_program_handler.TargetProgramHandler;
import edu.unl.knorth.historical_fault_localization.target_program_handler.TestExecutionDataFromCommit;
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
     * @param args The command line arguments. This should include exactly one
     * string, the path to the configuration file.
     */
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: ./program pathToConfigurationFile");
            System.out.println("To see an example configuration file, look at "
                    + "config/config.example.txt");
            System.out.println("included in this program's source code.");
            return;
        }
        
        // Parse config file
        Configuration config =
                new ConfigurationParser().parseConfigurationFile(args[0]);
        if(config == null) {
            return;
        }

        // Run target program tests to get test coverage data
        List<TestExecutionDataFromCommit> coverageData;
        if(config.getTestHarnessOutput() == null) {
            coverageData =
                new TargetProgramHandler().handleProgram(
                        config.getGitArguments(),
                        config.getTestHarnessPath(),
                        config.getTargetProgramDirectory(),
                        config.getTestTimeout());
        } else {
            coverageData =
                new TargetProgramHandler().handleProgram(
                        config.getGitArguments(),
                        config.getTestHarnessPath(),
                        config.getTargetProgramDirectory(),
                        config.getTestTimeout(),
                        config.getTestHarnessOutput());
        }

        // Prepare the calculator based on the configuration
        SuspiciousnessCalculator calculator;
        if(config.getSuspiciousnessAlgorithm().equals("ochiai")) {
            calculator = new OchiaiSuspiciousnessCalculator();
        } else {
            calculator = new ProximityBasedWeightingSuspiciousnessCalculator(
                        config.getLowerBound(), config.getUpperBound());            
        }
        
        // Calcualte suspiciousness scores
        List<SuspiciousnessScoresFromCommit> allSuspiciousnessScores =
                new ArrayList<>();
        for(TestExecutionDataFromCommit result : coverageData) {    
            List<SuspiciousnessScore> suspiciousnessScores = 
                    calculator.calculateSuspiciousness(
                            result.getTestExecutionData());
            
            allSuspiciousnessScores.add(new SuspiciousnessScoresFromCommit(
                    result.getCommitHash(), result.getOrderProcessed(),
                    suspiciousnessScores));
        }
        
        // Create visualizations
        SuspiciousnessVisualizer visualizer = new SuspiciousnessVisualizer();
        visualizer.visualizeSuspiciousnessForAllCommits(
                config.getStatementHeight(), config.getStatementWidth(),
                config.getFileMargin(), config.getFileFontSize(),
                config.getImageOutputDirectory(), allSuspiciousnessScores);
    }
}
