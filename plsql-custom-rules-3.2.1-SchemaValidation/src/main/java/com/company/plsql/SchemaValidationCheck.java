package com.company.plsql;

import org.sonar.plugins.plsqlopen.api.DdlGrammar;
import org.sonar.plugins.plsqlopen.api.SqlPlusGrammar;
import org.sonar.plugins.plsqlopen.api.annotations.Priority;
import org.sonar.plugins.plsqlopen.api.annotations.Rule;
import org.sonar.plugins.plsqlopen.api.annotations.ActivatedByDefault;
import org.sonar.plugins.plsqlopen.api.annotations.ConstantRemediation;
import org.sonar.plugins.plsqlopen.api.checks.PlSqlCheck;
import org.sonar.plugins.plsqlopen.api.sslr.AstNode;
import org.sonar.plugins.plsqlopen.api.PlSqlGrammar;
import java.util.logging.Logger;
import java.util.List;
import org.sonar.plugins.plsqlopen.api.PlSqlFile;
import org.sonar.plsqlopen.squid.SonarQubePlSqlFile;
import org.sonar.plugins.plsqlopen.api.PlSqlVisitorContext;
import org.sonar.plugins.plsqlopen.api.squid.SemanticAstNode;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

//import org.sonar.plugins.plsqlopen.api.checks.CheckContext;
import org.sonar.plugins.plsqlopen.api.symbols.Scope;
import org.sonar.plugins.plsqlopen.api.symbols.Symbol;

@Rule(
    key = "SchemaValidationCheck",
    name = "CMiC - Schema Validation for create table",
    description = "All SQL operations should match the schema defined in the SQL file. This ensures that operations are performed in the expected schema context. "
        + "Mismatched schemas can cause unexpected behavior and potential data corruption. "
        + "Always ensure that your SQL operations match the defined schema. <br><br>"
        + "<b>Non-Compliance:</b> Flag any SQL operation that doesn't match the defined schema.<br><br>"
        + "<b>Reference Document:</b> <a href='https://cmicglobal.sharepoint.com/:w:/t/development/EZE9BhqD2I5CiguVLLnNo-kB24zU-PP1HKhh_i-HabFKHA?e=5b4YrL'>CMiC_SQL_Object_Standards.docx</a>",
    priority = Priority.MAJOR
)
@ActivatedByDefault
@ConstantRemediation("5min")

public class SchemaValidationCheck extends PlSqlCheck {

    private static final Logger LOGGER = Logger.getLogger(SchemaValidationCheck.class.getName());

    // List of approved schemas
    private final Set<String> approvedSchemas = new HashSet<>(Arrays.asList(
        "DA", "UIG", "DAR", "JSR", "JSD", "CMIC_BI", "CMIC_BI_RUNTIME", "CMIC_REPORT_VIEWS",
        "CMIC_REPORT_WRITER", "CMIC_USER_DEFINED_VIEWS", "OWF_MGR", "CMIC_FK_IDX", "CMIC_GCS",
        "CMIC_AUDIT_DA", "CMIC_GENERIC_AUDIT1", "CMIC_GENERIC_AUDIT2"
    ));

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        // Add other DDL grammar nodes as needed
    }

    @Override
    public void visitNode(AstNode node) {
        if (node.is(DdlGrammar.CREATE_TABLE)) {
            handleTableCreation(node);
        }
        // Handle other DDL operations similarly as needed
    }

    private void handleTableCreation(AstNode node) {
        LOGGER.info("Starting handleTableCreation for node: " + node);

        AstNode tableNameNode = node.getFirstDescendant(PlSqlGrammar.UNIT_NAME);

        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a descendant of type UNIT_NAME");
            return;
        }

        List<AstNode> tableNameChildren = tableNameNode.getChildren();
        String schemaName = null;

        if (tableNameChildren.size() > 1) {
            // We expect this to be schema.table structure
            schemaName = tableNameChildren.get(0).getTokenOriginalValue().toUpperCase();
            LOGGER.info("Schema name derived from tableNameChildren: " + schemaName);
        } else {
            LOGGER.info("tableNameChildren does not contain schema and table structure.");
        }

        LOGGER.info("Evaluating for schema validation at line " + tableNameNode.getTokenLine());
        LOGGER.info("tableNameNode: " + (tableNameNode != null ? tableNameNode.getTokenOriginalValue() : "null"));
        LOGGER.info("Schema from tableNameNode: " + (schemaName != null ? schemaName : "none provided"));

        if (schemaName == null) {
            String message = String.format("No schema is being used here at Line #%d.", tableNameNode.getTokenLine());
            LOGGER.info("Issue added with message: " + message);
            addIssue(tableNameNode, message);
        } else if (!approvedSchemas.contains(schemaName.toUpperCase())) {
            String message = String.format("Schema '%s' does not match the approved schemas at Line #%d.", schemaName, tableNameNode.getTokenLine());
            LOGGER.info("Issue added with message: " + message);
            addIssue(tableNameNode, message);
        } else {
            LOGGER.info("Schema matches approved schema. No issues added.");
        }

        LOGGER.info("Finished handleTableCreation for node: " + node);
    }
}

