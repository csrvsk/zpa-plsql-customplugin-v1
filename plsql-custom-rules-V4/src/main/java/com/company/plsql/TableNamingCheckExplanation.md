# TableNamingCheck Class Explanation

This class is a custom rule class designed to enforce specific table naming conventions in PL/SQL.

## Class Definition

```java
public class TableNamingCheck extends PlSqlCheck {
```

- Defines the `TableNamingCheck` class.
- Extends `PlSqlCheck`, a class likely provided by ZPA for creating custom rules.

## Logger Initialization

```java
private final static Logger LOGGER = Logger.getLogger(TableNamingCheck.class.getName());
```

- Initializes a `Logger` instance for logging information, warnings, and errors.

## Table Naming Pattern

```java
private static final Pattern TABLE_PATTERN = Pattern.compile("^([A-Z0-9]{2,3})_([A-Z0-9_]+)(_TN|_TABLE_TN|_TABLE)?$");
```

- Defines a `Pattern` for the valid table naming convention.
- A valid table name starts with 2 to 3 uppercase letters or digits, followed by an underscore, then any combination of uppercase letters, digits, and underscores, optionally ending with `_TN`, `_TABLE_TN`, or `_TABLE`.

## Initialization Method

```java
public void init() {
    subscribeTo(DdlGrammar.CREATE_TABLE);
    LOGGER.info("Subscribed to CREATE_TABLE nodes");
}
```

- Subscribes this rule to `CREATE_TABLE` nodes in the AST.
- The rule will be invoked when a `CREATE_TABLE` statement is encountered.

## Visiting Nodes

```java
public void visitNode(AstNode node) {
```

- Called when visiting a node (specifically, a `CREATE_TABLE` node) in the AST.

### Extracting Table Name

```java
AstNode tableNameNode = node.getFirstChild(PlSqlGrammar.UNIT_NAME);
if (tableNameNode == null) {
    LOGGER.warning("Node does not have a child of type UNIT_NAME");
    return;
}
String tableName = tableNameNode.getTokenOriginalValue().toUpperCase();
```

- Extracts the table name from the node and converts it to uppercase.
- If the node does not have a child of type `UNIT_NAME`, logs a warning and returns.

### Handling Exceptions

```java
if (isException(tableName)) {
    LOGGER.info("Table name follows the naming conventions: " + tableName);
    return;
}
```

- Checks whether the table name is an exception to the naming conventions by calling the `isException` method.
- If it is an exception, logs an info message and returns.

### Checking Naming Conventions

```java
if (TABLE_PATTERN.matcher(tableName).matches()) {
```

- Checks if the table name matches the defined naming conventions.

#### Special Prefixes Check

```java
if (tableName.startsWith("JSR") || tableName.startsWith("JSD")) {
    LOGGER.warning("Table names should not start with JSR or JSD: " + tableName);
    addIssue(node, "Table names should not start with JSR or JSD.");
} else {
    LOGGER.info("Table name follows the naming conventions: " + tableName);
}
```

- Checks for table names starting with `JSR` or `JSD`, logs a warning, and adds an issue if such a prefix is found.

### Invalid Naming Convention

```java
} else {
    LOGGER.warning("Table name does not follow the naming conventions: " + tableName);
    addIssue(node, "Table name does not follow the naming conventions.");
}
```

- If the table name does not match the defined pattern, logs a warning and adds an issue.

## isException Method

```java
private boolean isException(String tableName) {
    return tableName.endsWith("_GTT") || tableName.endsWith("_MV") || tableName.startsWith("TMP_") || tableName.endsWith("_TEMP") || tableName.endsWith("_TMP");
}
```

- Defines certain exceptions to the naming conventions.
- If a table name ends with `_GTT`, `_MV`, `_TEMP`, `_TMP`, or starts with `TMP_`, it is considered an exception.

## Conclusion

This class is designed to enforce specific table naming conventions in PL/SQL, by subscribing to `CREATE_TABLE` statements, analyzing table names, and adding issues to the code when the naming conventions are not met, with some exceptions.
