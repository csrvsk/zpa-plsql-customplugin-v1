package com.company.plsql;

import org.sonar.plsqlopen.checks.verifier.PlSqlCheckVerifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.sonar.plugins.plsqlopen.api.checks.PlSqlCheck;
import java.util.logging.Logger;

public class SchemaValidationCheckTest {

    private static final Logger LOGGER = Logger.getLogger(SchemaValidationCheckTest.class.getName());

    @Test
    public void testSchemaValidationOnSqlFiles() {
        String testFilePath = "src/test/resources/SchemaValidationTest.sql"; // adjust the path if needed
        SchemaValidationCheck check = new SchemaValidationCheck();
        try {
            PlSqlCheckVerifier.Companion.verify(testFilePath, check);
        } catch (AssertionError e) {
            e.printStackTrace();
            LOGGER.info("Assertion error: " + e.getMessage());
            fail(e.getMessage());
        }
    }
}

/*public class SchemaValidationCheckTest {

    @Test
    public void testSchemaValidationOnSqlFiles() {
        String testFilePath = "src/test/resources/SchemaValidationTest.sql"; // adjust the path if needed
        SchemaValidationCheck check = new SchemaValidationCheck();
        try {
            PlSqlCheckVerifier.Companion.verify(testFilePath, check);
        } catch (AssertionError e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}*/
