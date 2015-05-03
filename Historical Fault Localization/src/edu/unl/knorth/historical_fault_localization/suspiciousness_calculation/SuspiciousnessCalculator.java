package edu.unl.knorth.historical_fault_localization.suspiciousness_calculation;

import edu.unl.knorth.historical_fault_localization.intermediate_data.StatementData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.TestData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;
import java.util.ArrayList;
import java.util.List;

/**
 * Many spectrum-based fault localization techniques differ entirely in how they
 * calculate suspiciousness, so we use a superclass so that if we want to
 * implement additional techniques in the future, it is easy to do so.
 */
public abstract class SuspiciousnessCalculator {
    /**
     * Calculates the suspiciousness for each statement in a TestExecutionData.
     * @param testExecutionData The TestExecutionData for which suspiciousness
     * should be calculated.
     * @return The suspiciousness score of each statement in the
     * TestExecutionData. If every test in the TestExeuctionData passed, all
     * statements that were executed by at least one test will be assigned a
     * suspiciousness of 0.0 and all remaining statements will be skipped.
     * Likewise, if every test failed, all executed statement will be assigned a
     * suspiciousness of 1.0.
     */
    public final List<SuspiciousnessScore>
        calculateSuspiciousness(TestExecutionData testExecutionData) {
        List<TestData> passingTests = testExecutionData.getTests(true);
        List<TestData> failingTests = testExecutionData.getTests(false);
        
        // If there aren't any passing tests, or if there aren't any failing
        // test, calculating the suspiciousness would involve some
        // divide-by-zero-type situations. Instead, we mark all statements as
        // highly suspicious or not susicious at all.
        if(passingTests.isEmpty()) {
            // No passing tests - all statements are suspicious
            List<SuspiciousnessScore> suspiciousnessScores = new ArrayList<>();
            for(StatementData statement : testExecutionData.getStatements()) {
                suspiciousnessScores.add(new SuspiciousnessScore(statement,
                        1.0));
            }
            return suspiciousnessScores;
        } else if(failingTests.isEmpty()) {
            // No failing tests - no statements are suspicious
            List<SuspiciousnessScore> suspiciousnessScores = new ArrayList<>();
            for(StatementData statement : testExecutionData.getStatements()) {
                suspiciousnessScores.add(new SuspiciousnessScore(statement,
                        0.0));
            }
            return suspiciousnessScores;
        }
            
        return doCalculateSuspiciousness(testExecutionData);
    }
    
    /**
     * Calculates the suspiciousness for each statement in a TestExecutionData.
     * You can assume that at least one test case passed and at least one test
     * case failed in the TestExecutionData passed into this function. (If
     * that's not the case,
     * <code>SuspiciousnessCalculator.calculateSuspiciousness()</code>, the only
     * place from which this method is called directly, will return with
     * appropriate results before this method is called.
     * @param testExecutionData The TestExecutionData for which suspiciousness
     * should be calculated.
     * @return The suspiciousness score of each statement in the
     * TestExecutionData.
     */
    protected abstract List<SuspiciousnessScore>
        doCalculateSuspiciousness(TestExecutionData testExecutionData);
}