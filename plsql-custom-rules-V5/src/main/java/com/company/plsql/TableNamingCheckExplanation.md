# `TableNamingCheck` Class Detailed Explanation

`TableNamingCheck` is a class intended to validate table names in SQL scripts according to specific conventions and rules. Here's a breakdown of its logic:

1. **Initialization**: The class begins by subscribing to `CREATE_TABLE` nodes. This ensures that for each `CREATE_TABLE` statement in a SQL script, the class's `visitNode` method gets called.
    ```java
    @Override
    public void init() {
        subscribeTo(DdlGrammar.CREATE_TABLE);
        LOGGER.info("Subscribed to CREATE_TABLE nodes");
    }
    ```

2. **Prefix Validation**: There's a helper method, `hasValidPrefix`, which is designed to check if the table name starts with any prefix that's specified in the `VALID_PREFIXES` list.
    ```java
    private boolean hasValidPrefix(String tableName) {
        for (String prefix : VALID_PREFIXES) {
            if (tableName.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
    ```

3. **Node Visitor**: The `visitNode` method is where the primary logic of this class resides. Upon encountering a `CREATE_TABLE` node, it carries out a sequence of checks on the table name:

    - It validates if the name starts with "JSR" or "JSD" - both are prohibited prefixes.
    - It checks if the table name exceeds a pre-defined maximum length.
    - It uses the `hasValidPrefix` method to ensure the table name starts with a valid prefix.
    - It validates the table name against the established naming convention using the `matchesNamingConvention` method.
    ```java
    @Override
    public void visitNode(AstNode node) {
        ...
        ...
        if (!matchesNamingConvention(tableName)) {
            LOGGER.warning("Table name does not follow the naming conventions: " + tableName);
            addIssue(tableNameNode.getToken(), "Table name does not follow the naming conventions.");
        }
    }
    ```

4. **Matching Naming Convention**: The `matchesNamingConvention` method checks:
    - If the table name starts with "TMP_", it should end with either "_TEMP" or "_TMP".
    - If not starting with "TMP_", the table name should match a regex pattern or end with "_GTT" or "_MV".
    ```java
    private boolean matchesNamingConvention(String tableName) {
        if (tableName.startsWith("TMP_")) {
            return tableName.endsWith("_TEMP") || tableName.endsWith("_TMP");
        } else {
            return TABLE_PATTERN.matcher(tableName).matches() || tableName.endsWith("_GTT") || tableName.endsWith("_MV");
        }
    }
    ```

This class ensures that all table names in the SQL script adhere to the set conventions and naming patterns.
