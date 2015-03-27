package edu.unl.knorth.historical_fault_localization.intermediate_data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single test.
 */
public final class TestData {
    private final boolean passed;
    private final Set<StatementData> statementsExecuted;

    /**
     * @param passed Whether the test passed or failed.
     * @param statementsExecuted The statements that the test executed.
     */
    public TestData(boolean passed, Collection<StatementData> statementsExecuted) {
        this.passed = passed;
        this.statementsExecuted =
                Collections.unmodifiableSet(new HashSet<>(statementsExecuted));
    }
    
    /**
     * @param passed Whether the test passed or failed.
     * @param statementsExecuted The statements that the test executed.
     */
    public TestData(boolean passed, StatementData... statementsExecuted) {
        this.passed = passed;
        this.statementsExecuted =
            Collections.unmodifiableSet(
                new HashSet<>(Arrays.asList(statementsExecuted)));
    }

    /**
     * Determines whether this test executed the specified statement.
     * @param statement The statement to check.
     * @return <code>true</code> if this test executed the specified statement.
     * <code>false</code> otherwise.
     */
    public boolean executedStatement(StatementData statement) {
        return statementsExecuted.contains(statement);
    }
    
    /**
     * @return Whether this test passed.
     */
    public boolean getPassed() {
        return passed;
    }

    /**
     * @return The Set of statements this test executed. This Set is read-only.
     */
    public Set<StatementData> getStatementsExecuted() {
        return statementsExecuted;
    }

    @Override
    public String toString() {
        return "Test{" + "passed=" + passed +
                ", statementsExecuted=" + statementsExecuted + '}';
    }
}