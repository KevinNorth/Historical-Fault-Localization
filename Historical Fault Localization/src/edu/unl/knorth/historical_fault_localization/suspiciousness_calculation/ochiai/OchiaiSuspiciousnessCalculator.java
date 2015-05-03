package edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.ochiai;

import edu.unl.knorth.historical_fault_localization.intermediate_data.StatementData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.TestData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;
import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.SuspiciousnessCalculator;
import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.SuspiciousnessScore;
import java.util.ArrayList;
import java.util.List;

public class OchiaiSuspiciousnessCalculator extends SuspiciousnessCalculator {
    @Override
    protected List<SuspiciousnessScore>
       doCalculateSuspiciousness(TestExecutionData testExecutionData) {
        List<SuspiciousnessScore> suspiciousnessScores = new ArrayList<>();
            
        int numFailingTests = testExecutionData.getTests(false).size();
        
        for(StatementData statement : testExecutionData.getStatements()) {
            suspiciousnessScores.add(calculateIndividualSuspiciousness(
                    testExecutionData, statement, numFailingTests));
        }
        
        return suspiciousnessScores;
    }

    protected SuspiciousnessScore calculateIndividualSuspiciousness(
        TestExecutionData data, StatementData statement, int numFailingTests) {
        List<TestData> relevantFailingTests =
                data.getTestsThatExecuteStatment(statement, false);
        List<TestData> relevantPassingTests =
                data.getTestsThatExecuteStatment(statement, true);
        
        int totalRelevantTests = relevantFailingTests.size()
                + relevantPassingTests.size();
        
        double numerator = (double) relevantFailingTests.size();
        double denominator = StrictMath.sqrt(((double)numFailingTests) *
            ((double) totalRelevantTests));
        double suspiciousness = numerator/denominator;
        
        return new SuspiciousnessScore(statement, suspiciousness);
    }
}