/*public class SchemaValidationCheck extends PlSqlCheck {

    private static final Logger LOGGER = Logger.getLogger(SchemaValidationCheck.class.getName());

    // List of approved schemas
    private final Set<String> approvedSchemas = new HashSet<>(Arrays.asList(
        "DA", "UIG", "DAR", "JSR", "JSD", "CMIC_BI", "CMIC_BI_RUNTIME", "CMIC_REPORT_VIEWS",
        "CMIC_REPORT_WRITER", "CMIC_USER_DEFINED_VIEWS", "OWF_MGR", "CMIC_FK_IDX", "CMIC_GCS",
        "CMIC_AUDIT_DA", "CMIC_GENERIC_AUDIT1", "CMIC_GENERIC_AUDIT2"
    ));

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        // Add other DDL grammar nodes as needed
    }

    @Override
    public void visitNode(AstNode node) {
        if (node.is(DdlGrammar.CREATE_TABLE)) {
            handleTableCreation(node);
        }
        // Handle other DDL operations similarly as needed
    }

    private void handleTableCreation(AstNode node) {
        LOGGER.info("Starting handleTableCreation for node: " + node);

        AstNode tableNameNode = node.getFirstDescendant(PlSqlGrammar.UNIT_NAME);

        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a descendant of type UNIT_NAME");
            return;
        }

        List<AstNode> tableNameChildren = tableNameNode.getChildren();
        String schemaName = null;

        if (tableNameChildren.size() > 1) {
            // We expect this to be schema.table structure
            schemaName = tableNameChildren.get(0).getTokenOriginalValue().toUpperCase();
            LOGGER.info("Schema name derived from tableNameChildren: " + schemaName);
        } else {
            LOGGER.info("tableNameChildren does not contain schema and table structure.");
        }

        int lineNumber = tableNameNode.getTokenLine();
        LOGGER.info("Evaluating for schema validation at line " + lineNumber);
        LOGGER.info("tableNameNode: " + (tableNameNode != null ? tableNameNode.getTokenOriginalValue() : "null"));
        LOGGER.info("Schema from tableNameNode: " + (schemaName != null ? schemaName : "none provided"));

        //String generatedMessage = generateMessageBasedOnConditions(tableNameNode);
        String generatedMessage = generateMessageBasedOnConditions(tableNameNode.getTokenOriginalValue());

        String expectedMessage = retrieveExpectedMessageFromSQLFile(lineNumber);

        LOGGER.info("Generated Message: " + generatedMessage);
        LOGGER.info("Expected Message from SQL File: " + expectedMessage);

        if (schemaName == null) {
            String message = String.format("No schema is being used here at Line #%d.", tableNameNode.getTokenLine());
            LOGGER.info("Issue added with message: " + message);
            addIssue(tableNameNode, message);
        } else if (!approvedSchemas.contains(schemaName.toUpperCase())) {
            String message = String.format("The schema '%s' does not match the approved schemas at Line #%d.", schemaName, tableNameNode.getTokenLine());
            LOGGER.info("Issue added with message: " + message);
            addIssue(tableNameNode, message);
            The schema 'DB' does not match the approved schemas at Line
        } else {
            LOGGER.info("Schema matches approved schema. No issues added.");
        }

        LOGGER.info("Finished handleTableCreation for node: " + node);
    }

    private String generateMessageBasedOnConditions(String tableNameNode) {
        if ("DB".equals(tableNameNode)) {
            return "The schema 'DB' does not match the approved schemas.";
        }
        // ... Other conditions ...
        if ("none provided".equals(tableNameNode)) {
            return "No schema is being used here.";
        }
        return "Default message or an empty string if none matches"; // replace with your actual default message
    }

    private String retrieveExpectedMessageFromSQLFile(int lineNumber) {
        // Logic to read the SQL test file based on the given line number and extract the expected message
        // For now, I'm returning a placeholder, but this method needs to be implemented based on your test SQL file structure.
        return "Placeholder expected message from SQL file for line " + lineNumber;
    }
}*/
/*
Here is a list of basic SQL Operations in general - We might have to include them in this rule for future.
    private static final List<String> SQL_OPERATIONS = Arrays.asList(
        "CREATE TABLE", "ALTER TABLE", "DROP TABLE",
        "CREATE INDEX", "ALTER INDEX", "DROP INDEX",
        "CREATE VIEW", "ALTER VIEW", "DROP VIEW",
        "CREATE PROCEDURE", "ALTER PROCEDURE", "DROP PROCEDURE",
        "CREATE FUNCTION", "ALTER FUNCTION", "DROP FUNCTION",
        "CREATE TRIGGER", "ALTER TRIGGER", "DROP TRIGGER",
        "CREATE SEQUENCE", "ALTER SEQUENCE", "DROP SEQUENCE",
        "CREATE SYNONYM", "DROP SYNONYM",
        "CREATE PACKAGE", "ALTER PACKAGE", "DROP PACKAGE",
        "CREATE TYPE", "ALTER TYPE", "DROP TYPE"
    );
*/

