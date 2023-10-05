package com.company.plsql;

import org.sonar.plugins.plsqlopen.api.DdlGrammar;
import org.sonar.plugins.plsqlopen.api.annotations.Priority;
import org.sonar.plugins.plsqlopen.api.annotations.Rule;
import org.sonar.plugins.plsqlopen.api.annotations.ActivatedByDefault;
import org.sonar.plugins.plsqlopen.api.annotations.ConstantRemediation;
import org.sonar.plugins.plsqlopen.api.checks.PlSqlCheck;
import org.sonar.plugins.plsqlopen.api.sslr.AstNode;
import org.sonar.plugins.plsqlopen.api.PlSqlGrammar;

import java.util.regex.Pattern;
import java.util.logging.Logger;
// Add this import for List
import java.util.List;



@Rule(
    name = "CMiC - Table Naming Conventions",
    description = "Ensure tables follow the defined naming conventions. <br><br>"
        + "<b>Maximum Length:</b> Length of the table name must not exceed 30 characters. <br><br>"
        + "<b>Start Format:</b> Must start with short abbreviation of 2 or 3 uppercase letters/numbers followed by an underscore `_`. <br><br>"
        + "<b>Invalid Prefixes:</b> Must not start with `JSR` or `JSD`. <br><br>"
        + "<b>End Format:</b> Optional endings: `_TN`, `_TABLE_TN`, `_TABLE`. <br><br>"
        + "<b>Exceptions:</b> Allow names ending with `_GTT`, `_MV`, `_TEMP`, `_TMP` and starting with `TMP_`. <br><br>"
        + "<b>Non-Compliance:</b> Flag any deviation from the above points.",
    key = "TableNamingCheck",
    priority = Priority.MAJOR
)
@ConstantRemediation("10min")
@ActivatedByDefault

public class TableNamingCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(TableNamingCheck.class.getName());
    private static final Pattern TABLE_PATTERN = Pattern.compile("^([A-Z0-9]{2,3})_([A-Z0-9_]+)(_TN|_TABLE_TN|_TABLE)?$");

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        LOGGER.info("Subscribed to CREATE_TABLE nodes");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a CREATE_TABLE node");

        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME");
            return;
        }

        String tableName = tableNameNode.getTokenOriginalValue().toUpperCase();
        LOGGER.info("Evaluating table name: " + tableName);

        // Strict rule check: Names starting with JSR or JSD are strictly prohibited
        if (tableName.startsWith("JSR") || tableName.startsWith("JSD")) {
            LOGGER.warning("Table names should not start with JSR or JSD: " + tableName);
            addIssue(tableNameNode.getToken(), "Table names should not start with JSR or JSD.");
            //addPreciseIssue(tableNameNode, "Table names should not start with JSR or JSD.");
            // addIssue is replaced by addPreciseIssue, node replaced by tableNameNode
            return; // Return early as this violation is non-negotiable
        }

        int maxLength = 30; // Define the maximum allowed length for a table name
        if (tableName.length() > maxLength) {
            LOGGER.warning("Table name exceeds the maximum allowed length: " + tableName);
            addIssue(tableNameNode.getToken(), "Table name exceeds the maximum allowed length.");
            //addIssue(node, "Table name exceeds the maximum allowed length.");
            // No return here; Continue checking other conditions
        }

        if (!TABLE_PATTERN.matcher(tableName).matches() && !isException(tableName)) {
            LOGGER.warning("Table name does not follow the naming conventions: " + tableName);
            addIssue(tableNameNode.getToken(), "Table name does not follow the naming conventions.");
            //addIssue(node, "Table name does not follow the naming conventions.");
        } else {
            LOGGER.info("Table name follows the naming conventions: " + tableName);
        }
    }

    private boolean isException(String tableName) {
        return tableName.endsWith("_GTT") || tableName.endsWith("_MV") || tableName.startsWith("TMP_") || tableName.endsWith("_TEMP") || tableName.endsWith("_TMP");
    }
}

