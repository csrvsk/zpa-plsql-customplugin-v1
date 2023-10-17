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
    key = "TableNameConventionCheck",
    name = "CMiC - Table Naming Conventions",
    description = "Ensures tables follow the defined naming conventions. "
        + "Tables must start with a valid prefix and have the appropriate suffix. "
        + "Special rules apply for TMP prefixed tables.",
    priority = Priority.MAJOR
)
public class TableNameConventionCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(TableNameConventionCheck.class.getName());

    private static final Set<String> VALID_PREFIXES = new HashSet<>(Arrays.asList("AP_", "AR_", "BA_", "CI_", "CM_", "COL_", "DC_", "DM_", "DSH_", "ECM_", "EM_", "FA_", "GL_", "HR_", "IMG_", "JB_", "JC_", "KPB_", "MS_", "OM_", "PM_", "PO_", "PRD_", "PRM_", "PY_", "PYE_", "RP_", "RQ_", "SC_", "SD_", "SYS_", "TMP_", "TS_", "UE_", "WKF_"));

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        LOGGER.info("Subscribed to CREATE_TABLE nodes for TableNameConventionCheck");
    }

    @Override
    public void visitNode(AstNode node) {
        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME for TableNameConventionCheck");
            return;
        }

        String tableName = tableNameNode.getTokenOriginalValue().toUpperCase();
        int lineNumber = tableNameNode.getTokenLine();
        LOGGER.info("Evaluating table name convention at line " + lineNumber + ": " + tableName);

        // Check for tables starting with "TMP_"
        if (tableName.startsWith("TMP_")) {
            if (!tableName.endsWith("_TEMP") && !tableName.endsWith("_TMP")) {
                String errorMessage = String.format("Invalid TMP suffix for the table name at Line #%d.", lineNumber);
                LOGGER.warning(errorMessage);
                addIssue(tableNameNode.getToken(), errorMessage);
            }
            return;  // Important to exit after evaluating TMP_ related conditions.
        } else if (!hasValidPrefix(tableName)) { // For other tables, check for valid prefix.
            String errorMessage = String.format("Invalid table name prefix at Line #%d.", lineNumber);
            LOGGER.warning(errorMessage);
            addIssue(tableNameNode.getToken(), errorMessage);
            return;
        } else if (!tableName.endsWith("_TN") && !tableName.endsWith("_GTT") && !tableName.endsWith("_MV")) {
            String errorMessage = String.format("Invalid table name suffix at Line #%d.", lineNumber);
            LOGGER.warning(errorMessage);
            addIssue(tableNameNode.getToken(), errorMessage);
        }
    }

    private boolean hasValidPrefix(String tableName) {
        return VALID_PREFIXES.stream().anyMatch(tableName::startsWith);
    }
}

/*
public class TableNameConventionCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(TableNameConventionCheck.class.getName());

    private static final Set<String> VALID_PREFIXES = new HashSet<>(Arrays.asList("AP_", "AR_", "BA_", "CI_", "CM_", "COL_", "DC_", "DM_", "DSH_", "ECM_", "EM_", "FA_", "GL_", "HR_", "IMG_", "JB_", "JC_", "KPB_", "MS_", "OM_", "PM_", "PO_", "PRD_", "PRM_", "PY_", "PYE_", "RP_", "RQ_", "SC_", "SD_", "SYS_", "TMP_", "TS_", "UE_", "WKF_"));

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        LOGGER.info("Subscribed to CREATE_TABLE nodes for TableNameConventionCheck");
    }

    @Override
    public void visitNode(AstNode node) {
        AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (tableNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME for TableNameConventionCheck");
            return;
        }

        String tableName = tableNameNode.getTokenOriginalValue().toUpperCase();
        int lineNumber = tableNameNode.getTokenLine();
        LOGGER.info("Evaluating table name convention at line " + lineNumber + ": " + tableName);

        // Step-1
        if (hasValidPrefix(tableName)) {
            if (!tableName.endsWith("_TN") && !tableName.endsWith("_GTT") && !tableName.endsWith("_MV")) {
                String errorMessage = String.format("Invalid table name suffix at Line #%d.", lineNumber);
                LOGGER.warning(errorMessage);
                addIssue(tableNameNode.getToken(), errorMessage);
            }
            return;
        }
        // Step-2
        if (!tableName.startsWith("TMP_")) {
            String errorMessage = String.format("Invalid table name prefix at Line #%d.", lineNumber);
            LOGGER.warning(errorMessage);
            addIssue(tableNameNode.getToken(), errorMessage);
        } else {
            // Step-2.1
            if (!tableName.endsWith("_TEMP") && !tableName.endsWith("_TMP")) {
                String errorMessage = String.format("Invalid TMP suffix for the table name at Line #%d.", lineNumber);
                LOGGER.warning(errorMessage);
                addIssue(tableNameNode.getToken(), errorMessage);
            }
        }
    }

    private boolean hasValidPrefix(String tableName) {
        for (String prefix : VALID_PREFIXES) {
            if (tableName.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}*/
