package edu.unl.knorth.historical_fault_localization;

import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.StatementData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.TestData;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class DummyDataTest {
    /**
     * This test ensures that DummyData, which we can use for test data for our
     * tests, produces the correct data. The data is taken from the paper on
     * Proximity-Based Weighting fault localization
     * (<a href="http://dx.doi.org/10.1109/ASE.2011.6100088">
     * http://dx.doi.org/10.1109/ASE.2011.6100088</a>) on page 421.
     */
    @Test
    public void producesCorrectDummyData() {
        TestExecutionData dummyData = DummyData.getDummyData();

        Set<StatementData> statements = dummyData.getStatements();        
        assertTrue(statements.contains(new StatementData(1, "program.c")));
        assertTrue(statements.contains(new StatementData(2, "program.c")));
        assertTrue(statements.contains(new StatementData(3, "program.c")));
        assertTrue(statements.contains(new StatementData(4, "program.c")));
        assertTrue(statements.contains(new StatementData(5, "program.c")));
        assertTrue(statements.contains(new StatementData(6, "program.c")));
        assertTrue(statements.contains(new StatementData(7, "program.c")));
        assertTrue(statements.contains(new StatementData(8, "program.c")));
        assertTrue(statements.contains(new StatementData(9, "program.c")));
        assertTrue(statements.contains(new StatementData(10, "program.c")));
        assertTrue(statements.contains(new StatementData(11, "program.c")));
        assertTrue(statements.contains(new StatementData(12, "program.c")));
        assertTrue(statements.contains(new StatementData(13, "program.c")));
        
        // First test
        List<TestData> tests = dummyData.getTests();
        TestData test = tests.get(0);
        statements = test.getStatementsExecuted();
        assertTrue(test.getPassed() == true);
        assertTrue(statements.size() == 7);
        assertTrue(statements.contains(new StatementData(6, "program.c")));
        assertTrue(statements.contains(new StatementData(8, "program.c")));
        assertTrue(statements.contains(new StatementData(9, "program.c")));
        assertTrue(statements.contains(new StatementData(10, "program.c")));
        assertTrue(statements.contains(new StatementData(11, "program.c")));
        assertTrue(statements.contains(new StatementData(12, "program.c")));
        assertTrue(statements.contains(new StatementData(13, "program.c")));

        // Second test
        test = tests.get(1);
        statements = test.getStatementsExecuted();
        assertTrue(test.getPassed() == true);
        assertTrue(statements.size() == 8);
        assertTrue(statements.contains(new StatementData(6, "program.c")));
        assertTrue(statements.contains(new StatementData(7, "program.c")));
        assertTrue(statements.contains(new StatementData(8, "program.c")));
        assertTrue(statements.contains(new StatementData(9, "program.c")));
        assertTrue(statements.contains(new StatementData(10, "program.c")));
        assertTrue(statements.contains(new StatementData(11, "program.c")));
        assertTrue(statements.contains(new StatementData(12, "program.c")));
        assertTrue(statements.contains(new StatementData(13, "program.c")));
        
        // Third test
        test = tests.get(2);
        statements = test.getStatementsExecuted();
        assertTrue(test.getPassed() == false);
        assertTrue(statements.size() == 13);
        assertTrue(statements.contains(new StatementData(1, "program.c")));
        assertTrue(statements.contains(new StatementData(2, "program.c")));
        assertTrue(statements.contains(new StatementData(3, "program.c")));
        assertTrue(statements.contains(new StatementData(4, "program.c")));
        assertTrue(statements.contains(new StatementData(5, "program.c")));
        assertTrue(statements.contains(new StatementData(6, "program.c")));
        assertTrue(statements.contains(new StatementData(7, "program.c")));
        assertTrue(statements.contains(new StatementData(8, "program.c")));
        assertTrue(statements.contains(new StatementData(9, "program.c")));
        assertTrue(statements.contains(new StatementData(10, "program.c")));
        assertTrue(statements.contains(new StatementData(11, "program.c")));
        assertTrue(statements.contains(new StatementData(12, "program.c")));
        assertTrue(statements.contains(new StatementData(13, "program.c")));
        
        // Fouth test
        test = tests.get(3);
        statements = test.getStatementsExecuted();
        assertTrue(test.getPassed() == true);
        assertTrue(statements.size() == 12);
        assertTrue(statements.contains(new StatementData(1, "program.c")));
        assertTrue(statements.contains(new StatementData(2, "program.c")));
        assertTrue(statements.contains(new StatementData(3, "program.c")));
        assertTrue(statements.contains(new StatementData(4, "program.c")));
        assertTrue(statements.contains(new StatementData(5, "program.c")));
        assertTrue(statements.contains(new StatementData(7, "program.c")));
        assertTrue(statements.contains(new StatementData(8, "program.c")));
        assertTrue(statements.contains(new StatementData(9, "program.c")));
        assertTrue(statements.contains(new StatementData(10, "program.c")));
        assertTrue(statements.contains(new StatementData(11, "program.c")));
        assertTrue(statements.contains(new StatementData(12, "program.c")));
        assertTrue(statements.contains(new StatementData(13, "program.c")));

        // Fifth test
        test = tests.get(4);
        statements = test.getStatementsExecuted();
        assertTrue(test.getPassed() == true);
        assertTrue(statements.size() == 12);
        assertTrue(statements.contains(new StatementData(1, "program.c")));
        assertTrue(statements.contains(new StatementData(2, "program.c")));
        assertTrue(statements.contains(new StatementData(3, "program.c")));
        assertTrue(statements.contains(new StatementData(4, "program.c")));
        assertTrue(statements.contains(new StatementData(5, "program.c")));
        assertTrue(statements.contains(new StatementData(7, "program.c")));
        assertTrue(statements.contains(new StatementData(8, "program.c")));
        assertTrue(statements.contains(new StatementData(9, "program.c")));
        assertTrue(statements.contains(new StatementData(10, "program.c")));
        assertTrue(statements.contains(new StatementData(11, "program.c")));
        assertTrue(statements.contains(new StatementData(12, "program.c")));
        assertTrue(statements.contains(new StatementData(13, "program.c")));
    }
}