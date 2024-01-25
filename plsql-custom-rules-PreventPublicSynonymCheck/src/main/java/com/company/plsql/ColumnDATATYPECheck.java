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
import org.sonar.plugins.plsqlopen.api.symbols.Scope;
import org.sonar.plugins.plsqlopen.api.symbols.Symbol;
import org.sonar.check.RuleProperty;


import org.sonar.plugins.plsqlopen.api.annotations.Priority;
import org.sonar.plugins.plsqlopen.api.annotations.Rule;
import org.sonar.plugins.plsqlopen.api.annotations.ActivatedByDefault;
import org.sonar.plugins.plsqlopen.api.annotations.ConstantRemediation;

import java.util.List;
import java.util.logging.Logger;

//import org.sonar.squidbridge.annotations.ActivatedByDefault;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Rule(
    key = "ColumnDATATYPECheck",
    name = "CMiC - Prohibited Column Data Types Check",
    description = "Certain data types may not be suitable for certain business requirements or may cause issues in specific contexts. "
        + "This rule checks for the usage of such prohibited data types in table column definitions. "
        + "Make sure to avoid using these restricted data types. <br><br>"
        + "<b>Non-Compliance:</b> Flag any column using a prohibited data type.<br><br>"
        + "<b>Reference Document:</b> <a href='https://cmicglobal.sharepoint.com/:w:/t/development/EZE9BhqD2I5CiguVLLnNo-kB24zU-PP1HKhh_i-HabFKHA?e=5b4YrL'>CMiC_SQL_Object_Standards.docx</a>",
    priority = Priority.MAJOR
)
@ConstantRemediation("5min")
@ActivatedByDefault



public class ColumnDATATYPECheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(ColumnDATATYPECheck.class.getName());

    private static final Set<String> REGULAR_DATA_TYPES = new HashSet<>(Arrays.asList(
        "VARCHAR2", "NUMBER", "DATE"
    ));

    private static final Set<String> SPECIALTY_DATA_TYPES = new HashSet<>(Arrays.asList(
        "CLOB", "BLOB", "TIMESTAMP", "TIMESTAMP WITH TIME ZONE", "INTERVAL DAY TO SECOND",
        "LONG", "ROWID", "UNDEFINED", "RAW", "ANYDATA", "XMLTYPE"
    ));

    @Override
    public void init() {
        subscribeTo(DdlGrammar.TABLE_COLUMN_DEFINITION);
        LOGGER.info("Subscribed to TABLE_COLUMN_DEFINITION nodes for DataType Check");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Starting the visit for TABLE_COLUMN_DEFINITION node.");

        AstNode dataTypeNode = node.getFirstChild(PlSqlGrammar.DATATYPE);
        if (dataTypeNode == null) {
            LOGGER.warning("Node does not have a child of type DATATYPE. Exiting early.");
            return;
        }

        String dataTypeValue = dataTypeNode.getTokenOriginalValue().toUpperCase().split("\\(")[0].trim();
        int lineNumber = dataTypeNode.getTokenLine();
        LOGGER.info("DataType value: " + dataTypeValue);

        if (REGULAR_DATA_TYPES.contains(dataTypeValue)) {
            LOGGER.info("DataType is regular. No action needed.");
            return;
        }

        if (SPECIALTY_DATA_TYPES.contains(dataTypeValue)) {
            String errorMessage = String.format("Specialty datatype found at Line #%d.", lineNumber);
            LOGGER.info("DataType is specialty. Adding issue.");
            addIssue(dataTypeNode.getToken(), errorMessage);
            return;
        }

        String errorMessage = String.format("Unapproved datatype found at Line #%d.", lineNumber);
        LOGGER.info("DataType is unapproved. Adding issue.");
        addIssue(dataTypeNode.getToken(), errorMessage);
    }
}

/*
    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a TABLE_COLUMN_DEFINITION node for DataType Check");

        AstNode dataTypeNode = node.getFirstChild(PlSqlGrammar.DATATYPE);
        if (dataTypeNode == null) {
            LOGGER.warning("Node does not have a child of type DATATYPE for DataType Check");
            return;
        }

        String dataTypeValue = dataTypeNode.getTokenOriginalValue().toUpperCase().split("\\(")[0].trim();
        int lineNumber = dataTypeNode.getTokenLine();
        LOGGER.info("Evaluating data type for DataType Check at line " + lineNumber + ": " + dataTypeValue);

        // Regular Types Checks
        if (REGULAR_DATA_TYPES.contains(dataTypeValue)) {
            return; // No issues for these data types
        }

        // Specialty Types Checks
        if (SPECIALTY_DATA_TYPES.contains(dataTypeValue)) {
            String errorMessage = String.format("Specialty datatype found at Line #%d.", lineNumber);
            LOGGER.warning(errorMessage);
            addIssue(dataTypeNode.getToken(), errorMessage);
            return;
        }

        // If no return has been hit yet, then it's an Unapproved datatype
        String errorMessage = String.format("Unapproved datatype found at Line #%d.", lineNumber);
        LOGGER.warning(errorMessage);
        addIssue(dataTypeNode.getToken(), errorMessage);
    }
}
*/