/*public class SchemaValidationCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(SchemaValidationCheck.class.getName());

    private String definedSchema;

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        subscribeTo(SqlPlusGrammar.SQLPLUS_COMMAND); // Assuming SQLPLUS_COMMAND is the correct node name. Adjust if not.
        LOGGER.info("Subscribed to CREATE_TABLE and SQLPLUS_COMMAND nodes for Schema Validation Check");
    }

    @Override
    public void visitNode(AstNode node) {
        if (node.is(SqlPlusGrammar.SQLPLUS_COMMAND)) {
            handleSqlPlusCommand(node);
        } else if (node.is(DdlGrammar.CREATE_TABLE)) {
            handleCreateTable(node);
        }
    }

    private void handleSqlPlusCommand(AstNode node) {
        LOGGER.info("Handling SQLPLUS_COMMAND node");

        if ("define".equalsIgnoreCase(node.getFirstChild().getTokenOriginalValue())) {
            List<AstNode> children = node.getChildren();

            if (children.size() >= 4) {
                AstNode variableNameNode = children.get(1);
                AstNode variableValueNode = children.get(3);

                String variableName = variableNameNode.getTokenOriginalValue();
                String variableValue = variableValueNode.getTokenOriginalValue().toUpperCase();

                if (definedSchema == null) {
                    definedSchema = variableValue;
                    LOGGER.info("Defined schema found using variable '" + variableName + "': " + definedSchema);
                }
            }
        }
    }

    private void handleCreateTable(AstNode node) {
        AstNode tableNameNode = node.getFirstDescendant(PlSqlGrammar.UNIT_NAME);

        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a descendant of type UNIT_NAME");
            return;
        }

        List<AstNode> tableNameChildren = tableNameNode.getChildren();
        String schemaName = null;

        if (tableNameChildren.size() > 1) {
            // We expect this to be schema.table structure
            schemaName = tableNameChildren.get(0).getTokenOriginalValue().toUpperCase();
        }

        int lineNumber = tableNameNode.getTokenLine();
        LOGGER.info("Evaluating for schema validation at line " + lineNumber);
        LOGGER.info("tableNameNode: " + (tableNameNode != null ? tableNameNode.getTokenOriginalValue() : "null"));
        LOGGER.info("Schema from tableNameNode: " + (schemaName != null ? schemaName : "none provided"));
        LOGGER.info("Expected schema: " + definedSchema);

        if (schemaName == null) {
            String message = String.format("Missing expected schema '%s' at Line #%d", definedSchema, lineNumber);
            LOGGER.info("Generated Message: " + message);
            addIssue(tableNameNode, message);
        } else {
            // If the name of the schema doesn't match the defined schema, report an issue
            if (!definedSchema.equalsIgnoreCase(schemaName)) {
                String message = String.format("The schema '%s' does not match the defined schema '%s' at Line #%d", schemaName, definedSchema, lineNumber);
                LOGGER.info("Generated Message: " + message);
                addIssue(tableNameNode, message);
            }
        }
    }
}*/

