
# PreventPublicSynonymCheck Rule Documentation

## Rule Overview

The creation of public synonyms in Oracle databases can lead to security risks and namespace pollution, as they are accessible to all users. To mitigate these risks, the `PreventPublicSynonymCheck` rule was developed to automatically identify and flag instances where public synonyms are created in PL/SQL code.

## Initialization

When the rule is initialized, it subscribes to `CREATE_SYNONYM` statements within the code being analyzed. This subscription tells the analysis tool to invoke this rule whenever it encounters a `CREATE_SYNONYM` statement in the abstract syntax tree (AST) generated from the PL/SQL code.

```java
@Override
public void init() {
    subscribeTo(DdlGrammar.CREATE_SYNONYM);
    LOGGER.info("Subscribed to CREATE_SYNONYM nodes for PreventPublicSynonymCheck");
}
```

## Node Visitation

Upon encountering a `CREATE_SYNONYM` statement, the rule's `visitNode` method is called. This method is responsible for analyzing the statement to determine if it defines a public synonym.

```java
@Override
public void visitNode(AstNode node) {
    LOGGER.info("Visiting a node for PreventPublicSynonymCheck");

    for (AstNode child : node.getChildren()) {
        if (child.is(PlSqlKeyword.PUBLIC)) {
            int lineNumber = child.getTokenLine();
            String errorMessage = "Public synonym for CMiC object found at Line #" + lineNumber + ".";
            LOGGER.warning(errorMessage);
            addIssue(child.getToken(), errorMessage);
            break;
        }
    }
}
```

## Logic Flow

1. **Node Analysis**: The `visitNode` method iterates through each child node of the `CREATE_SYNONYM` statement.
2. **Keyword Detection**: For each child node, the rule checks if the node represents the `PUBLIC` keyword. This is crucial as the presence of `PUBLIC` indicates the synonym being created is public.
3. **Flagging Violations**: When a `PUBLIC` keyword is found, the rule constructs an error message indicating the line number where the violation occurs. This message is logged, and an issue is reported at the location of the `PUBLIC` keyword in the source code.

## Example Scenario

Consider the following PL/SQL statement:

```plsql
CREATE PUBLIC SYNONYM CMiC_Financials FOR some_schema.financial_table;
```

Here's how the `PreventPublicSynonymCheck` rule processes this statement:

- The rule identifies the `CREATE_SYNONYM` statement and starts analyzing its child nodes.
- It finds the `PUBLIC` keyword as one of the children of the `CREATE_SYNONYM` node.
- The rule then determines the line number where `PUBLIC` is found and constructs an error message: "Public synonym for CMiC object found at Line #X."
- This error message is logged, and an issue is reported to highlight the use of a public synonym, which is against the coding standard being enforced.

## Conclusion

The `PreventPublicSynonymCheck` rule serves as an automated guardrail against the use of public synonyms in PL/SQL code, aligning with best practices for database security and schema management. By integrating this rule into SonarQube, we ensure consistent enforcement of this important coding standard across our company's SQL codebase.