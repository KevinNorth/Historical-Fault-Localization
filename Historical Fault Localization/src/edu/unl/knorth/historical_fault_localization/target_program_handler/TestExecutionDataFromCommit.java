package edu.unl.knorth.historical_fault_localization.target_program_handler;

import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;

public class TestExecutionDataFromCommit {
    private final TestExecutionData testExecutionData;
    private final String commitHash;
    private final int orderProcessed;

    public TestExecutionDataFromCommit(TestExecutionData testExecutionData, String commitHash, int orderProcessed) {
        this.testExecutionData = testExecutionData;
        this.commitHash = commitHash;
        this.orderProcessed = orderProcessed;
    }

    public TestExecutionData getTestExecutionData() {
        return testExecutionData;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public int getOrderProcessed() {
        return orderProcessed;
    }
}