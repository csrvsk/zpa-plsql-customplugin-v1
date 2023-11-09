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
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import org.sonar.check.RuleProperty;

@Rule(
    key = "JSRJSDCheck",
    name = "CMiC - Table names must not start with JSR or JSD",
    description = "Table names must not start with the prefixes 'JSR' or 'JSD'. These prefixes are reserved and have special meanings in certain contexts. "
        + "Using them in table names can cause confusion and potential conflicts with future developments. "
        + "Ensure that your table names do not begin with these prefixes. <br><br>"
        + "<b>Non-Compliance:</b> Flag any table name starting with 'JSR' or 'JSD'.<br><br>"
        + "<b>Reference Document:</b> <a href='https://cmicglobal.sharepoint.com/:w:/t/development/EZE9BhqD2I5CiguVLLnNo-kB24zU-PP1HKhh_i-HabFKHA?e=5b4YrL'>CMiC_SQL_Object_Standards.docx</a>",
    priority = Priority.MAJOR
)
@ConstantRemediation("5min")
@ActivatedByDefault

public class JSRJSDCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(JSRJSDCheck.class.getName());

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        LOGGER.info("Subscribed to CREATE_TABLE nodes for JSR/JSD Check");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a CREATE_TABLE node for JSR/JSD Check");

        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME for JSR/JSD Check");
            return;
        }

        List<AstNode> tableNameChildren = tableNameNode.getChildren();
        String schemaName = null;
        String actualTableName;

        if (tableNameChildren.size() > 1) {
            // We expect this to be schema.table structure
            schemaName = tableNameChildren.get(0).getTokenOriginalValue().toUpperCase();
            actualTableName = tableNameChildren.get(2).getTokenOriginalValue().toUpperCase(); // Index 1 is for the dot
        } else {
            // Only table name is provided
            actualTableName = tableNameNode.getTokenOriginalValue().toUpperCase();
        }

        int lineNumber = tableNameNode.getTokenLine();
        LOGGER.info("Evaluating table name for JSR/JSD Check at line " + lineNumber + ". Schema: " + (schemaName != null ? schemaName : "default") + ", Table: " + actualTableName);

        // Strict rule check: Names starting with JSR or JSD are strictly prohibited
        if (actualTableName.startsWith("JSR") || actualTableName.startsWith("JSD")) {
            String errorMessage = String.format("Table name started with JSR or JSD at Line #%d", lineNumber);
            LOGGER.warning(errorMessage);
            addIssue(tableNameNode.getToken(), errorMessage);
        }
    }
}



/* this commented code work well to detect table names with JSR/JSD. But not if the format is like "SCHEMA_NAME.TABLE_NAME".
public class JSRJSDCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(JSRJSDCheck.class.getName());

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        LOGGER.info("Subscribed to CREATE_TABLE nodes for JSR/JSD Check");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a CREATE_TABLE node for JSR/JSD Check");

        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME for JSR/JSD Check");
            return;
        }

        String tableName = tableNameNode.getTokenOriginalValue().toUpperCase();
        int lineNumber = tableNameNode.getTokenLine();
        LOGGER.info("Evaluating table name for JSR/JSD Check at line " + lineNumber + ": " + tableName);

        // Strict rule check: Names starting with JSR or JSD are strictly prohibited
        if (tableName.startsWith("JSR") || tableName.startsWith("JSD")) {
            String errorMessage = String.format("Table name started with JSR or JSD at Line #%d", lineNumber);
            LOGGER.warning(errorMessage);
            addIssue(tableNameNode.getToken(), errorMessage);
            return;
        }
    }
}*/