/*  This is working but - adding enhanced logging for investigation of error during test.
    private void handleCreateTable(AstNode node) {
        AstNode tableNameNode = node.getFirstDescendant(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a descendant of type UNIT_NAME");
            return;
        }

        List<AstNode> tableNameChildren = tableNameNode.getChildren();
        String schemaName = null;

        if (tableNameChildren.size() > 1) {
            // We expect this to be schema.table structure
            schemaName = tableNameChildren.get(0).getTokenOriginalValue().toUpperCase();
        }

        int lineNumber = tableNameNode.getTokenLine();
        LOGGER.info("Evaluating for schema validation at line " + lineNumber + ". Schema: " + (schemaName != null ? schemaName : "none provided"));

        if (schemaName == null) {
            String message = String.format("Missing expected schema '%s' at Line #%d", definedSchema, lineNumber);
            addIssue(tableNameNode, message);
        } else {
            // If the name of the schema doesn't match the defined schema, report an issue
            if (!definedSchema.equalsIgnoreCase(schemaName)) {
                String message = String.format("The schema '%s' does not match the defined schema '%s' at Line #%d", schemaName, definedSchema, lineNumber);
                addIssue(tableNameNode, message);
            }
        }
    }
}*/
/*
    private void handleCreateTable(AstNode node) {
        AstNode tableNameNode = node.getFirstDescendant(PlSqlGrammar.UNIT_NAME);

        // Logging tableNameNode value
        LOGGER.info("tableNameNode: " + (tableNameNode != null ? tableNameNode.getTokenValue() : "null"));

        // Extract the potential schema node, which is the node before the table name node
        AstNode potentialSchemaNode = tableNameNode.getPreviousSibling();

        // Logging potentialSchemaNode value
        LOGGER.info("potentialSchemaNode: " + (potentialSchemaNode != null ? potentialSchemaNode.getTokenValue() : "null"));

        // If there is no schema node before the table name node, report an issue
        if (potentialSchemaNode == null) {
            String message = String.format("Missing expected schema '%s' at Line #%d", definedSchema, tableNameNode.getTokenLine());
            // Logging the message
            LOGGER.info("Generated Message: " + message);
            addIssue(tableNameNode, message);
        } else {
            // If the name of the potential schema node doesn't match the defined schema, report an issue
            String schemaName = potentialSchemaNode.getTokenValue();
            String message = String.format("The schema '%s' does not match the defined schema '%s' at Line #%d", schemaName, definedSchema, tableNameNode.getTokenLine());
            // Logging the message
            LOGGER.info("Generated Message: " + message);
            addIssue(potentialSchemaNode, message);
        }
    }
}*/
/*    private void handleCreateTable(AstNode node) {
        LOGGER.info("Visiting a CREATE_TABLE node for Schema Validation Check");

        // Extract table name node, which is immediate child
        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);

            if (tableNameNode.hasDirectChildren(PlSqlGrammar.IDENTIFIER_NAME)) {
            AstNode potentialSchemaNode = tableNameNode.getFirstChild(PlSqlGrammar.IDENTIFIER_NAME);

            // Check for a dot before the potential schema node
            if (potentialSchemaNode.getPreviousSibling() != null && ".".equals(potentialSchemaNode.getPreviousSibling().getTokenOriginalValue())) {
                // No schema specified, just a table name
                //String message = "Schema not specified! Expected schema '" + definedSchema + "' at Line #" + potentialSchemaNode.getTokenLine();
                String message = "Missing expected schema '" + definedSchema + "' at Line #" + potentialSchemaNode.getTokenLine();
                addIssue(potentialSchemaNode, message);
                LOGGER.warning(message);
            } else {
                String actualSchema = potentialSchemaNode.getTokenOriginalValue();
                if (!definedSchema.equals(actualSchema)) {
                    String message = "The schema '" + actualSchema + "' does not match the defined schema '" + definedSchema + "' at Line #" + potentialSchemaNode.getTokenLine();
                    addIssue(potentialSchemaNode, message);
                    LOGGER.warning(message);
                }
            }
        }
    }*/

