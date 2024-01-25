package com.company.plsql;

import org.sonar.plsqlopen.checks.verifier.PlSqlCheckVerifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.sonar.plugins.plsqlopen.api.checks.PlSqlCheck;
import static org.junit.jupiter.api.Assertions.fail;

public class JSRJSDCheckTest {
    @Test
    public void testJSRJSDCheckOnTables() {
        String testFilePath = "src/test/resources/JSRJSDTableNames.sql"; // adjust the path if needed
        JSRJSDCheck check = new JSRJSDCheck();
        try {
            PlSqlCheckVerifier.Companion.verify(testFilePath, check);
        } catch (AssertionError e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
