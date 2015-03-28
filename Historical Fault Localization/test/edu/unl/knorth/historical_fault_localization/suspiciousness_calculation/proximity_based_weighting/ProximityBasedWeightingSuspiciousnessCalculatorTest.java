package edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.proximity_based_weighting;

import edu.unl.knorth.historical_fault_localization.DummyData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.StatementData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;
import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.SuspiciousnessScore;
import java.util.List;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ProximityBasedWeightingSuspiciousnessCalculatorTest {
    @Test
    public void exampleFromPaper() {
        // Make sure that the calculator produces the same results that 
        // paper that describes the proximity-based weighting algorithm gets on
        // the example program the paper uses.
        TestExecutionData data = DummyData.getDummyData();
        ProximityBasedWeightingSuspiciousnessCalculator calculator =
                new ProximityBasedWeightingSuspiciousnessCalculator(
                        ThresholdType.IGNORED, ThresholdType.IGNORED);
        
        List<SuspiciousnessScore> suspiciousnessScores =
                calculator.calculateSuspiciousness(data);
        
        // Storing all of the suspiciousness scores in doubles is actually an
        // important step because, if the suspiciousness scores we get back miss
        // any of the statements, the double that's supposed to record its score
        // will be left at the default value of 0D. This will cause our
        // assertions that we get the right suspiciousness score below to
        // correctly fail.
        double statement1Suspiciousness = 0, statement2Suspiciousness = 0,
                statement3Suspiciousness = 0, statement4Suspiciousness = 0,
                statement5Suspiciousness = 0, statement6Suspiciousness = 0,
                statement7Suspiciousness = 0, statement8Suspiciousness = 0,
                statement9Suspiciousness = 0, statement10Suspiciousness = 0,
                statement11Suspiciousness = 0, statement12Suspiciousness = 0,
                statement13Suspiciousness = 0;
        
        final StatementData statement1 = new StatementData(1, "program.c");
        final StatementData statement2 = new StatementData(2, "program.c");
        final StatementData statement3 = new StatementData(3, "program.c");
        final StatementData statement4 = new StatementData(4, "program.c");
        final StatementData statement5 = new StatementData(5, "program.c");
        final StatementData statement6 = new StatementData(6, "program.c");
        final StatementData statement7 = new StatementData(7, "program.c");
        final StatementData statement8 = new StatementData(8, "program.c");
        final StatementData statement9 = new StatementData(9, "program.c");
        final StatementData statement10 = new StatementData(10, "program.c");
        final StatementData statement11 = new StatementData(11, "program.c");
        final StatementData statement12 = new StatementData(12, "program.c");
        final StatementData statement13 = new StatementData(13, "program.c");
        
        for(SuspiciousnessScore suspiciousnessScore : suspiciousnessScores) {
            StatementData statement = suspiciousnessScore.getStatement();
            if(statement.equals(statement1)) {
                statement1Suspiciousness =
                        suspiciousnessScore.getSuspiciousness();
            } else if(statement.equals(statement2)) {
                statement2Suspiciousness =
                        suspiciousnessScore.getSuspiciousness();
            } else if(statement.equals(statement3)) {
                statement3Suspiciousness =
                        suspiciousnessScore.getSuspiciousness();
            } else if(statement.equals(statement4)) {
                statement4Suspiciousness =
                        suspiciousnessScore.getSuspiciousness();
            } else if(statement.equals(statement5)) {
                statement5Suspiciousness =
                        suspiciousnessScore.getSuspiciousness();
            } else if(statement.equals(statement6)) {
                statement6Suspiciousness =
                        suspiciousnessScore.getSuspiciousness();
            } else if(statement.equals(statement7)) {
                statement7Suspiciousness =
                        suspiciousnessScore.getSuspiciousness();
            } else if(statement.equals(statement8)) {
                statement8Suspiciousness =
                        suspiciousnessScore.getSuspiciousness();
            } else if(statement.equals(statement9)) {
                statement9Suspiciousness =
                        suspiciousnessScore.getSuspiciousness();
            } else if(statement.equals(statement10)) {
                statement10Suspiciousness =
                        suspiciousnessScore.getSuspiciousness();
            } else if(statement.equals(statement11)) {
                statement11Suspiciousness =
                        suspiciousnessScore.getSuspiciousness();
            } else if(statement.equals(statement12)) {
                statement12Suspiciousness =
                        suspiciousnessScore.getSuspiciousness();
            } else if(statement.equals(statement13)) {
                statement13Suspiciousness =
                        suspiciousnessScore.getSuspiciousness();
            } else {
                fail("One of the returned suspiciousness scores referred to a "
                + "non-existant statement: " + statement.toString());
            }
        }
        
        assertEquals(0.477D, statement1Suspiciousness, 0.0005D);
        assertEquals(0.477D, statement2Suspiciousness, 0.0005D);
        assertEquals(0.477D, statement3Suspiciousness, 0.0005D);
        assertEquals(0.477D, statement4Suspiciousness, 0.0005D);
        assertEquals(0.477D, statement5Suspiciousness, 0.0005D);
        assertEquals(0.577D, statement6Suspiciousness, 0.0005D);
        assertEquals(0.408D, statement7Suspiciousness, 0.0005D);
        assertEquals(0.378D, statement8Suspiciousness, 0.0005D);
        assertEquals(0.378D, statement9Suspiciousness, 0.0005D);
        assertEquals(0.378D, statement10Suspiciousness, 0.0005D);
        assertEquals(0.378D, statement11Suspiciousness, 0.0005D);
        assertEquals(0.378D, statement12Suspiciousness, 0.0005D);
        assertEquals(0.378D, statement13Suspiciousness, 0.0005D);
    }
}