package edu.unl.knorth.historical_fault_localization.intermediate_data;

import edu.unl.knorth.historical_fault_localization.DummyData;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestExecutionDataTest {
    @Test
    public void getTestsThatExecuteStatment() {
        TestExecutionData data = DummyData.getDummyData();
        
        List<TestData> selectedTests =
                data.getTestsThatExecuteStatment(new StatementData(1, "program.c"));
        assertTrue(selectedTests.size() == 3);
        
        selectedTests =
                data.getTestsThatExecuteStatment(new StatementData(6, "program.c"));
        assertTrue(selectedTests.size() == 3);

        selectedTests =
                data.getTestsThatExecuteStatment(new StatementData(7, "program.c"));
        assertTrue(selectedTests.size() == 4);

        selectedTests =
                data.getTestsThatExecuteStatment(new StatementData(8, "program.c"));
        assertTrue(selectedTests.size() == 5);
    }
    
    @Test
    public void getTestsThatExecuteStatmentWithPassingFlag() {
        TestExecutionData data = DummyData.getDummyData();

        List<TestData> selectedTests =
                data.getTestsThatExecuteStatment(new StatementData(1, "program.c"),
                true);
        assertTrue(selectedTests.size() == 2);

        selectedTests =
                data.getTestsThatExecuteStatment(new StatementData(1, "program.c"),
                false);
        assertTrue(selectedTests.size() == 1);
        
        selectedTests =
                data.getTestsThatExecuteStatment(new StatementData(6, "program.c"),
                true);
        assertTrue(selectedTests.size() == 2);
        
        selectedTests =
                data.getTestsThatExecuteStatment(new StatementData(6, "program.c"),
                false);
        assertTrue(selectedTests.size() == 1);
        
        selectedTests =
                data.getTestsThatExecuteStatment(new StatementData(7, "program.c"),
                true);
        assertTrue(selectedTests.size() == 3);
        
        selectedTests =
                data.getTestsThatExecuteStatment(new StatementData(7, "program.c"),
                false);
        assertTrue(selectedTests.size() == 1);
        
        selectedTests =
                data.getTestsThatExecuteStatment(new StatementData(8, "program.c"),
                true);
        assertTrue(selectedTests.size() == 4);
        
        selectedTests =
                data.getTestsThatExecuteStatment(new StatementData(8, "program.c"),
                false);
        assertTrue(selectedTests.size() == 1);
    }
    
    @Test
    public void addStatementInsertsNewStatemets() {
        TestExecutionData data = DummyData.getDummyData();
        StatementData newStatement = new StatementData(14, "program.c");
        data.addStatement(newStatement);
        assertTrue(data.getStatements().size() == 14);
        assertTrue(data.getStatements().contains(newStatement));
    }

    @Test
    public void addStatementsIgnoresDuplicateStatemets() {
        TestExecutionData data = DummyData.getDummyData();
        StatementData duplicateStatement = new StatementData(1, "program.c");
        data.addStatement(duplicateStatement);
        assertTrue(data.getStatements().size() == 13); // Stays at 13
        assertTrue(data.getStatements().contains(duplicateStatement));
    }
    
    @Test
    public void addStatementsInsertsNewStatemets() {
        TestExecutionData data = DummyData.getDummyData();
        List<StatementData> newStatements = new ArrayList<>();
        newStatements.add(new StatementData(14, "program.c"));
        newStatements.add(new StatementData(15, "program.c"));
        newStatements.add(new StatementData(16, "program.c"));
        data.addStatements(newStatements);
        assertTrue(data.getStatements().size() == 16);
        
        for(StatementData newStatement : newStatements) {
            assertTrue(data.getStatements().contains(newStatement));
        }
    }

    @Test
    public void addStatementIgnoresDuplicateStatemets() {
        TestExecutionData data = DummyData.getDummyData();
        List<StatementData> duplicateStatements = new ArrayList<>();
        duplicateStatements.add(new StatementData(1, "program.c"));
        duplicateStatements.add(new StatementData(2, "program.c"));
        duplicateStatements.add(new StatementData(3, "program.c"));
        data.addStatements(duplicateStatements);
        assertTrue(data.getStatements().size() == 13); // Stays at 13
        
        for(StatementData newStatement : duplicateStatements) {
            assertTrue(data.getStatements().contains(newStatement));
        }
    }

    @Test
    public void addTest() {
        TestExecutionData data = DummyData.getDummyData();
        
        List<StatementData> testStatements = new ArrayList<>();
        testStatements.add(new StatementData(1, "program.c"));
        testStatements.add(new StatementData(14, "program.c"));
        
        TestData newTest = new TestData(true, testStatements);
        data.addTest(newTest);
        
        assertTrue(data.getTests().size() == 6);
        assertTrue(data.getTests().contains(newTest));
        // Add the new statement, but not the duplicate
        assertTrue(data.getStatements().size() == 14);
        assertTrue(data.getStatements().contains(
                new StatementData(1, "program.c")));
        assertTrue(data.getStatements().contains(
                new StatementData(14, "program.c")));
    }
    
        @Test
    public void addTests() {
        TestExecutionData data = DummyData.getDummyData();
        
        List<StatementData> test1Statements = new ArrayList<>();
        test1Statements.add(new StatementData(1, "program.c"));
        test1Statements.add(new StatementData(14, "program.c"));

        List<StatementData> test2Statements = new ArrayList<>();
        test2Statements.add(new StatementData(2, "program.c"));
        // The new statement 14 should be only added once, not twice
        test2Statements.add(new StatementData(14, "program.c"));
        test2Statements.add(new StatementData(15, "program.c"));
        
        List<TestData> newTests = new ArrayList<>();
        newTests.add(new TestData(true, test1Statements));
        newTests.add(new TestData(false, test2Statements));
        data.addTests(newTests);
        
        assertTrue(data.getTests().size() == 7);
        assertTrue(data.getTests().contains(newTests.get(0)));
        assertTrue(data.getTests().contains(newTests.get(1)));
        // Add the new statements, but not the duplicate
        assertTrue(data.getStatements().size() == 15);
        assertTrue(data.getStatements().contains(
                new StatementData(1, "program.c")));
        assertTrue(data.getStatements().contains(
                new StatementData(2, "program.c")));
        assertTrue(data.getStatements().contains(
                new StatementData(14, "program.c")));
        assertTrue(data.getStatements().contains(
                new StatementData(15, "program.c")));
    }
}