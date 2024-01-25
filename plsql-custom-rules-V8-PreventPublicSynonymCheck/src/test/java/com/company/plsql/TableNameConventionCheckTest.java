package com.company.plsql;

//import org.junit.Test;
import org.sonar.plsqlopen.checks.verifier.PlSqlCheckVerifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.sonar.plugins.plsqlopen.api.checks.PlSqlCheck;
import static org.junit.jupiter.api.Assertions.fail;

//import org.junit.Test;
//import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;
import org.sonar.plsqlopen.checks.verifier.PlSqlCheckVerifier;

public class TableNameConventionCheckTest {

    @Test
    public void testTableNameConvention() {
        String testFilePath = "src/test/resources/InvalidTableNameConventions.sql";
        TableNameConventionCheck check = new TableNameConventionCheck();

        try {
            PlSqlCheckVerifier.Companion.verify(testFilePath, check);
        } catch (AssertionError e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
