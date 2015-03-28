package edu.unl.knorth.historical_fault_localization.intermediate_data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Keeps track of the results from running and profiling all of the tests for a
 * single revision of the code under test.
 */
public class TestExecutionData {
    // Statements are in a set because there wil never be identical statements.
    private final Set<StatementData> statements;
    // Tests are in a list because two tests can have identical values, but
    // still be the results from two different tests, so unlike the Set of
    // statements, we don't want to silently ignore duplicate tests.
    private final List<TestData> tests;

    public TestExecutionData() {
        statements = new HashSet<>();
        tests = new ArrayList<>();
    }
    
    /**
     * Gets all of the tests that executed the specified statement.
     * @param statement This method returns tests that executed this statement.
     * @return A List of tests that executed the specified statement.
     */
    public List<TestData> getTestsThatExecuteStatment(StatementData statement) {
        List<TestData> selectedTests = new ArrayList<>();
        
        for(TestData test : tests) {
            if(test.executedStatement(statement)) {
                selectedTests.add(test);
            }
        }
        
        return selectedTests;
    }
    
    /**
     * Gets all of the tests that both executed the specified statement and
     * passed or failed, depending on what arguments are passed in.
     * @param statement This method returns tests that executed this statement.
     * @param passing If <code>true</code>, this method only returns tests that
     * passed. If <code>false</code>, this method only returns tests that
     * failed.
     * @return All of the tests that both executed the specified statement and
     * passed if <code>passing == true</code> or failed if
     * <code>passing == false</code>.
     */
    public List<TestData> getTestsThatExecuteStatment(StatementData statement,
            boolean passing) {
        List<TestData> partialTests = getTestsThatExecuteStatment(statement);
        List<TestData> selectedTests = new ArrayList<>();
        
        for(TestData test : partialTests) {
            if(test.getPassed() == passing) {
                selectedTests.add(test);
            }
        }
        
        return selectedTests;
    }
    
    /**
     * Adds a statement to the statements this TestExecutionData keeps track of.
     * If the statement is already being kept track of, no changes are made.
     * @param statement The statement to add.
     */
    public void addStatement(StatementData statement) {
        statements.add(statement);
    }
    
    /**
     * Adds to the statements this TestExecutionData keeps track of. Any
     * statements that are already being kept track of are ignored.
     * @param statements The statements to add.
     */
    public void addStatements(Collection<StatementData> statements) {
        this.statements.addAll(statements);
    }
    
    /**
     * Adds a test to the tests this TestExecutionData keeps track of. Any
     * statements that the test executed are automatically added to the
     * statements this TestExecutionData keeps track of.
     * @param test The test to add.
     */
    public void addTest(TestData test) {
        statements.addAll(test.getStatementsExecuted());
        tests.add(test);
    }
    
    /**
     * Adds to the tests this TestExecutionData keeps track of. Any statements
     * that the tests executed are automatically added to the statements this
     * TestExecutionData keeps track of.
     * @param tests The tests to add.
     */
    public void addTests(Collection<TestData> tests) {
        for(TestData test : tests) {
            statements.addAll(test.getStatementsExecuted());
        }
        
        this.tests.addAll(tests);
    }

    /**
     * @return The statements that this TestExecutionData keeps track of. The
     * Set that is returned is read-only.
     */
    public Set<StatementData> getStatements() {
        return Collections.unmodifiableSet(statements);
    }

    /**
     * @return The tests that this TestExecutionData keeps track of. The List
     * that is returned is read-only.
     */
    public List<TestData> getTests() {
        return Collections.unmodifiableList(tests);
    }
    
    /**
     * Gets only the passing or failing tests that this TestExecutionData keeps
     * track of.
     * @param passing Pass <code>true</code> to get only passing tests. Pass
     * <code>false</code> to get only failing tests.
     * @return All of the passing tests that this TestExecutionData keeps track
     * of if <code>passing == true</code>. All of the failing tests that this
     * TestExeuctionData keeps track of it <code>passing == false</code>.
     */
    public List<TestData> getTests(boolean passing) {
        List<TestData> selectedTests = new ArrayList<>();
        
        for(TestData test : tests) {
            if(test.getPassed() == passing) {
                selectedTests.add(test);
            }
        }
        
        return Collections.unmodifiableList(selectedTests);
    }

    @Override
    public String toString() {
        return "TestExecutionData{" + "statements=" + statements +
                ", tests=" + tests + '}';
    }
}