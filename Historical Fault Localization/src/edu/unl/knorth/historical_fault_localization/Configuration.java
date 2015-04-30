package edu.unl.knorth.historical_fault_localization;

import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.proximity_based_weighting.ThresholdType;

/**
 * Keeps track of the configuration values gathered from the configuration file.
 */
public class Configuration {
    private String gitArguments;
    private String testHarnessPath;
    private String targetProgramDirectory;
    private long testTimeout;
    private String suspiciousnessAlgorithm;
    private ThresholdType lowerBound;
    private ThresholdType upperBound;
    private int statementHeight;
    private int statementWidth;
    private int fileMargin;
    private int fileFontSize;
    private String imageOutputDirectory;
    private String testHarnessOutput;

    public Configuration() {
        gitArguments = null;
        testHarnessPath = null;
        targetProgramDirectory = null;
        testTimeout = -1;
        suspiciousnessAlgorithm = null;
        lowerBound = null;
        upperBound = null;
        statementHeight = -1;
        statementWidth = -1;
        fileMargin = -1;
        fileFontSize = -1;
        imageOutputDirectory = null;
        testHarnessOutput = null;
    }
    
    public boolean areMandatoryValuesSet() {
        if(suspiciousnessAlgorithm == null) {
            return false;
        } else if(suspiciousnessAlgorithm.equals("ochiai")) {
            return (gitArguments != null)
                && (testHarnessPath != null)
                && (targetProgramDirectory != null)
                && (testTimeout >= 0)
                && (statementHeight >= 0)
                && (statementWidth >= 0)
                && (fileMargin >= 0)
                && (fileFontSize >= 0)
                && (imageOutputDirectory != null);
            // testHarnessOutput is optional
            // lowerBound and upperBound not required with Ochiai
        } else if(suspiciousnessAlgorithm.equals("proximity")) {
            return (gitArguments != null)
                && (testHarnessPath != null)
                && (targetProgramDirectory != null)
                && (testTimeout >= 0)
                && (lowerBound != null)
                && (upperBound != null)
                && (statementHeight >= 0)
                && (statementWidth >= 0)
                && (fileMargin >= 0)
                && (fileFontSize >= 0)
                && (imageOutputDirectory != null);
        } else {
            return false;
        }
    }

    public String getGitArguments() {
        return gitArguments;
    }

    public void setGitArguments(String gitArguments) {
        this.gitArguments = gitArguments;
    }

    public String getTestHarnessPath() {
        return testHarnessPath;
    }

    public void setTestHarnessPath(String testHarnessPath) {
        this.testHarnessPath = testHarnessPath;
    }

    public String getTargetProgramDirectory() {
        return targetProgramDirectory;
    }

    public void setTargetProgramDirectory(String targetProgramDirectory) {
        this.targetProgramDirectory = targetProgramDirectory;
    }

    public long getTestTimeout() {
        return testTimeout;
    }

    public void setTestTimeout(long testTimeout) {
        this.testTimeout = testTimeout;
    }

    public String getSuspiciousnessAlgorithm() {
        return suspiciousnessAlgorithm;
    }

    public void setSuspiciousnessAlgorithm(String suspiciousnessAlgorithm) {
        this.suspiciousnessAlgorithm = suspiciousnessAlgorithm;
    }

    public ThresholdType getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(ThresholdType lowerBound) {
        this.lowerBound = lowerBound;
    }

    public ThresholdType getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(ThresholdType upperBound) {
        this.upperBound = upperBound;
    }

    public int getStatementHeight() {
        return statementHeight;
    }

    public void setStatementHeight(int statementHeight) {
        this.statementHeight = statementHeight;
    }

    public int getStatementWidth() {
        return statementWidth;
    }

    public void setStatementWidth(int statementWidth) {
        this.statementWidth = statementWidth;
    }

    public int getFileMargin() {
        return fileMargin;
    }

    public void setFileMargin(int fileMargin) {
        this.fileMargin = fileMargin;
    }

    public int getFileFontSize() {
        return fileFontSize;
    }

    public void setFileFontSize(int fileFontSize) {
        this.fileFontSize = fileFontSize;
    }

    public String getImageOutputDirectory() {
        return imageOutputDirectory;
    }

    public void setImageOutputDirectory(String imageOutputDirectory) {
        this.imageOutputDirectory = imageOutputDirectory;
    }

    public String getTestHarnessOutput() {
        return testHarnessOutput;
    }

    public void setTestHarnessOutput(String testHarnessOutput) {
        this.testHarnessOutput = testHarnessOutput;
    }
}