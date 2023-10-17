package com.company.plsql;

import org.sonar.plugins.plsqlopen.api.DdlGrammar;
import org.sonar.plugins.plsqlopen.api.annotations.Priority;
import org.sonar.plugins.plsqlopen.api.annotations.Rule;
import org.sonar.plugins.plsqlopen.api.annotations.ActivatedByDefault;
import org.sonar.plugins.plsqlopen.api.annotations.ConstantRemediation;
import org.sonar.plugins.plsqlopen.api.checks.PlSqlCheck;
import org.sonar.plugins.plsqlopen.api.sslr.AstNode;
//import org.sonar.plugins.plsqlopen.api.AstNode;
import org.sonar.plugins.plsqlopen.api.PlSqlGrammar;

import java.util.regex.Pattern;
import java.util.logging.Logger;
// Add this import for List
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

//import org.sonar.check.Priority;
//import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;


@Rule(
    key = "TableNameMaxLengthCheck",
    name = "CMiC - Table names must not exceed maximum allowed length",
    description = "Table names should not exceed a predefined maximum length to maintain clarity and prevent database constraints. Longer names can also make SQL statements and code less readable. <br><br>"
        + "<b>Maximum Allowed Length:</b> " + TableNameMaxLengthCheck.MAX_LENGTH + " characters. <br><br>"
        + "<b>Non-Compliance:</b> Flag any table name exceeding this length.<br><br>"
        + "<b>Reference Document:</b> <a href='https://cmicglobal.sharepoint.com/:w:/t/development/EZE9BhqD2I5CiguVLLnNo-kB24zU-PP1HKhh_i-HabFKHA?e=5b4YrL'>CMiC_SQL_Object_Standards.docx</a>",
    priority = Priority.MAJOR
)
public class TableNameMaxLengthCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(TableNameMaxLengthCheck.class.getName());
    static final int MAX_LENGTH = 30; // Define the maximum allowed table name length here

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        LOGGER.info("Subscribed to CREATE_TABLE nodes for TableNameMaxLengthCheck");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a CREATE_TABLE node for TableNameMaxLengthCheck");

        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME for TableNameMaxLengthCheck");
            return;
        }

        String tableName = tableNameNode.getTokenOriginalValue().toUpperCase();
        int lineNumber = tableNameNode.getTokenLine();
        LOGGER.info("Evaluating table name for maximum allowed length of characters check at line " + lineNumber + ": " + tableName);

        if (tableName.length() > MAX_LENGTH) {
            String errorMessage = String.format("Table name exceeds %d characters length at Line #%d.", MAX_LENGTH, lineNumber);
            LOGGER.warning(errorMessage);
            addIssue(tableNameNode.getToken(), errorMessage);
        }
    }
}