/*        // Check if there's a schema
        if (tableNameNode.hasDirectChildren(PlSqlGrammar.IDENTIFIER_NAME)) {
            AstNode schemaNode = tableNameNode.getFirstChild(PlSqlGrammar.IDENTIFIER_NAME);
            String actualSchema = schemaNode.getTokenOriginalValue();

            if (!definedSchema.equals(actualSchema)) {
                String message = "The schema '" + actualSchema + "' does not match the defined schema '" + definedSchema + "' at Line #" + schemaNode.getTokenLine();
                LOGGER.info("Reported Issue: " + message);
                addIssue(schemaNode, message);
                LOGGER.warning(message);
            }
        } else {
            String message = "Missing expected schema '" + definedSchema + "' at Line #" + tableNameNode.getTokenLine();
            LOGGER.info("Reported Issue: " + message);
            addIssue(tableNameNode, message);
            LOGGER.warning(message);
        }
    }*/
/* following block is perfectly detecting the schema mismatch. But it doesn't have mechanism to check if there is no schema.
        private void handleCreateTable(AstNode node) {
        LOGGER.info("Visiting a CREATE_TABLE node for Schema Validation Check");

        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME for Schema Validation Check");
            return;
        }

        AstNode schemaNode = tableNameNode.getFirstChild(PlSqlGrammar.IDENTIFIER_NAME);
        String schemaName = schemaNode != null ? schemaNode.getTokenOriginalValue().toUpperCase() : null;

        if (schemaName != null && !schemaName.equals(definedSchema)) {
            String message = String.format("The schema '%s' does not match the defined schema '%s' at Line #%d", schemaName, definedSchema, node.getTokenLine());
            //String message = String.format("The schema '%s' does not match the defined schema '%s' at Line #%d", schemaName, definedSchema);
            LOGGER.info("Reported Issue: " + message);
            addIssue(tableNameNode, message);
            //addIssue(node, message);
            LOGGER.warning(message);
        }
    }*/

