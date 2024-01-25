package com.company.plsql;

import org.sonar.plsqlopen.checks.verifier.PlSqlCheckVerifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.sonar.plugins.plsqlopen.api.checks.PlSqlCheck;
import static org.junit.jupiter.api.Assertions.fail;

public class ColumnDataTypeCheckTest {

    @Test
    public void shouldIdentifySpecialityAndUnapprovedDataTypes() {
        String testFilePath = "src/test/resources/ColumnDataTypes.sql"; // adjust the path if needed
        ColumnDATATYPECheck check = new ColumnDATATYPECheck();

        // Use the PlSqlCheckVerifier to verify the output of the ColumnDATATYPECheck
        PlSqlCheckVerifier.Companion.verify(testFilePath, check);
    }
}

/*public class ColumnDataTypeCheckTest {

    @Test
    public void testColumnDataTypeCheckOnColumns() {
        String testFilePath = "src/test/resources/ColumnDataTypes.sql"; // adjust the path if needed
        ColumnDATATYPECheck check = new ColumnDATATYPECheck();
        //ColumnDATATYPECheck check = new ColumnDataTypeCheck();
        try {
            PlSqlCheckVerifier.Companion.verify(testFilePath, check);
        } catch (AssertionError e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}*/





