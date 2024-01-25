package com.company.plsql;

import org.sonar.plugins.plsqlopen.api.DdlGrammar;
import org.sonar.plugins.plsqlopen.api.annotations.Priority;
import org.sonar.plugins.plsqlopen.api.annotations.Rule;
import org.sonar.plugins.plsqlopen.api.annotations.ActivatedByDefault;
import org.sonar.plugins.plsqlopen.api.annotations.ConstantRemediation;
import org.sonar.plugins.plsqlopen.api.checks.PlSqlCheck;
import org.sonar.plugins.plsqlopen.api.sslr.AstNode;
import org.sonar.plugins.plsqlopen.api.PlSqlGrammar;
import org.sonar.plugins.plsqlopen.api.PlSqlKeyword;
import java.util.regex.Pattern;
import java.util.logging.Logger;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import org.sonar.check.RuleProperty;

@Rule(
    key = "PreventPublicSynonymCheck",
    name = "CMiC - Prevent creation of PUBLIC SYNONYMS for CMiC objects",
    description = "Creating PUBLIC SYNONYMS for CMiC objects can lead to security risks and unexpected behaviors. This rule ensures that no public synonyms are created for CMiC objects. <br><br>"
        + "<b>Non-Compliance:</b> Flag any creation of public synonyms for CMiC objects.<br><br>"
        + "<b>Reference Document:</b> <a href='https://cmicglobal.sharepoint.com/:w:/t/development/EZE9BhqD2I5CiguVLLnNo-kB24zU-PP1HKhh_i-HabFKHA?e=5b4YrL'>CMiC_SQL_Object_Standards.docx</a>",
    priority = Priority.CRITICAL
)

public class PreventPublicSynonymCheck extends PlSqlCheck {

    private static final Logger LOGGER = Logger.getLogger(PreventPublicSynonymCheck.class.getName());

    @Override
    public void init() {
        // Subscribe to the CREATE_SYNONYM grammar rule to get notified when such statements are encountered
        subscribeTo(DdlGrammar.CREATE_SYNONYM);
        LOGGER.info("Subscribed to CREATE_SYNONYM nodes for PreventPublicSynonymCheck");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a node for PreventPublicSynonymCheck");

        // Log the type of node being visited for clarity
        LOGGER.fine("Node type: " + node.getType());

        boolean publicKeywordFound = false;

        for (AstNode child : node.getChildren()) {
            LOGGER.fine("Checking child node of type: " + child.getType());

            if (child.is(PlSqlKeyword.PUBLIC)) {
                publicKeywordFound = true;

                int lineNumber = child.getTokenLine();
                String errorMessage = "PUBLIC SYNONYM for CMiC object found at Line #" + lineNumber + ".";

                // Log the discovery of the PUBLIC keyword and the line number where it was found
                LOGGER.info("PUBLIC SYNONYM found at line " + lineNumber);

                LOGGER.warning(errorMessage);

                // Highlight the 'PUBLIC' keyword by underlining it in the reported issue
                addIssue(child.getToken(), errorMessage);

                break; // No need to check further once the PUBLIC keyword is found
            }
        }

        if (!publicKeywordFound) {
            // Log that the PUBLIC keyword was not found in this CREATE_SYNONYM statement
            LOGGER.fine("No PUBLIC SYNONYM found in the CREATE statement.");
        }
    }
}

/*public class PreventPublicSynonymCheck extends PlSqlCheck {

    private static final Logger LOGGER = Logger.getLogger(PreventPublicSynonymCheck.class.getName());

    @Override
    public void init() {
        // Subscribe to the CREATE_SYNONYM grammar rule to get notified when such statements are encountered
        subscribeTo(DdlGrammar.CREATE_SYNONYM);
        LOGGER.info("Subscribed to CREATE_SYNONYM nodes for PreventPublicSynonymCheck");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a node for PreventPublicSynonymCheck");

        for (AstNode child : node.getChildren()) {
            if (child.is(PlSqlKeyword.PUBLIC)) {
                int lineNumber = child.getTokenLine();
                String errorMessage = "Public synonym for CMiC object found at Line #" + lineNumber + ".";

                LOGGER.warning(errorMessage);

                // Highlight the 'PUBLIC' keyword by underlining it in the reported issue
                // Assuming 'addIssue' can take parameters to specify the exact location for underlining
                addIssue(child.getToken(), errorMessage);

                break; // No need to check further once the PUBLIC keyword is found
            }
        }
    }
}*/

