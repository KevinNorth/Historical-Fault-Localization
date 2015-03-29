package edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.proximity_based_weighting;

import edu.unl.knorth.historical_fault_localization.DummyData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.StatementData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.TestData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;
import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.SuspiciousnessScore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
        
        assertEquals(0.447D, statement1Suspiciousness, 0.0005D);
        assertEquals(0.447D, statement2Suspiciousness, 0.0005D);
        assertEquals(0.447D, statement3Suspiciousness, 0.0005D);
        assertEquals(0.447D, statement4Suspiciousness, 0.0005D);
        assertEquals(0.447D, statement5Suspiciousness, 0.0005D);
        assertEquals(0.577D, statement6Suspiciousness, 0.0005D);
        assertEquals(0.408D, statement7Suspiciousness, 0.0005D);
        assertEquals(0.378D, statement8Suspiciousness, 0.0005D);
        assertEquals(0.378D, statement9Suspiciousness, 0.0005D);
        assertEquals(0.378D, statement10Suspiciousness, 0.0005D);
        assertEquals(0.378D, statement11Suspiciousness, 0.0005D);
        assertEquals(0.378D, statement12Suspiciousness, 0.0005D);
        assertEquals(0.378D, statement13Suspiciousness, 0.0005D);
    }
    
    @Test
    public void calculateCodeCoverageProximity() {
        TestExecutionData data = DummyData.getDummyData();
        ProximityBasedWeightingSuspiciousnessCalculator calculator =
                new ProximityBasedWeightingSuspiciousnessCalculator(
                        ThresholdType.IGNORED, ThresholdType.IGNORED);
        
        List<TestData> tests = data.getTests();
        TestData test1 = tests.get(0);
        TestData test2 = tests.get(1);
        TestData test3 = tests.get(2);
        TestData test4 = tests.get(3);
        TestData test5 = tests.get(4);
        
        double codeCoverageProximity =
                calculator.calculateCodeCoverageProximity(test1, test2);
        assertEquals(7D/8D, codeCoverageProximity, 0D);
        
        codeCoverageProximity =
                calculator.calculateCodeCoverageProximity(test1, test3);
        assertEquals(7D/13D, codeCoverageProximity, 0D);

        // This test case is especially important: It's one of only two test
        // cases where nether set of statements executed by one of the tests is
        // a subset of the other.
        codeCoverageProximity =
                calculator.calculateCodeCoverageProximity(test1, test4);
        assertEquals(6D/13D, codeCoverageProximity, 0D);
        codeCoverageProximity = // Put the tests in the other direction too
                calculator.calculateCodeCoverageProximity(test4, test1);
        assertEquals(6D/13D, codeCoverageProximity, 0D);

        
        codeCoverageProximity =
                calculator.calculateCodeCoverageProximity(test2, test3);
        assertEquals(8D/13D, codeCoverageProximity, 0D);

        // This test case is especially important: It's one of only two test
        // cases where nether set of statements executed by one of the tests is
        // a subset of the other.
        codeCoverageProximity =
                calculator.calculateCodeCoverageProximity(test2, test4);
        assertEquals(7D/13D, codeCoverageProximity, 0D);
        codeCoverageProximity = // Put the tests in the other direction too
                calculator.calculateCodeCoverageProximity(test2, test4);
        assertEquals(7D/13D, codeCoverageProximity, 0D);

        // This test case is especially important: It's the only one where the
        // set of statements executed by one of the tests is equal to the other.
        codeCoverageProximity =
                calculator.calculateCodeCoverageProximity(test4, test5);
        assertEquals(1D, codeCoverageProximity, 0D);
        codeCoverageProximity = // Put the tests in the other direction too
                calculator.calculateCodeCoverageProximity(test5, test4);
        assertEquals(1D, codeCoverageProximity, 0D);
    }
    
    @Test
    public void calculateUnadjustedProximityWeight() {
        TestExecutionData data = DummyData.getDummyData();
        // Add a failing test to test the algorithm more effectively
        List<StatementData> newFailingTestStatements = new LinkedList<>();
        for(int i = 1; i <= 6; i++) {
            newFailingTestStatements.add(new StatementData(i, "program.c"));
        }
        data.addTest(new TestData(false, newFailingTestStatements));
        
        ProximityBasedWeightingSuspiciousnessCalculator calculator =
                new ProximityBasedWeightingSuspiciousnessCalculator(
                        ThresholdType.IGNORED, ThresholdType.IGNORED);
        
        List<TestData> allTests = data.getTests();
        List<TestData> failedTests = data.getTests(false);

        TestData test = allTests.get(0);
        double expectedValue = ((7D/13D) + (1D/12D)) / 2D;
        Weighting actualValue = calculator
                .calculateUnadjustedProximityWeight(test, failedTests);
        assertEquals(expectedValue, actualValue.getWeighitng(), 0.000005D);
        assertTrue(actualValue.getTest() == test);

        test = allTests.get(1);
        expectedValue = ((8D/13D) + (1D/13D)) / 2D;
        actualValue = calculator
                .calculateUnadjustedProximityWeight(test, failedTests);
        assertEquals(expectedValue, actualValue.getWeighitng(), 0.000005D);
        assertTrue(actualValue.getTest() == test);

        test = allTests.get(3);
        expectedValue = ((12D/13D) + (5D/13D)) / 2D;
        actualValue = calculator
                .calculateUnadjustedProximityWeight(test, failedTests);
        assertEquals(expectedValue, actualValue.getWeighitng(), 0.000005D);
        assertTrue(actualValue.getTest() == test);

        test = allTests.get(4);
        expectedValue = ((12D/13D) + (5D/13D)) / 2D;
        actualValue = calculator
                .calculateUnadjustedProximityWeight(test, failedTests);
        assertEquals(expectedValue, actualValue.getWeighitng(), 0.000005D);
        assertTrue(actualValue.getTest() == test);

        // An exception should be thrown when a failing test is passed to
        // calculateUnadjustedProximityWeight().
        for(TestData failedTest : failedTests) {
            try {
                calculator.calculateUnadjustedProximityWeight(
                        failedTest, failedTests);
                fail("No exception was thrown when one should have been for "
                        + "trying to calculateUnadjustedProximityWeight() "
                        + "on a failing test.");
            } catch(IllegalArgumentException err) {
                // This is the exception that should be thrown
            } catch(Exception err) {
                fail("The wrong kind of exception was thrown when an "
                    + "IllegalArgumentException should have been thrown from "
                    + "calculateUnadjustedProximityWeight(). Actual exception "
                    + "thrown was: " + err.toString());
            }
        }
    }
    
    @Test
    public void calculateThreshold() {
        TestExecutionData data = DummyData.getDummyData();
        ProximityBasedWeightingSuspiciousnessCalculator calculator =
                new ProximityBasedWeightingSuspiciousnessCalculator(
                        ThresholdType.IGNORED, ThresholdType.IGNORED);

        // This test assumes that calculateUnadjustedProximityWeight() works
        // correctly. If it does not, this test will most likely fail, although
        // calculateThreshold() may be programmed correctly.
        List<Weighting> unadjustedWeightings =
                getUnadjustedWeightings(data, calculator);
                
        // Test ignored thresholds
        double threshold = calculator.calculateThreshold(unadjustedWeightings,
                ThresholdType.IGNORED, false);
        assertTrue(threshold == Double.NEGATIVE_INFINITY);

        threshold = calculator.calculateThreshold(unadjustedWeightings,
                ThresholdType.IGNORED, true);
        assertTrue(threshold == Double.POSITIVE_INFINITY);
        
        // Test quartile values
        threshold = calculator.calculateThreshold(unadjustedWeightings,
                ThresholdType.QUARTILE, false);
        assertEquals(0.57692, threshold, 0.000005D);
        threshold = calculator.calculateThreshold(unadjustedWeightings,
                ThresholdType.QUARTILE, true);
        assertEquals(0.92308, threshold, 0.000005D);

        // Test tail values
        threshold = calculator.calculateThreshold(unadjustedWeightings,
                ThresholdType.TAIL, false);
        assertEquals(0.05769, threshold, 0.000005D);
        threshold = calculator.calculateThreshold(unadjustedWeightings,
                ThresholdType.TAIL, true);
        assertEquals(1.44231, threshold, 0.000005D);
    }
    
    @Test
    public void calculateAdjustedWeightings() {
        TestExecutionData data = DummyData.getDummyData();
        ProximityBasedWeightingSuspiciousnessCalculator calculator =
                new ProximityBasedWeightingSuspiciousnessCalculator(
                        ThresholdType.IGNORED, ThresholdType.IGNORED);
        
        List<Weighting> unadjustedWeightings =
                getUnadjustedWeightings(data, calculator);
        
        // We don't have to use the calculated threshold values - we can specify
        // our own values, and the algorithm should still work correctly. So
        // we test on several of our own thresholds.
        List<Weighting> results =
                calculator.calculateAdjustedWeightings(unadjustedWeightings,
                        Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        Collections.sort(results);
        assertTrue(results.size() == 4);
        assertEquals(0.53846, results.get(0).getWeighitng(), 0.000005D);
        assertEquals(0.61538, results.get(1).getWeighitng(), 0.000005D);
        assertEquals(0.92308, results.get(2).getWeighitng(), 0.000005D);
        assertEquals(0.92308, results.get(3).getWeighitng(), 0.000005D);

        results = calculator.calculateAdjustedWeightings(unadjustedWeightings,
                        0.55D, 0.90D);
        Collections.sort(results);
        assertTrue(results.size() == 4);
        assertEquals(0.07692, results.get(0).getWeighitng(), 0.000005D);
        assertEquals(0.07692, results.get(1).getWeighitng(), 0.000005D);
        assertEquals(0.46154, results.get(2).getWeighitng(), 0.000005D);
        assertEquals(0.61538, results.get(3).getWeighitng(), 0.000005D);

        results = calculator.calculateAdjustedWeightings(unadjustedWeightings,
                        0.50D, 0.60D);
        Collections.sort(results);
        assertTrue(results.size() == 4);
        assertEquals(0.07692, results.get(0).getWeighitng(), 0.000005D);
        assertEquals(0.07692, results.get(1).getWeighitng(), 0.000005D);
        assertEquals(0.38462, results.get(2).getWeighitng(), 0.000005D);
        assertEquals(0.53846, results.get(3).getWeighitng(), 0.000005D);
    }
    
    @Test
    public void calculateScalingFactor() {
        TestExecutionData data = DummyData.getDummyData();
        ProximityBasedWeightingSuspiciousnessCalculator calculator =
                new ProximityBasedWeightingSuspiciousnessCalculator(
                        ThresholdType.IGNORED, ThresholdType.IGNORED);
                
        List<Weighting> adjustedWeighting =
                getAdjustedWeightings(data, calculator);
        
        double scalingFactor =
                calculator.calculateScalingFactor(adjustedWeighting);
        
        assertEquals(4D/3D, scalingFactor, 0.000005D);
    }
    
    @Test
    public void calculateFinalWeightings() {
        TestExecutionData data = DummyData.getDummyData();
        ProximityBasedWeightingSuspiciousnessCalculator calculator =
                new ProximityBasedWeightingSuspiciousnessCalculator(
                        ThresholdType.IGNORED, ThresholdType.IGNORED);

        List<Weighting> adjustedWeightings =
                getAdjustedWeightings(data, calculator);
        double scalingFactor =
                calculator.calculateScalingFactor(adjustedWeightings);
        
        List<Weighting> finalWeightings = calculator
                .calculateFinalWeightings(adjustedWeightings, scalingFactor);
        
        Collections.sort(finalWeightings);
        assertTrue(finalWeightings.size() == 4);
        assertEquals(0.538462 * 4D/3D,
                finalWeightings.get(0).getWeighitng(), 0.000005D);
        assertEquals(0.615385 * 4D/3D,
                finalWeightings.get(1).getWeighitng(), 0.000005D);
        assertEquals(0.923077 * 4D/3D,
                finalWeightings.get(2).getWeighitng(), 0.000005D);
        assertEquals(0.923077 * 4D/3D,
                finalWeightings.get(3).getWeighitng(), 0.000005D);
    }
    
    @Test
    public void calculateSuspiciousness() {
        TestExecutionData data = DummyData.getDummyData();
        ProximityBasedWeightingSuspiciousnessCalculator calculator =
                new ProximityBasedWeightingSuspiciousnessCalculator(
                        ThresholdType.IGNORED, ThresholdType.IGNORED);
        
        List<Weighting> finalWeightings =
                getFinalWeightings(data, calculator);
        int numFailingTests = data.getTests(false).size();
        
        SuspiciousnessScore result = calculator.calculateSuspiciousnessScore(
                new StatementData(1, "program.c"), data, numFailingTests,
                finalWeightings);
        assertEquals(result.getStatement(), new StatementData(1, "program.c"));
        assertEquals(0.447D, result.getSuspiciousness(), 0.0005D);

        result = calculator.calculateSuspiciousnessScore(
                new StatementData(2, "program.c"), data, numFailingTests,
                finalWeightings);
        assertEquals(result.getStatement(), new StatementData(2, "program.c"));
        assertEquals(0.447D, result.getSuspiciousness(), 0.0005D);

        result = calculator.calculateSuspiciousnessScore(
                new StatementData(3, "program.c"), data, numFailingTests,
                finalWeightings);
        assertEquals(result.getStatement(), new StatementData(3, "program.c"));
        assertEquals(0.447D, result.getSuspiciousness(), 0.0005D);

        result = calculator.calculateSuspiciousnessScore(
                new StatementData(4, "program.c"), data, numFailingTests,
                finalWeightings);
        assertEquals(result.getStatement(), new StatementData(4, "program.c"));
        assertEquals(0.447D, result.getSuspiciousness(), 0.0005D);

        result = calculator.calculateSuspiciousnessScore(
                new StatementData(5, "program.c"), data, numFailingTests,
                finalWeightings);
        assertEquals(result.getStatement(), new StatementData(5, "program.c"));
        assertEquals(0.447D, result.getSuspiciousness(), 0.0005D);

        result = calculator.calculateSuspiciousnessScore(
                new StatementData(6, "program.c"), data, numFailingTests,
                finalWeightings);
        assertEquals(result.getStatement(), new StatementData(6, "program.c"));
        assertEquals(0.577D, result.getSuspiciousness(), 0.0005D);

        result = calculator.calculateSuspiciousnessScore(
                new StatementData(7, "program.c"), data, numFailingTests,
                finalWeightings);
        assertEquals(result.getStatement(), new StatementData(7, "program.c"));
        assertEquals(0.408D, result.getSuspiciousness(), 0.0005D);

        result = calculator.calculateSuspiciousnessScore(
                new StatementData(8, "program.c"), data, numFailingTests,
                finalWeightings);
        assertEquals(result.getStatement(), new StatementData(8, "program.c"));
        assertEquals(0.378D, result.getSuspiciousness(), 0.0005D);

        result = calculator.calculateSuspiciousnessScore(
                new StatementData(9, "program.c"), data, numFailingTests,
                finalWeightings);
        assertEquals(result.getStatement(), new StatementData(9, "program.c"));
        assertEquals(0.378D, result.getSuspiciousness(), 0.0005D);

        result = calculator.calculateSuspiciousnessScore(
                new StatementData(10, "program.c"), data, numFailingTests,
                finalWeightings);
        assertEquals(result.getStatement(), new StatementData(10, "program.c"));
        assertEquals(0.378D, result.getSuspiciousness(), 0.0005D);

        result = calculator.calculateSuspiciousnessScore(
                new StatementData(11, "program.c"), data, numFailingTests,
                finalWeightings);
        assertEquals(result.getStatement(), new StatementData(11, "program.c"));
        assertEquals(0.378D, result.getSuspiciousness(), 0.0005D);

        result = calculator.calculateSuspiciousnessScore(
                new StatementData(12, "program.c"), data, numFailingTests,
                finalWeightings);
        assertEquals(result.getStatement(), new StatementData(12, "program.c"));
        assertEquals(0.378D, result.getSuspiciousness(), 0.0005D);

        result = calculator.calculateSuspiciousnessScore(
                new StatementData(13, "program.c"), data, numFailingTests,
                finalWeightings);
        assertEquals(result.getStatement(), new StatementData(13, "program.c"));
        assertEquals(0.378D, result.getSuspiciousness(), 0.0005D);
    }
    
    private List<Weighting> getUnadjustedWeightings(TestExecutionData data,
            ProximityBasedWeightingSuspiciousnessCalculator calculator) {
        List<Weighting> unadjustedWeightings = new ArrayList<>();

        for(TestData passingTest : data.getTests(true)) {
            Weighting weighting = calculator.calculateUnadjustedProximityWeight(
                    passingTest, data.getTests(false));
            unadjustedWeightings.add(weighting);
        }
        
        return unadjustedWeightings;
    }
    
    private List<Weighting> getAdjustedWeightings(TestExecutionData data,
            ProximityBasedWeightingSuspiciousnessCalculator calculator) {
        List<Weighting> unadjustedWeightings =
                getUnadjustedWeightings(data, calculator);
        ThresholdType lowerThresholdType = calculator.getLowerThresdholdType();
        ThresholdType upperThresholdType = calculator.getUpperThresdholdType();
        
        double lowerThreshold = calculator.calculateThreshold(
                unadjustedWeightings, lowerThresholdType, false);
        double upperThreshold = calculator.calculateThreshold(
                unadjustedWeightings, upperThresholdType, true);

        return calculator.calculateAdjustedWeightings(unadjustedWeightings,
                lowerThreshold, upperThreshold);
    }
    
    private List<Weighting> getFinalWeightings(TestExecutionData data,
            ProximityBasedWeightingSuspiciousnessCalculator calculator) {
        List<Weighting> adjustedWeightings =
                getAdjustedWeightings(data, calculator);
        double scalingFactor =
                calculator.calculateScalingFactor(adjustedWeightings);
        return calculator.calculateFinalWeightings(adjustedWeightings,
                scalingFactor);
    }
}