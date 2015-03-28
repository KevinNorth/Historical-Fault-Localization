package edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.proximity_based_weighting;

import edu.unl.knorth.historical_fault_localization.intermediate_data.TestData;

/**
 * Associates tests with their weightings for the intermediate steps in the
 * proximity-based weighting algorithm. This class is reused multiple times for
 * different types of weightings. In particular, it's used for:
 * <ol>
 * <li>The initial weightings before especially high and low values are
 * adjusted</li>
 * <li>The weightings immediately after high and large values are adjusted</li>
 * <li>The final weightings once they are scaled to produce suspiciousness
 * scores comparable to other algorithms' suspiciousness scores</li>
 * </ol>
 * Since this class just keeps track of a double value to associate to a test,
 * it can be used for all three cases without any adjustment.
 */
public class Weighting implements Comparable<Weighting> {
    private final TestData test;
    private final double weighitng;

    public Weighting(TestData test, double weighitng) {
        this.test = test;
        this.weighitng = weighitng;
    }

    public TestData getTest() {
        return test;
    }

    public double getWeighitng() {
        return weighitng;
    }

    /**
     * Compares Weightings by their <code>weighting</code> values.
     * @param other The Weighting to compare to.
     * @return A negative value, 0, or a positive value if
     * <code>this.weighting</code> is less than, equal to, or greater than
     * <code>other.weighting</code>, respectively.
     */
    @Override
    public int compareTo(Weighting other) {
        return Double.compare(this.weighitng, other.weighitng);
    }
}