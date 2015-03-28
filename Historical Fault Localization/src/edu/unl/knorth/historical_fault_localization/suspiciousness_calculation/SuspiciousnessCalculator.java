package edu.unl.knorth.historical_fault_localization.suspiciousness_calculation;

import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;
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
     * TestExecutionData.
     */
    public abstract List<SuspiciousnessScore>
        calculateSuspiciousness(TestExecutionData testExecutionData);
}