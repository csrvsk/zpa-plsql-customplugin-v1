package com.company.plsql;

//import org.junit.Test;
import org.sonar.plsqlopen.checks.verifier.PlSqlCheckVerifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.sonar.plugins.plsqlopen.api.checks.PlSqlCheck;


public class TableNamingCheckTest {

/*     @Test
    public void testTableNameFollowsConvention() {
        PlSqlCheckVerifier.Companion.verify("src/test/resources/ValidTableNames.sql", new TableNamingCheck());
    } */
	@Test
	public void testTableNameFollowsConvention() {
		// Prepare the path to your test file and instantiate your custom check
		String testFilePath = "src/test/resources/ValidTableNames.sql";
		TableNamingCheck check = new TableNamingCheck();
		
		try {
			// Call the verify method, which will throw an AssertionError if it finds any discrepancies between actual and expected issues.
			PlSqlCheckVerifier.Companion.verify(testFilePath, check);
		} catch (AssertionError e) {
			// Print the stack trace for more debug information
			e.printStackTrace();
			// Fail the test with the error message
			fail(e.getMessage());
		}
	}

	@Test
	public void testTableNameDoesNotFollowConvention() {
		// Prepare the path to your test file and instantiate your custom check
		String testFilePath = "src/test/resources/InvalidTableNames.sql";
		TableNamingCheck check = new TableNamingCheck();
		
		try {
			// Call the verify method, which will throw an AssertionError if it finds any discrepancies between actual and expected issues.
			PlSqlCheckVerifier.Companion.verify(testFilePath, check);
		} catch (AssertionError e) {
			// Print the stack trace for more debug information
			e.printStackTrace();
			// Fail the test with the error message
			fail(e.getMessage());
		}
	}
}



/*     @Test
    public void testTableNameFollowsConvention() {
        PlSqlCheck check = new TableNamingCheck();
        String fileName = "src/test/resources/Table-naming-example.sql";
        PlSqlCheckVerifier.verify(fileName, check);
    }

    @Test
    public void testTableNameDoesNotFollowConvention() {
        PlSqlCheck check = new TableNamingCheck();
        String fileName = "src/test/resources/Table-naming-example.sql";
        PlSqlCheckVerifier.verify(fileName, check);
    } */
