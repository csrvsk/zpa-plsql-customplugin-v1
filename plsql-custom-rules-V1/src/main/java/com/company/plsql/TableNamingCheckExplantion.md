# Initial Version - TableNamingCheck Class Explanation

The `TableNamingCheck` class extends `PlSqlCheck` and is responsible for checking whether the names of the tables created in PL/SQL code follow a specific naming convention. The class uses regular expression matching for this purpose.

## Class Structure

### Logger

```java
private final static Logger LOGGER = Logger.getLogger(TableNamingCheck.class.getName());
```

- A `Logger` instance is initialized for logging information, warnings, or errors during the rule check.

### Table Name Pattern

```java
private static final Pattern TABLE_PATTERN = Pattern.compile("^[A-Z]{2,}_([A-Z0-9]*_)*TN$");
```

- A `Pattern` instance represents the compiled form of a regular expression. This pattern defines the naming convention that table names must follow. 
- According to the pattern, a valid table name must start with two or more uppercase letters, followed by a sequence of uppercase letters, digits, or underscores, and must end with 'TN'.

### Initialization Method

```java
@Override
public void init() {
    LOGGER.info("Initializing TableNamingCheck and subscribing to CREATE_TABLE");
    subscribeTo(DdlGrammar.CREATE_TABLE);
}
```

- The `init` method subscribes the check to `CREATE_TABLE` nodes in the Abstract Syntax Tree (AST) representing the PL/SQL code. The rule will be applied when a `CREATE_TABLE` statement is encountered.

### Node Visiting Method

```java
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
```

- The `visitNode` method is called when a `CREATE_TABLE` node is visited. It retrieves the table name and checks it against the defined pattern.
- If the table name does not match the pattern, a warning is logged, and an issue is added to the node, indicating that the table name does not follow the naming conventions.
- If the table name matches the pattern, an informational message is logged, indicating that the table name follows the naming conventions.

## Summary

The `TableNamingCheck` class is a custom rule class designed to enforce a specific naming convention for table names in PL/SQL code by leveraging the SonarQube API. It logs relevant information and warnings, and adds issues to the AST nodes that violate the naming conventions.
