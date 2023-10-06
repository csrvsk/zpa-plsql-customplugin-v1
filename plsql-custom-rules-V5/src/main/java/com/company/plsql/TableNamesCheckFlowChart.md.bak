## Flowchart for `TableNamingCheck` Rule Logic

1. **Initialization**
   → Subscribe to nodes with the `DdlGrammar.CREATE_TABLE` type.
   → Log that the subscription to `CREATE_TABLE` nodes has been made.

2. **Visit Node**
   → When visiting a node of type `CREATE_TABLE`, fetch the table name.
   → If no table name is present (i.e., the node doesn't have a child of type `UNIT_NAME`), log a warning and return.

3. **Table Name Checks**
   → Convert table name to uppercase for uniformity in checks.
   → **JSR/JSD Check:** If the table name starts with `JSR` or `JSD`, add an issue mentioning it should not start with `JSR` or `JSD`, log a warning, and return.
   → **Max Length Check:** If the table name exceeds the defined maximum length (30 characters), add an issue indicating the name is too long, log a warning, and return.
   → **Prefix Check:** Check if the table name starts with one of the valid prefixes. If not, add an issue indicating it does not start with a valid prefix, log a warning, and return.

4. **Naming Convention Check**
   → For tables starting with `TMP_`, they must end with either `_TEMP` or `_TMP`. If they don't, raise an issue.
   → For other tables:
    - Check if they match the provided regex pattern.
    - Check if they end with `_GTT` or `_MV`.
    - If neither condition is met, raise an issue stating the table does not follow the naming conventions.

The above sequence ensures that checks are executed in a manner that helps identify and report non-compliant table names based on the prescribed rule logic.