/*
public class SchemaValidationCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(SchemaValidationCheck.class.getName());
    private String definedSchema;
    private boolean fileProcessed = false; // To ensure the file is processed only once

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        LOGGER.info("Subscribed to CREATE_TABLE nodes for Schema Validation Check");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a node for Schema Validation Check");

        // Process the file content only once per file
        if (!fileProcessed) {
            PlSqlVisitorContext context = getContext();
            if (context.rootNode() instanceof SonarQubePlSqlFile) {
                SonarQubePlSqlFile sonarFile = (SonarQubePlSqlFile) context.rootNode();
                String fileContent = sonarFile.content();
                LOGGER.info("Contents of the current file being processed: \n" + fileContent);
                preprocessSqlFileContent(fileContent);
                LOGGER.info("Defined Schema after preprocessing: " + definedSchema);
            }
            fileProcessed = true;
        }

        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME for Schema Validation Check");
            return;
        }

        AstNode schemaNode = tableNameNode.getFirstChild(PlSqlGrammar.IDENTIFIER_NAME);
        String schemaName = schemaNode != null ? schemaNode.getTokenOriginalValue().toUpperCase() : null;

        if (schemaName != null && !schemaName.equals(definedSchema)) {
            String message = String.format("The schema '%s' does not match the defined schema '%s' at Line #%d", schemaName, definedSchema, node.getTokenLine());
            addIssue(node, message);
            LOGGER.warning(message);
        } else {
            LOGGER.info("Schema matches or schema not found. Skipping...");
        }
    }

    private void preprocessSqlFileContent(String content) {
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("define")) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    definedSchema = parts[1].trim().toUpperCase();
                    LOGGER.info("Found a DEFINE command: " + line + " | Extracted Schema: " + definedSchema);
                    break;
                }
            }
        }
    }
}
*/

/*public class SchemaValidationCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(SchemaValidationCheck.class.getName());

    private String definedSchema;

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        LOGGER.info("Subscribed to CREATE_TABLE nodes for Schema Validation Check");
    }

    @Override
    public void visitFile(AstNode ast) {
        if (ast != null) {
            logAstNode(ast, 0); // starting with root node at level 0
        }
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a CREATE_TABLE node for Schema Validation Check");

        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME for Schema Validation Check");
            return;
        }

        AstNode schemaNode = tableNameNode.getFirstChild(PlSqlGrammar.IDENTIFIER_NAME);
        String schemaName = schemaNode != null ? schemaNode.getTokenOriginalValue().toUpperCase() : null;

        if (schemaName != null && !schemaName.equals(definedSchema)) {
            String message = String.format("The schema '%s' does not match the defined schema '%s' at Line #%d", schemaName, definedSchema, node.getTokenLine());
            addIssue(node, message);
            LOGGER.warning(message);
        }
    }

    private void preprocessSqlFileContent(String content) {
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("define")) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    definedSchema = parts[1].trim().toUpperCase();
                    break;
                }
            }
        }
    }

    private void logAstNode(AstNode node, int level) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indent.append("--");
        }

        LOGGER.info(indent + "> " + node.getName() + " (" + node.getTokenOriginalValue() + ")");
        List<AstNode> children = node.getChildren();
        for (AstNode child : children) {
            logAstNode(child, level + 1);
        }
    }
}*/


/*
public class SchemaValidationCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(SchemaValidationCheck.class.getName());

    private String definedSchema;

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        LOGGER.info("Subscribed to CREATE_TABLE nodes for Schema Validation Check");
    }

    // Removing the visitFile method as it's not feasible with the current plugin structure.

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a CREATE_TABLE node for Schema Validation Check");

        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME for Schema Validation Check");
            return;
        }

        AstNode schemaNode = tableNameNode.getFirstChild(PlSqlGrammar.IDENTIFIER_NAME);
        String schemaName = schemaNode != null ? schemaNode.getTokenOriginalValue().toUpperCase() : null;

        if (schemaName != null && !schemaName.equals(definedSchema)) {
            String message = String.format("The schema '%s' does not match the defined schema '%s' at Line #%d", schemaName, definedSchema, node.getTokenLine());
            addIssue(node, message);
            LOGGER.warning(message);
        }
    }

    private void preprocessSqlFileContent(String content) {
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("define")) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    definedSchema = parts[1].trim().toUpperCase();
                    break;
                }
            }
        }
    }
}
*/