/*public class ColumnDATATYPECheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(ColumnDATATYPECheck.class.getName());

    @Override
    public void init() {
        subscribeTo(PlSqlGrammar.TABLE_COLUMN_DEFINITION);
        LOGGER.info("Subscribed to TABLE_COLUMN_DEFINITION nodes for DataType Check");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a TABLE_COLUMN_DEFINITION node for DataType Check");

        AstNode dataTypeNode = node.getFirstChild(PlSqlGrammar.DATATYPE);
        if (dataTypeNode == null) {
            LOGGER.warning("Node does not have a child of type DATATYPE for DataType Check");
            return;
        }

        String dataTypeValue = dataTypeNode.getTokenOriginalValue().toUpperCase();
        int lineNumber = dataTypeNode.getTokenLine();
        LOGGER.info("Evaluating data type for DataType Check at line " + lineNumber + ": " + dataTypeValue);

        // Regular Types Checks
        if ("VARCHAR2".equals(dataTypeValue) || "NUMBER".equals(dataTypeValue) || "DATE".equals(dataTypeValue)) {
            return; // No issues for these data types
        }

        // Specialty Types Checks
        List<String> specialtyTypes = Arrays.asList("CLOB", "BLOB", "TIMESTAMP", "TIMESTAMP WITH TIME ZONE", "INTERVAL DAY TO SECOND", "LONG", "ROWID", "UNDEFINED", "RAW", "ANYDATA", "XMLTYPE");
        if (specialtyTypes.contains(dataTypeValue)) {
            String errorMessage = String.format("Specialty datatype found at Line #%d.", lineNumber);
            LOGGER.warning(errorMessage);
            addIssue(dataTypeNode.getToken(), errorMessage);
            return;
        }

        // If no return has been hit yet, then it's an Unapproved datatype
        String errorMessage = String.format("Unapproved datatype found at Line #%d.", lineNumber);
        LOGGER.warning(errorMessage);
        addIssue(dataTypeNode.getToken(), errorMessage);
    }
}*/

/*public class ColumnDataTypeCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(ColumnDataTypeCheck.class.getName());

    @Override
    public void init() {
        subscribeTo(DdlGrammar.TABLE_COLUMN_DEFINITION);
        LOGGER.info("Subscribed to TABLE_COLUMN_DEFINITION nodes for DataType Check");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a TABLE_COLUMN_DEFINITION node for DataType Check");

        AstNode dataTypeNode = node.getFirstChild(PlSqlGrammar.DATATYPE);
        if (dataTypeNode == null) {
            LOGGER.warning("Node does not have a child of type DATATYPE for DataType Check");
            return;
        }

        String dataTypeValue = dataTypeNode.getTokenOriginalValue().toUpperCase();
        int lineNumber = dataTypeNode.getTokenLine();
        LOGGER.info("Evaluating data type for DataType Check at line " + lineNumber + ": " + dataTypeValue);

        // Regular Types Checks
        if ("VARCHAR2".equals(dataTypeValue) || "NUMBER".equals(dataTypeValue) || "DATE".equals(dataTypeValue)) {
            return; // No issues for these data types
        }

        // Specialty Types Checks
        List<String> specialtyTypes = Arrays.asList("CLOB", "BLOB", "TIMESTAMP", "TIMESTAMP WITH TIME ZONE", "INTERVAL DAY TO SECOND", "LONG", "ROWID", "UNDEFINED", "RAW", "ANYDATA", "XMLTYPE");
        if (specialtyTypes.contains(dataTypeValue)) {
            String errorMessage = String.format("Specialty datatype found at Line #%d.", lineNumber);
            LOGGER.warning(errorMessage);
            addIssue(dataTypeNode.getToken(), errorMessage);
            return;
        }

        // If no return has been hit yet, then it's an Unapproved datatype
        String errorMessage = String.format("Unapproved datatype found at Line #%d.", lineNumber);
        LOGGER.warning(errorMessage);
        addIssue(dataTypeNode.getToken(), errorMessage);
    }
}*/

/*public class ColumnDataTypeCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(ColumnDataTypeCheck.class.getName());
    private static final Set<String> REGULAR_DATA_TYPES = new HashSet<>(Arrays.asList(
        "CHARACTER_DATAYPE", "NUMERIC_DATATYPE", "DATE_DATATYPE"
    ));
    private static final Set<String> SPECIALTY_DATA_TYPES = new HashSet<>(Arrays.asList(
        "CLOB", "BLOB", "TIMESTAMP", "TIMESTAMP WITH TIME ZONE", "INTERVAL DAY TO SECOND",
        "LONG", "ROWID", "UNDEFINED", "RAW", "ANYDATA", "XMLTYPE"
    ));

    @Override
    public void init() {
        subscribeTo(PlSqlGrammar.TABLE_COLUMN_DEFINITION);
        LOGGER.info("Subscribed to TABLE_COLUMN_DEFINITION nodes for ColumnDataTypeCheck");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a TABLE_COLUMN_DEFINITION node for ColumnDataTypeCheck");

        AstNode datatypeNode = node.getFirstChild(PlSqlGrammar.DATATYPE);
        if (datatypeNode != null) {
            String dataType = datatypeNode.getTokenOriginalValue().toUpperCase();

            // Check for regular data types
            if (REGULAR_DATA_TYPES.contains(dataType)) {
                LOGGER.info(String.format("Column uses regular datatype %s", dataType));
                return;
            }

            // Check for specialty data types if it's not a regular type
            if (SPECIALTY_DATA_TYPES.contains(dataType)) {
                String specialtyMessage = "Specialty datatype found";
                LOGGER.warning(specialtyMessage);
                addIssue(datatypeNode.getToken(), specialtyMessage);
                return;
            }

            // Report unapproved data type
            String errorMessage = "Unapproved datatype found";
            LOGGER.warning(errorMessage);
            addIssue(datatypeNode.getToken(), errorMessage);
        }
    }
}*/


