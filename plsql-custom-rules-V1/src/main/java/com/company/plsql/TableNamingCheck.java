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
    name = "Table Naming Conventions",
    description = "Ensure tables follow the defined naming conventions.",
    key = "TableNamingCheck",
    priority = Priority.MAJOR
)
@ConstantRemediation("10min")
@ActivatedByDefault
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