/*
public class TableNamingCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(TableNamingCheck.class.getName());
    private static final Pattern TABLE_PATTERN = Pattern.compile("^([A-Z0-9]{2,3})_([A-Z0-9_]+)(_TN|_TABLE_TN|_TABLE)?$");

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        LOGGER.info("Subscribed to CREATE_TABLE nodes");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a CREATE_TABLE node");

        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME");
            return;
        }
        AstNode problematicToken = node.getFirstChild(DdlGrammar.TABLE_NAME);
        if (problematicToken != null && problematicToken.getTokenOriginalValue().startsWith("JSR")) {
            addPreciseIssue(problematicToken, "Table names should not start with JSR or JSD.");
        }

        String tableName = tableNameNode.getTokenOriginalValue().toUpperCase();
        LOGGER.info("Evaluating table name: " + tableName);

        // Strict rule check: Names starting with JSR or JSD are strictly prohibited
        if (tableName.startsWith("JSR") || tableName.startsWith("JSD")) {
            LOGGER.warning("Table names should not start with JSR or JSD: " + tableName);
            addIssue(node, "Table names should not start with JSR or JSD.");
            return; // Return early as this violation is non-negotiable
        }


        int maxLength = 25; // Define the maximum allowed length for a table name
        if (tableName.length() > maxLength) {
            LOGGER.warning("Table name exceeds the maximum allowed length: " + tableName);
            addIssue(node, "Table name exceeds the maximum allowed length.");
            // No return here; Continue checking other conditions
        }

        if (!TABLE_PATTERN.matcher(tableName).matches() && !isException(tableName)) {
            LOGGER.warning("Table name does not follow the naming conventions: " + tableName);
            addIssue(node, "Table name does not follow the naming conventions.");
        } else {
            LOGGER.info("Table name follows the naming conventions: " + tableName);
        }
    }
    private boolean isException(String tableName) {
        return tableName.endsWith("_GTT") || tableName.endsWith("_MV") || tableName.startsWith("TMP_") || tableName.endsWith("_TEMP") || tableName.endsWith("_TMP");
    }
}*/

/*
    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a CREATE_TABLE node");

        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME");
            return;
        }

        String tableName = tableNameNode.getTokenOriginalValue().toUpperCase();
        LOGGER.info("Evaluating table name: " + tableName);

        int maxLength = 25; // Define the maximum allowed length for a table name
        if (tableName.length() > maxLength) {
            LOGGER.warning("Table name exceeds the maximum allowed length: " + tableName);
            addIssue(node, "Table name exceeds the maximum allowed length.");
            return;
        }

        if (TABLE_PATTERN.matcher(tableName).matches()) {
            if (isException(tableName)) {
                LOGGER.info("Table name is an exception and follows the naming conventions: " + tableName);
            } else {
                if (tableName.startsWith("JSR") || tableName.startsWith("JSD")) {
                    LOGGER.warning("Table names should not start with JSR or JSD: " + tableName);
                    addIssue(node, "Table names should not start with JSR or JSD.");
                } else {
                    LOGGER.info("Table name follows the naming conventions: " + tableName);
                }
            }
        } else {
            LOGGER.warning("Table name does not follow the naming conventions: " + tableName);
            addIssue(node, "Table name does not follow the naming conventions.");
        }
    }

    private boolean isException(String tableName) {
        return tableName.endsWith("_GTT") || tableName.endsWith("_MV") || tableName.startsWith("TMP_") || tableName.endsWith("_TEMP") || tableName.endsWith("_TMP");
    }
}
*/
// Above modification is made to ensure that the rule will check the TABLE_PATTERN for all tables, and it will log specific messages and/or add issues based on whether the table name is an exception, whether it starts with "JSR" or "JSD", and whether it follows the naming conventions.

/*
public class TableNamingCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(TableNamingCheck.class.getName());
    private static final Pattern TABLE_PATTERN = Pattern.compile("^([A-Z0-9]{2,3})_([A-Z0-9_]+)(_TN|_TABLE_TN|_TABLE)?$");

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        LOGGER.info("Subscribed to CREATE_TABLE nodes");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a CREATE_TABLE node");

        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME");
            return;
        }

        String tableName = tableNameNode.getTokenOriginalValue().toUpperCase();
        LOGGER.info("Evaluating table name: " + tableName);

        if (isException(tableName)) {
            LOGGER.info("Table name follows the naming conventions: " + tableName);
            return;
        }

        if (TABLE_PATTERN.matcher(tableName).matches()) {
            if (tableName.startsWith("JSR") || tableName.startsWith("JSD")) {
                LOGGER.warning("Table names should not start with JSR or JSD: " + tableName);
                addIssue(node, "Table names should not start with JSR or JSD.");
            } else {
                LOGGER.info("Table name follows the naming conventions: " + tableName);
            }
        } else {
            LOGGER.warning("Table name does not follow the naming conventions: " + tableName);
            addIssue(node, "Table name does not follow the naming conventions.");
        }
    }

    private boolean isException(String tableName) {
        return tableName.endsWith("_GTT") || tableName.endsWith("_MV") || tableName.startsWith("TMP_") || tableName.endsWith("_TEMP") || tableName.endsWith("_TMP");
    }
}*/
/*
public class TableNamingCheck extends PlSqlCheck {
    private static final String PREFIX = "(TMP_)?";
    private static final String APP_CODE = "(?!JSR|JSD)[A-Z0-9]{2,3}";
    private static final String DESCRIPTIVE_NAME = "[A-Z0-9_]+";
    private static final String SUFFIX = "(_TN|_TABLE_TN|_TABLE|_GTT|_MV|_TEMP|_TMP)?$";

    private static final Pattern TABLE_PATTERN = Pattern.compile("^" + PREFIX + APP_CODE + "_" + DESCRIPTIVE_NAME + SUFFIX + "$");
    private final static Logger LOGGER = Logger.getLogger(TableNamingCheck.class.getName());

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a node");

        AstNode unitNameNode = node.getFirstChildOrNull(PlSqlGrammar.UNIT_NAME);
        if (unitNameNode == null) {
            LOGGER.info("Node does not have a child of type UNIT_NAME");
            return;
        }

        String tableName = unitNameNode.getTokenOriginalValue().toUpperCase();
        LOGGER.info("Evaluating table name: " + tableName);

        if (!TABLE_PATTERN.matcher(tableName).matches()) {
            LOGGER.warning("Table name does not follow the naming conventions: " + tableName);
            addIssue(node, "Table name does not follow the naming conventions.");
        } else {
            LOGGER.info("Table name follows the naming conventions: " + tableName);
        }
    }
}*/

