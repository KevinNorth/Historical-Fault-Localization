package edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.proximity_based_weighting;

import edu.unl.knorth.historical_fault_localization.intermediate_data.StatementData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.TestData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;
import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.SuspiciousnessCalculator;
import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.SuspiciousnessScore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Implements the proximity-based weighting algorithm described in the paper
 * <a href="http://dx.doi.org/10.1109/ASE.2011.6100088">
 * Proximity Based Weighting of Test Cases to Improve Spectrum Based Fault
 * Localization by A. Bandyopadhyay and S. Ghosh</a>.
 * <p/>
 * All of the calculation methods with no side effects are protected instead of
 * private to make it easier to test them.
 */
public class ProximityBasedWeightingSuspiciousnessCalculator
        extends SuspiciousnessCalculator {
    private final ThresholdType lowerThresdholdType;
    private final ThresholdType upperThresdholdType;

    /**
     * @param lowerThresdholdType What kind of lower threshold the algorithm
     * should use for the second step of 
     * @param upperThresdholdType 
     */
    public ProximityBasedWeightingSuspiciousnessCalculator(ThresholdType
            lowerThresdholdType, ThresholdType upperThresdholdType) {
        this.lowerThresdholdType = lowerThresdholdType;
        this.upperThresdholdType = upperThresdholdType;
    }
    
    @Override
    public List<SuspiciousnessScore> calculateSuspiciousness(
            TestExecutionData testExecutionData) {
        List<TestData> passingTests = testExecutionData.getTests(true);
        List<TestData> failingTests = testExecutionData.getTests(false);
        
        List<Weighting> unadjustedWeightings = new ArrayList<>();
        for(TestData passingTest : passingTests) {
            unadjustedWeightings.add(
                    calculateUnadjustedProximityWeight(passingTest,
                            failingTests));
        }
        
        double lowerThreshold = calculateThreshold(unadjustedWeightings,
                lowerThresdholdType, false);
        double upperThreshold = calculateThreshold(unadjustedWeightings,
                upperThresdholdType, true);
        
        List<Weighting> adjustedWeightings =
                calculateAdjustedWeightings(unadjustedWeightings,
                        lowerThreshold, upperThreshold);
        
        double scalingFactor = calculateScalingFactor(adjustedWeightings);
        
        List<Weighting> finalWeightings =
                calculateFinalWeightings(adjustedWeightings, scalingFactor);
        
        List<SuspiciousnessScore> suspiciousnessScores = new ArrayList<>();
        int numFailingTests = failingTests.size();
        for(StatementData statement : testExecutionData.getStatements()) {
            suspiciousnessScores.add(calculateSuspiciousnessScore(statement,
                    testExecutionData, numFailingTests, finalWeightings));
        }
        
        return suspiciousnessScores;
    }
    
    /**
     * Calculates the code-coverage proximity between two tests.
     * @param t1 One of the tests to process.
     * @param t2 One of the tests to process.
     * @return The code-coverage proximity between the two tests.
     */
    protected double calculateCodeCoverageProximity(TestData t1, TestData t2) {
        Set<StatementData> t1Statements = t1.getStatementsExecuted();
        Set<StatementData> t2Statements = t2.getStatementsExecuted();
        
        Set<StatementData> union = new HashSet<>(t1Statements);
        union.addAll(t2Statements);
        
        Set<StatementData> intersection = new HashSet<>(t1Statements);
        intersection.retainAll(t2Statements);
        
        return ((double) intersection.size()) / ((double) union.size());
    }
    
    /**
     * Calculates the proximity-based weighting for a passing test. This is
     * the weighting before especially high and low values are adjusted and
     * before all weightings are scaled.
     * @param test The passing test to calculate proximity-based weighting for.
     * @param failingTests A list of all failing tests.
     * @return The unadjusted proximity-based weighting for a passing test.
     * @throws IllegalArgumentException If <code>test</code> doesn't represent
     * a passing test; that is, if <code>test.getPassing() == false</code>.
     */
    protected Weighting calculateUnadjustedProximityWeight(TestData test,
            List<TestData> failingTests) {
        if(test.getPassed() == false) {
            throw new IllegalArgumentException("Proximity weighing is only "
            + "defined for passing tests.");
        }
        
        double sum = 0D;
        for(TestData failingTest : failingTests) {
            sum += calculateCodeCoverageProximity(test, failingTest);
        }
        
        return new Weighting(test, sum / (double) failingTests.size());
    }
    
    /**
     * Calculates the thresholds to use to adjust especially high and low
     * weighting values.
     * @param unadjustedWeightings A list containing all of the unadjusted
     * weightings.
     * @param thresholdType What type of threshold to calculate
     * @param findUpperThreshold <code>true</code> to calculate the upper
     * threshold, <code>false</code> to calculate the lower threshold.
     * @return The value to use for the specified threshold.
     */
    protected double calculateThreshold(List<Weighting> unadjustedWeightings,
            ThresholdType thresholdType, boolean findUpperThreshold) {
        switch(thresholdType) {
            case IGNORED:
                if(findUpperThreshold) {
                    return Double.POSITIVE_INFINITY;
                } else {
                    return Double.NEGATIVE_INFINITY;
                }
            case QUARTILE:
                return calculateQuartile(unadjustedWeightings,
                        findUpperThreshold);
            case TAIL:
                double upperQuartile = calculateQuartile(unadjustedWeightings,
                        true);
                double lowerQuartile = calculateQuartile(unadjustedWeightings,
                        false);
                double interquartileRange = upperQuartile - lowerQuartile;
                if(findUpperThreshold) {
                    return upperQuartile + (interquartileRange * 1.5);
                } else {
                    return lowerQuartile - (interquartileRange * 1.5);
                }
            default:
                throw new RuntimeException("Switch statement fell through");
        }
    }
    
    private double calculateQuartile(List<Weighting> weightings,
            boolean findThirdQuartile) {
        Collections.sort(weightings);
        if(weightings.size() % 2 == 1) {
            if(findThirdQuartile) {
                int index = weightings.size() * 3 / 4 - 1;
                return weightings.get(index).getWeighitng();
            } else {
                int index = weightings.size() / 4 - 1;
                return weightings.get(index).getWeighitng();
            }
        } else {
            if(findThirdQuartile) {
                int index1 = weightings.size() * 3 / 4 - 1;
                int index2 = weightings.size() * 3 / 4;
                double value1 = weightings.get(index1).getWeighitng();
                double value2 = weightings.get(index2).getWeighitng();
                return (value1 + value2) / 2D;
            } else {
                int index1 = weightings.size() / 4 - 1;
                int index2 = weightings.size() / 4;
                double value1 = weightings.get(index1).getWeighitng();
                double value2 = weightings.get(index2).getWeighitng();
                return (value1 + value2) / 2D;
            }
        }
    }
    
    /**
     * Uses the threshold values to adjust very large and very small weightings
     * to more moderate values.
     * @param unadjustedWeightings The weightings to run through the adjustment
     * algorithm.
     * @param lowerThreshold The value of the lower threshold.
     * @param upperThreshold The value of the upper threshold.
     * @return All of the weightings, with weightings with values that are
     * higher or lower than the thresholds adjusted to more moderate values.
     */
    protected List<Weighting>
        calculateAdjustedWeightings(List<Weighting> unadjustedWeightings,
                double lowerThreshold, double upperThreshold) {
        // We won't be sorting this list, so we can use a LinkedList for
        // faster insertions instead of an ArrayList for faster rearranging
        List<Weighting> adjustedWeightings = new LinkedList<>();

        for(Weighting unadjustedWeighting : unadjustedWeightings) {
            double oldVal = unadjustedWeighting.getWeighitng();
            double newVal;

            if(oldVal < lowerThreshold) {
                newVal = 1 - oldVal;
            } else if(oldVal > upperThreshold) {
                newVal = 1 - oldVal;
            } else {
                newVal = oldVal;
            }

            adjustedWeightings.add(
                    new Weighting(unadjustedWeighting.getTest(), newVal));
        }

        return adjustedWeightings;
    }
        
    protected double calculateScalingFactor(List<Weighting> adjustedWeightings) {
        double sum = 0D;
        
        for(Weighting adjustedWeighting : adjustedWeightings) {
            sum += adjustedWeighting.getWeighitng();
        }
        
        return ((double) adjustedWeightings.size()) / sum;
    }
    
    protected List<Weighting> calculateFinalWeightings(
            List<Weighting> adjustedWeightings, double scalingFactor) {
        // We won't be sorting this list, so we can use a LinkedList for
        // faster insertions instead of an ArrayList for faster rearranging
        List<Weighting> finalWeightings = new LinkedList<>();

        for(Weighting adjustedWeighting : adjustedWeightings) {
            double oldVal = adjustedWeighting.getWeighitng();
            double newVal = oldVal * scalingFactor;
            
            finalWeightings.add(
                    new Weighting(adjustedWeighting.getTest(), newVal));
        }
        
        return finalWeightings;
    }
    
    protected SuspiciousnessScore calculateSuspiciousnessScore(
            StatementData statement, TestExecutionData testExecutionData,
            int numFailingTests, List<Weighting> finalWeightings) {
        List<TestData> relevantFailingTests =
                testExecutionData.getTestsThatExecuteStatment(statement, false);
        List<TestData> relevantPassingTests =
                testExecutionData.getTestsThatExecuteStatment(statement, true);
        int numRelevantFailingTests = relevantFailingTests.size();
        
        double sum = 0D;
        for(Weighting weighting : finalWeightings) {
            boolean flag = false;
            
            for(TestData passingTest : relevantPassingTests) {
                if(passingTest == weighting.getTest()) {
                    flag = true;
                    break;
                }
            }
            
            if(flag) {
                sum += weighting.getWeighitng();
            }
        }
        
        double suspiciousness =  ((double) numRelevantFailingTests)
                / (((double) numFailingTests)
                    * (sum + ((double) numRelevantFailingTests)));
        return new SuspiciousnessScore(statement, suspiciousness);
    }
}