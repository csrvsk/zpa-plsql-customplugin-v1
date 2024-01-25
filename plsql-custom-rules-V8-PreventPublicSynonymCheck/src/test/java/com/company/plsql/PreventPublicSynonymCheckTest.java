package com.company.plsql;

import org.sonar.plsqlopen.checks.verifier.PlSqlCheckVerifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.sonar.plugins.plsqlopen.api.checks.PlSqlCheck;
import static org.junit.jupiter.api.Assertions.fail;

public class PreventPublicSynonymCheckTest {

    @Test
    public void testPreventPublicSynonymCheck() {
        String testFilePath = "src/test/resources/PreventPublicSynonymTest.sql"; // Path to your test SQL file
        PreventPublicSynonymCheck check = new PreventPublicSynonymCheck();

        try {
            PlSqlCheckVerifier.Companion.verify(testFilePath, check);
        } catch (AssertionError e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