/*
public class TableNamingCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(TableNamingCheck.class.getName());

    //private static final Pattern TABLE_PATTERN = Pattern.compile("^([A-Z0-9]{2,3})_([A-Z0-9_]+)(_TN|_TABLE_TN|_TABLE)?$");
    //private static final Pattern TABLE_PATTERN = Pattern.compile("^[A-Z]{2}_([A-Z0-9]*_)*TN$");
    private static final Pattern TABLE_PATTERN = Pattern.compile("^[A-Z]{2,}_([A-Z0-9]*_)*TN$");


    @Override
    public void init() {
        LOGGER.info("Initializing TableNamingCheck and subscribing to CREATE_TABLE");
        subscribeTo(DdlGrammar.CREATE_TABLE);
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a node");

        AstNode unitNameNode = node.getFirstChildOrNull(PlSqlGrammar.UNIT_NAME);
        if (unitNameNode == null) {
            LOGGER.info("Node does not have a child of type UNIT_NAME");
            return;
        }

        String tableName = unitNameNode.getTokenOriginalValue().toUpperCase();
        LOGGER.info("Evaluating table name: " + tableName);

        if (!TABLE_PATTERN.matcher(tableName).matches()) {
            LOGGER.warning("Table name does not follow the naming conventions: " + tableName);
            addIssue(node, "Table name does not follow the naming conventions.");
        } else {
            LOGGER.info("Table name follows the naming conventions: " + tableName);
        }
    }
}
*/

/*    @Override
    public void init() {
        LOGGER.info("Initializing TableNamingCheck and subscribing to CREATE_TABLE");
        subscribeTo(DdlGrammar.CREATE_TABLE);
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a node");

        AstNode unitNameNode = node.getFirstChildOrNull(PlSqlGrammar.UNIT_NAME);
        if (unitNameNode == null) {
            LOGGER.info("Node does not have a child of type UNIT_NAME");
            return;
        }

        String tableName = unitNameNode.getTokenOriginalValue().toUpperCase();
        LOGGER.info("Evaluating table name: " + tableName);

        if (!TABLE_PATTERN.matcher(tableName).matches()) {
            addIssue(node, "Table name does not follow the naming conventions.");
        }
        // Example (pseudo-code)
        if (!isValidTableName(tableName)) {
            LOGGER.info("Reporting violation for table name: " + tableName);
            reportIssue(node, "Table name does not follow the naming conventions.");
        }

    }*/
/*    @Override
    public void init() {
        LOGGER.info("Initializing TableNamingCheck and subscribing to CREATE_TABLE");
        subscribeTo(DdlGrammar.CREATE_TABLE);
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a node");

        // Recursively search for CREATE_TABLE nodes within the descendants of the current node
        List<AstNode> createTableNodes = node.getDescendants(DdlGrammar.CREATE_TABLE);

        if (!createTableNodes.isEmpty()) {
            LOGGER.info("Found " + createTableNodes.size() + " CREATE_TABLE node(s)");

            for (AstNode createTableNode : createTableNodes) {
                String tableName = createTableNode.getTokenOriginalValue().toUpperCase();
                LOGGER.info("Evaluating table name: " + tableName);

                if (!TABLE_PATTERN.matcher(tableName).matches()) {
                    LOGGER.warning("Table name does not follow the naming conventions: " + tableName);
                    addIssue(createTableNode, "Table name does not follow the naming conventions.");
                } else {
                    LOGGER.info("Table name follows the naming conventions: " + tableName);
                }
            }
        } else {
            LOGGER.info("Node does not have descendants of type CREATE_TABLE");
        }
    }
}*/
/*    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
    }

    @Override
    public void visitNode(AstNode node) {
        // Check for a CREATE_TABLE child node
        if (node.hasDirectChildren(DdlGrammar.CREATE_TABLE)) {
            AstNode createTableNode = node.getFirstChild(DdlGrammar.CREATE_TABLE);
            //node.getFirstChild(PlSqlGrammar.UNIT_NAME).getTokenOriginalValue()
            String tableName = createTableNode.getTokenOriginalValue().toUpperCase();
            LOGGER.info("Evaluating table name: " + tableName);

            if (!TABLE_PATTERN.matcher(tableName).matches()) {
                addIssue(node, "Table name does not follow the naming conventions.");
            }
        }
    }
}*/