/*    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a node for PreventPublicSynonymCheck");

        // Iterate through the children of the CREATE_SYNONYM node to find the PUBLIC keyword
        for (AstNode child : node.getChildren()) {
            if (child.is(PlSqlKeyword.PUBLIC)) {
                // The PUBLIC keyword is present, indicating a public synonym, which is not allowed
                int lineNumber = child.getTokenLine();
                String errorMessage = "Public synonym for CMiC object at Line #" + lineNumber + ".";

                LOGGER.warning(errorMessage);
                // Add an issue at the line number of the PUBLIC keyword
                addIssue(child.getToken(), errorMessage);
                break; // Since we've found the PUBLIC keyword, no need to check further
            }
        }
    }
}*/
/*

public class PreventPublicSynonymCheck extends PlSqlCheck {

    private static final Logger LOGGER = Logger.getLogger(PreventPublicSynonymCheck.class.getName());

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_SYNONYM);
        LOGGER.info("Subscribed to CREATE_SYNONYM nodes for PreventPublicSynonymCheck");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a node for PreventPublicSynonymCheck");
        LOGGER.info("Node type: " + node.getType());

        // Check if the node is a CREATE_SYNONYM statement
        if (node.is(DdlGrammar.CREATE_SYNONYM)) {
            boolean isPublicSynonym = false;
            String synonymName = "";

            // Iterate through the children of the CREATE_SYNONYM node
            for (AstNode child : node.getChildren()) {
                // Check if the child is of type UNIT_NAME
                if (child.is(PlSqlGrammar.UNIT_NAME)) {
                    synonymName = child.getTokenOriginalValue().toUpperCase();
                }
            }

            // Check if the synonym is public
            if (!synonymName.isEmpty()) {
                int lineNumber = node.getTokenLine();
                String errorMessage = String.format("Creation of public synonym '%s' is not allowed at Line #%d.", synonymName, lineNumber);

                LOGGER.info("Generated Error Message: " + errorMessage);
                LOGGER.warning(errorMessage);
                addIssue(node.getToken(), errorMessage);
            }
        }
    }
}
*/

/*
public class PreventPublicSynonymCheck extends PlSqlCheck {

    private static final Logger LOGGER = Logger.getLogger(PreventPublicSynonymCheck.class.getName());

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_SYNONYM);
        LOGGER.info("Subscribed to CREATE_SYNONYM nodes for PreventPublicSynonymCheck");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a node for PreventPublicSynonymCheck");
        LOGGER.info("Node type: " + node.getType());

        // Initialize variables to store information about the synonym
        boolean isPublicSynonym = false;
        String synonymName = "";

        // Iterate through the children of the CREATE_SYNONYM node
        for (AstNode child : node.getChildren()) {
            LOGGER.info("Child token value: " + child.getTokenOriginalValue() + ", Type: " + child.getType());

            // Check if the child node is the PUBLIC keyword
            if (child.is(PlSqlKeyword.PUBLIC)) {
                isPublicSynonym = true;
            }

            // Check if the child node is the synonym name (UNIT_NAME)
            if (child.is(PlSqlGrammar.UNIT_NAME)) {
                synonymName = child.getTokenOriginalValue().toUpperCase();
            }
        }

        // Check if it's a public synonym and if the synonym name is not empty
        if (isPublicSynonym && !synonymName.isEmpty()) {
            int lineNumber = node.getTokenLine();
            String errorMessage = String.format("Creation of public synonym '%s' is not allowed at Line #%d.", synonymName, lineNumber);

            LOGGER.info("Generated Error Message: " + errorMessage);
            LOGGER.warning(errorMessage);
            addIssue(node.getToken(), errorMessage);
        }
    }
}*/

/*public class PreventPublicSynonymCheck extends PlSqlCheck {

    private static final Logger LOGGER = Logger.getLogger(PreventPublicSynonymCheck.class.getName());

    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_SYNONYM);
        LOGGER.info("Subscribed to CREATE_SYNONYM nodes for PreventPublicSynonymCheck");
    }

    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a node for PreventPublicSynonymCheck");
        LOGGER.info("Node type: " + node.getType());

        boolean isPublicSynonym = false;
        String synonymName = "";

        for (AstNode childNode : node.getChildren()) {
            LOGGER.info("Child token value: " + childNode.getTokenOriginalValue() + ", Type: " + childNode.getType());
            if ("PUBLIC".equalsIgnoreCase(childNode.getTokenOriginalValue())) {
                isPublicSynonym = true;
            } else if ("IDENTIFIER".equals(childNode.getType().toString())) {
                synonymName = childNode.getTokenOriginalValue().toUpperCase();
            }
        }

        if (isPublicSynonym && !synonymName.isEmpty()) {
            int lineNumber = node.getTokenLine();
            String errorMessage = String.format("Creation of public synonym '%s' is not allowed at Line #%d.", synonymName, lineNumber);
            LOGGER.info("Generated Error Message: " + errorMessage);
            LOGGER.warning(errorMessage);
            addIssue(node.getToken(), errorMessage);
        }
    }
}*/


/*    @Override
    public void visitNode(AstNode node) {
        LOGGER.info("Visiting a CREATE_SYNONYM node for PreventPublicSynonymCheck");

        boolean isPublicSynonym = false;
        AstNode synonymNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);

        if (synonymNameNode == null) {
            LOGGER.warning("Node does not have a child of type UNIT_NAME for PreventPublicSynonymCheck");
            return;
        }

        for (AstNode child : node.getChildren()) {
            if ("PUBLIC".equalsIgnoreCase(child.getTokenOriginalValue())) {
                isPublicSynonym = true;
                break;
            }
        }

        if (isPublicSynonym) {
            String synonymName = synonymNameNode.getTokenOriginalValue().toUpperCase();
            int lineNumber = synonymNameNode.getTokenLine();
            String errorMessage = String.format("Creation of public synonym '%s' is not allowed at Line #%d.", synonymName, lineNumber);

            // Print the generated error message for debugging
            LOGGER.info("Generated Error Message: " + errorMessage);

            LOGGER.warning(errorMessage);
            addIssue(synonymNameNode.getToken(), errorMessage);
        }
    }
}*/
