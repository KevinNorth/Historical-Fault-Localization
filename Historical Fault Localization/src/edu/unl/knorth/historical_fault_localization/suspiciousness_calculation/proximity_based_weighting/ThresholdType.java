package edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.proximity_based_weighting;

/**
 * The types of lower and upper thresholds used to adjust test weightings.
 */
public enum ThresholdType {
    /** Indicates that the algorithm should not apply the threshold. */
    IGNORED,
    /** The threshold is the largest or smallest weighting that is not an
     * outlier. */
    TAIL,
    /** The threshold is the first or third quartile of test weightings. */
    QUARTILE;
}