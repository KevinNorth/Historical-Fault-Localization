package edu.unl.knorth.historical_fault_localization.suspiciousness_calculation;

import edu.unl.knorth.historical_fault_localization.intermediate_data.StatementData;

/**
 * Associates statements with their suspiciousness scores. I believe using a
 * small class is safer and more clear than, for example, using a dictionary to
 * associate suspiciousness scores to statements.
 */
public final class SuspiciousnessScore
        implements Comparable<SuspiciousnessScore> {
    private final StatementData statement;
    private final double suspiciousness;

    public SuspiciousnessScore(StatementData statement, double suspiciousness) {
        this.statement = statement;
        this.suspiciousness = suspiciousness;
    }

    public StatementData getStatement() {
        return statement;
    }

    public double getSuspiciousness() {
        return suspiciousness;
    }

    /**
     * Compares SuspiciousnessScores by their <code>suspiciousness</code>
     * values.
     * @param other The SuspiciousnessScore to compare to.
     * @return A negative value, 0, or a positive value if
     * <code>this.suspiciousness</code> is less than, equal to, or greater than
     * <code>other.suspiciousness</code>, respectively.
     */
    @Override
    public int compareTo(SuspiciousnessScore other) {
        return Double.compare(this.suspiciousness, other.suspiciousness);
    }
}