## Flow Chart for TableNamingCheck Class

1. **Start**
   ↓

2. **Initialization**
    - Subscribe to `DdlGrammar.CREATE_TABLE` nodes.
    - Log: Subscribed to `CREATE_TABLE` nodes.
      ↓

3. **Visit CREATE_TABLE Node**
    - Log: Visiting a `CREATE_TABLE` node.
      ↓

4. **Retrieve `UNIT_NAME` Child Node**
    - Node doesn't have a child of type `UNIT_NAME`?
        - Yes: Log Warning and Return.
        - No: Proceed.
          ↓

5. **Get Table Name and Convert to Uppercase**
    - Log: Evaluating table name: `TABLE_NAME`.
      ↓

6. **Check if Table Name Starts with JSR/JSD**
    - Starts with `JSR` or `JSD`?
        - Yes: Log Warning, Add Issue ("Table names should not start with JSR or JSD"), and Return.
        - No: Proceed.
          ↓

7. **Check Table Name Length**
    - Exceeds 30 characters?
        - Yes: Log Warning, Add Issue ("Table name exceeds the maximum allowed length"), and Return.
        - No: Proceed.
          ↓

8. **Validate Table Name Prefix**
    - Has a valid prefix?
        - Yes: Proceed.
        - No: Log Warning, Add Issue ("Table name does not start with a valid prefix"), and Return.
          ↓

9. **Check Naming Conventions**
    - For tables starting with `TMP_`: Must end with `_TEMP` or `_TMP`.
    - For other tables:
        - Match with `TABLE_PATTERN`.
        - OR end with `_GTT`, `_MV`, `_TEMP`, or `_TMP` (without `_TN` suffix).
        - All others must end with `_TN`.

        - Matches?
            - Yes: Log Info ("Table name follows the naming conventions").
            - No: Log Warning and Add Issue ("Table name does not follow the naming conventions").
              ↓

10. **End**
