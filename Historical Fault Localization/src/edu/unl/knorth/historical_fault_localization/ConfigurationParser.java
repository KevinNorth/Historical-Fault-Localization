package edu.unl.knorth.historical_fault_localization;

import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.proximity_based_weighting.ThresholdType;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Parses a configuration file.
 */
public class ConfigurationParser {
    /**
     * Parses a configuration file.
     * @param configurationFile The path to the configuration file.
     * @return A Configuration representing the configuration file's contents,
     * or null if there was a problem reading the configuration file.
     */
    public Configuration parseConfigurationFile(String configurationFile) {
        Configuration config = new Configuration();
        
        try {
            List<String> fileContents = Files.readAllLines(
                Paths.get(configurationFile), Charset.defaultCharset());
            
            for(String line : fileContents) {
                String[] split = line.trim().split("\\s+", 2);
                if(split.length < 2) {
                    continue;
                }
                
                switch(split[0]) {
                    case "gitArguments":
                        config.setGitArguments(split[1]);
                        break;
                    case "testHarnessPath":
                        config.setTestHarnessPath(split[1]);
                        break;
                    case "targetProgramDirectory":
                        config.setTargetProgramDirectory(split[1]);
                        break;
                    case "testTimeout":
                        long testTimeout = Long.parseLong(split[1]);
                        config.setTestTimeout(testTimeout);
                        break;
                    case "suspiciousnessAlgorithm":
                        if(split[1].equals("ochiai")
                                || split[1].equals("proximity")) {
                            config.setSuspiciousnessAlgorithm(split[1]);
                        } else {
                            throw new IOException("suspiciousnessAlgorithm of " +
                                    split[1] + " is invalid.");
                        }
                        break;
                    case "lowerBound":
                        switch(split[1]) {
                            case "none":
                                config.setLowerBound(ThresholdType.IGNORED);
                                break;
                            case "quartile":
                                config.setLowerBound(ThresholdType.QUARTILE);
                                break;
                            case "tail":
                                config.setLowerBound(ThresholdType.TAIL);
                                break;
                            default:
                                throw new IOException("lowerBound of " +
                                        split[1] + " is invalid.");
                        }
                        break;
                    case "upperBound":
                        switch(split[1]) {
                            case "none":
                                config.setUpperBound(ThresholdType.IGNORED);
                                break;
                            case "quartile":
                                config.setUpperBound(ThresholdType.QUARTILE);
                                break;
                            case "tail":
                                config.setUpperBound(ThresholdType.TAIL);
                                break;
                            default:
                                throw new IOException("upperBound of " +
                                        split[1] + " is invalid.");
                        }
                         break;
                    case "statementHeight":
                        int statementHeight = Integer.parseInt(split[1]);
                        config.setStatementHeight(statementHeight);
                        break;
                    case "statementWidth":
                        int statementWidth = Integer.parseInt(split[1]);
                        config.setStatementWidth(statementWidth);
                        break;
                    case "fileMargin":
                        int fileMargin = Integer.parseInt(split[1]);
                        config.setFileMargin(fileMargin);
                        break;
                    case "fileFontSize":
                        int fileFontSize = Integer.parseInt(split[1]);
                        config.setFileFontSize(fileFontSize);
                        break;
                    case "imageOutputDirectory":
                        if(split[1].endsWith("/")) {
                            config.setImageOutputDirectory(split[1]);
                        } else {
                            throw new IOException("imageOutputDirectory value "
                                    + "of \"" + split[1] + "\" does not end in"
                                    + "a \"/\".");
                        }
                        break;
                    case "testHarnessOutput":
                        config.setTestHarnessOutput(split[1]);
                        break;
                }
            }
        } catch(IOException err) {
            System.out.println("Could not read configuration file:");
            System.out.println(err.getMessage());
            return null;
        } catch(NumberFormatException err) {
            System.out.println("Could not read configuration file:");
            System.out.println(err.getMessage());
            return null;
        }
        
        if(config.areMandatoryValuesSet()) {
            return config;
        } else {
            System.out.println("Configuration file did not set all mandatory "
                    + "values!");
            return null;
        }
    }
}