/*public class SchemaValidationCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(SchemaValidationCheck.class.getName());

    //private String definedSchema = null;
    private String definedSchema;


    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        LOGGER.info("Subscribed to CREATE_TABLE nodes for Schema Validation Check");
    }

    @Override
    public void visitFile(PlSqlVisitorContext context) {
        if(context.rootNode() instanceof SonarQubePlSqlFile) {
            SonarQubePlSqlFile sonarFile = (SonarQubePlSqlFile) context.rootNode();
            String content = sonarFile.toString(); // Might need adjustments based on actual method.
            preprocessSqlFileContent(content);
        }
    }

        //preprocessSqlFileContent(getContext().fileContent());
        //preprocessSqlFileContent(((SonarQubePlSqlFile) getContext().file()).content());

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a CREATE_TABLE node for Schema Validation Check");

        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME for Schema Validation Check");
            return;
        }

        AstNode schemaNode = tableNameNode.getFirstChild(PlSqlGrammar.IDENTIFIER_NAME);
        String schemaName = schemaNode != null ? schemaNode.getTokenOriginalValue().toUpperCase() : null;

        if (schemaName != null && !schemaName.equals(definedSchema)) {
            String message = String.format("The schema '%s' does not match the defined schema '%s' at Line #%d", schemaName, definedSchema, node.getTokenLine());
            addIssue(node, message);
            LOGGER.warning(message);
        }
    }

    private void preprocessSqlFileContent(String content) {
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("define")) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    definedSchema = parts[1].trim().toUpperCase();
                    break;
                }
            }
        }
    }
}*/
/*    private void preprocessSqlFileContent(String content) {
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("define")) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    definedSchema = parts[1].trim().toUpperCase();
                    break;
                }
            }
        }
    }
}*/
/*
public class SchemaValidationCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(SchemaValidationCheck.class.getName());
    private String definedSchema;

    @Override
    public void init() {
        subscribeTo(SqlPlusGrammar.Companion.getDEFINE_DIRECTIVE()); // Subscribe to DEFINE directive
        subscribeTo(DdlGrammar.CREATE_TABLE, DdlGrammar.ALTER_TABLE, PlSqlGrammar.Companion.getINSERT_STATEMENT());
        LOGGER.info("Subscribed to relevant nodes for schema validation");
    }

    @Override
    public void visitNode(AstNode node) {
        if (node.is(SqlPlusGrammar.Companion.getDEFINE_DIRECTIVE())) {
            AstNode identifier = node.getFirstChild(SqlPlusGrammar.Companion.getIDENTIFIER());
            AstNode value = node.getFirstChild(SqlPlusGrammar.Companion.getCHARACTER_LITERAL());
            if (identifier != null && value != null) {
                definedSchema = value.getTokenOriginalValue().toUpperCase();
                LOGGER.info("Found DEFINE for schema: " + definedSchema);
            }
        }
        if (definedSchema == null) {
            LOGGER.warning("DEFINE statement not found in the SQL file.");
        }
    }

    @Override
    public void visitNode(AstNode node) {
        if (definedSchema == null) {
            return;  // Skip if no schema is defined
        }

        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME");
            return;
        }

        List<AstNode> tableNameChildren = tableNameNode.getChildren();
        String actualSchemaName;
        if (tableNameChildren.size() > 1) {
            actualSchemaName = tableNameChildren.get(0).getTokenOriginalValue().toUpperCase();
            if (!definedSchema.equals(actualSchemaName)) {
                LOGGER.warning("Schema mismatch! Defined schema: " + definedSchema + ", Actual schema: " + actualSchemaName);
                addIssue(tableNameNode.getToken(), "Schema mismatch! Defined schema: " + definedSchema + ", Actual schema: " + actualSchemaName);
            }
        } else {
            LOGGER.warning("Schema not provided in table operation. Expected schema: " + definedSchema);
        }
    }

    private String extractSchema(String line) {
        String[] parts = line.split("=");
        if (parts.length > 1) {
            return parts[1].trim().toUpperCase();
        }
        return "";
    }
}*/
