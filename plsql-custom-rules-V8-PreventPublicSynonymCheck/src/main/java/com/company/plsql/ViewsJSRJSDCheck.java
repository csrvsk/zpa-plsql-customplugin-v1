/*
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

@Rule(
    key = "ViewsJSRJSDCheck",
    name = "CMiC - View names for DA must not start with JSR or JSD",
    description = "View names for DA must not start with the prefixes 'JSR' or 'JSD'. These prefixes are reserved and have special meanings in certain contexts. "
        + "Using them in view names can cause confusion and potential conflicts with future developments. "
        + "Ensure that your view names do not begin with these prefixes. <br><br>"
        + "<b>Non-Compliance:</b> Flag any view name starting with 'JSR' or 'JSD'.<br><br>"
        + "<b>Reference Document:</b> <a href='https://cmicglobal.sharepoint.com/:w:/t/development/EZE9BhqD2I5CiguVLLnNo-kB24zU-PP1HKhh_i-HabFKHA?e=5b4YrL'>CMiC_SQL_Object_Standards.docx</a>",
    priority = Priority.MAJOR
)
@ConstantRemediation("5min")
@ActivatedByDefault

public class ViewsJSRJSDCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(ViewsJSRJSDCheck.class.getName());

    @Override
    public void init() {
        subscribeTo(PlSqlGrammar.CREATE_VIEW, PlSqlGrammar.DDL_EVENT);
        LOGGER.info("Subscribed to VIEW operations for ViewsJSRJSDCheck");
    }

*/
/*    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a VIEW operation node for ViewsJSRJSDCheck");

        if (node.getType() == PlSqlGrammar.DDL_EVENT) {
            // If the node is of type DDL_EVENT, check if it is an ALTER VIEW operation
            AstNode alterNode = node.getFirstChild(PlSqlGrammar.ALTER);
            if (alterNode != null) {
                // Further check for VIEW
                AstNode viewNode = alterNode.getNextSibling();
                if (viewNode != null && "VIEW".equals(viewNode.getTokenOriginalValue())) {
                    // Handle ALTER VIEW operation
                    handleViewOperation(viewNode);
                }
            }
        } else {
            // Handle other view operations (e.g., CREATE VIEW)
            handleViewOperation(node);
        }
    }*//*

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a DDL event node for ViewsJSRJSDCheck");

        if (node.getType() == PlSqlGrammar.DDL_EVENT) {
            AstNode alterNode = node.getFirstChild(PlSqlGrammar.ALTER);
            if (alterNode != null) {
                AstNode viewNode = node.getFirstChild(PlSqlGrammar.VIEW);
                if (viewNode != null) {
                    AstNode identifierNode = viewNode.getNextSibling();
                    if (identifierNode != null && this.viewNames.contains(identifierNode.getTokenOriginalValue().toUpperCase())) {
                        //if (identifierNode != null && viewNames.contains(identifierNode.getTokenOriginalValue().toUpperCase())) {
                        int lineNumber = identifierNode.getTokenLine();
                        String errorMessage = String.format("ALTER VIEW with name %s at Line #%d", identifierNode.getTokenOriginalValue(), lineNumber);
                        LOGGER.warning(errorMessage);
                        addIssue(identifierNode.getToken(), errorMessage);
                    }
                }
            }
        }
    }
*/
/*    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a VIEW operation node for ViewsJSRJSDCheck");

        if (node.getType() == PlSqlGrammar.CREATE_VIEW) {
            // ... (existing logic for CREATE_VIEW)
        } else if (node.getType() == PlSqlGrammar.RECOVERY && node.getFirstChild(PlSqlGrammar.ALTER) != null) {
            AstNode viewNode = node.getFirstChild(PlSqlGrammar.VIEW);
            AstNode identifierNode = node.getFirstChild(PlSqlGrammar.IDENTIFIER);

            if (viewNode != null && identifierNode != null) {
                // ... (logic for handling ALTER VIEW)
            } else {
                LOGGER.warning("Node does not have the expected children for ALTER VIEW");
            }
        } else {
            LOGGER.warning("Unexpected node type: " + node.getType());
        }
    }*//*


    private void handleViewOperation(AstNode node) {
        AstNode viewNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (viewNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME for ViewsJSRJSDCheck");
            return;
        }

        List<AstNode> viewNameChildren = viewNameNode.getChildren();
        String schemaName = null;
        String actualViewName;

        if (viewNameChildren.size() > 1) {
            // We expect this to be schema.view structure
            schemaName = viewNameChildren.get(0).getTokenOriginalValue().toUpperCase();
            actualViewName = viewNameChildren.get(2).getTokenOriginalValue().toUpperCase(); // Index 1 is for the dot
        } else {
            // Only view name is provided
            actualViewName = viewNameNode.getTokenOriginalValue().toUpperCase();
        }

        int lineNumber = viewNameNode.getTokenLine();
        LOGGER.info("Evaluating view name for ViewsJSRJSDCheck at line " + lineNumber + ". Schema: " + (schemaName != null ? schemaName : "default") + ", View: " + actualViewName);

        // Check if view name is in the list of view names to check
        if ("DA".equals(actualViewName) || "JSR".equals(actualViewName) || "JSD".equals(actualViewName)) {
            // Your specific logic for checking view names goes here
            // If the view name starts with "JSR" or "JSD", raise an issue
            if (actualViewName.startsWith("JSR") || actualViewName.startsWith("JSD")) {
                String errorMessage = String.format("View name started with JSR or JSD at Line #%d", lineNumber);
                LOGGER.warning(errorMessage);
                addIssue(viewNameNode.getToken(), errorMessage);
            }
        }
    }
}


*/
/*public class ViewsJSRJSDCheck extends PlSqlCheck {

    private final static Logger LOGGER = Logger.getLogger(ViewsJSRJSDCheck.class.getName());
    private final List<String> viewOperations = Arrays.asList(PlSqlGrammar.CREATE_VIEW, DdlGrammar.ALTER);
    private final List<String> viewNames = Arrays.asList("DA", "JSR", "JSD");

    @Override
    public void init() {
        subscribeTo(viewOperations.stream().map(this::getToken).toArray(Integer[]::new));
        LOGGER.info("Subscribed to VIEW operations for ViewsJSRJSDCheck");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a VIEW operation node for ViewsJSRJSDCheck");

        AstNode viewNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
        if (viewNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME for ViewsJSRJSDCheck");
            return;
        }

        List<AstNode> viewNameChildren = viewNameNode.getChildren();
        String schemaName = null;
        String actualViewName;

        if (viewNameChildren.size() > 1) {
            // We expect this to be schema.view structure
            schemaName = viewNameChildren.get(0).getTokenOriginalValue().toUpperCase();
            actualViewName = viewNameChildren.get(2).getTokenOriginalValue().toUpperCase(); // Index 1 is for the dot
        } else {
            // Only view name is provided
            actualViewName = viewNameNode.getTokenOriginalValue().toUpperCase();
        }

        int lineNumber = viewNameNode.getTokenLine();
        LOGGER.info("Evaluating view name for ViewsJSRJSDCheck at line " + lineNumber + ". Schema: " + (schemaName != null ? schemaName : "default") + ", View: " + actualViewName);

        // Check if view name is in the list of view names to check
        if (viewNames.contains(actualViewName)) {
            // Your specific logic for checking view names goes here
            // If the view name starts with "JSR" or "JSD", raise an issue
            if (actualViewName.startsWith("JSR") || actualViewName.startsWith("JSD")) {
                String errorMessage = String.format("View name started with JSR or JSD at Line #%d", lineNumber);
                LOGGER.warning(errorMessage);
                addIssue(viewNameNode.getToken(), errorMessage);
            }
        }
    }
}*//*


*/
