package com.company.plsql;

import org.sonar.plsqlopen.checks.verifier.PlSqlCheckVerifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.sonar.plugins.plsqlopen.api.checks.PlSqlCheck;
import static org.junit.jupiter.api.Assertions.fail;


import org.junit.jupiter.api.Test;
//import org.sonar.plugins.plsqlopen.api.PlSqlCheck;
//import static org.junit.jupiter.api.Assertions.fail;

public class TableNameMaxLengthCheckTest {

    @Test
    public void testTableNameLengthCheck() {
        // Prepare the path to your test file and instantiate your custom check
        String testFilePath = "src/test/resources/TableNameLengthTest.sql";
        PlSqlCheck check = new TableNameMaxLengthCheck();

        try {
            // Call for the verify method, which will throw an AssertionError if it finds any discrepancies between actual and expected issues.
            PlSqlCheckVerifier.Companion.verify(testFilePath, check);
        } catch (AssertionError e) {
            // Print the stack trace for more debug information
            e.printStackTrace();
            // Fail the test with the error message
            fail(e.getMessage());
        }
    }
}
