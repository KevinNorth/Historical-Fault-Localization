package edu.unl.knorth.historical_fault_localization;

import edu.unl.knorth.historical_fault_localization.intermediate_data.StatementData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.TestData;
import edu.unl.knorth.historical_fault_localization.intermediate_data.TestExecutionData;
import java.util.ArrayList;
import java.util.List;

public class DummyData {

    public static TestExecutionData getDummyData() {
        List<StatementData> statements = new ArrayList<>();

        statements.add(new StatementData(1, "program.c"));
        statements.add(new StatementData(2, "program.c"));
        statements.add(new StatementData(3, "program.c"));
        statements.add(new StatementData(4, "program.c"));
        statements.add(new StatementData(5, "program.c"));
        statements.add(new StatementData(6, "program.c"));
        statements.add(new StatementData(7, "program.c"));
        statements.add(new StatementData(8, "program.c"));
        statements.add(new StatementData(9, "program.c"));
        statements.add(new StatementData(10, "program.c"));
        statements.add(new StatementData(11, "program.c"));
        statements.add(new StatementData(12, "program.c"));
        statements.add(new StatementData(13, "program.c"));

        List<TestData> tests = new ArrayList<>();

        tests.add(new TestData(true, statements.get(5), statements.get(7),
                statements.get(8), statements.get(9), statements.get(10),
                statements.get(11), statements.get(12)));
        tests.add(new TestData(true, statements.get(5), statements.get(6),
                statements.get(7), statements.get(8), statements.get(9),
                statements.get(10), statements.get(11), statements.get(12)));
        tests.add(new TestData(false, statements));
        tests.add(new TestData(true, statements.get(0), statements.get(1),
                statements.get(2), statements.get(3), statements.get(4),
                statements.get(6), statements.get(7), statements.get(8),
                statements.get(9), statements.get(10), statements.get(11),
                statements.get(12)));
        tests.add(new TestData(true, statements.get(0), statements.get(1),
                statements.get(2), statements.get(3), statements.get(4),
                statements.get(6), statements.get(7), statements.get(8),
                statements.get(9), statements.get(10), statements.get(11),
                statements.get(12)));
        
        TestExecutionData data = new TestExecutionData();
        data.addStatements(statements);
        data.addTests(tests);
        
        return data;
    }
}
