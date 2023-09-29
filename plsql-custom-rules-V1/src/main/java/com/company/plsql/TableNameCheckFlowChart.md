## Flow Chart for TableNamingCheck Class

1. **Start**
   ↓

2. **Initialization**
   - Log: Initializing TableNamingCheck and subscribing to CREATE_TABLE
   - Subscribe to DdlGrammar.CREATE_TABLE nodes
   ↓

3. **Visit CREATE_TABLE Node**
   - Log: Visiting a node
   ↓

4. **Retrieve UNIT_NAME Child Node**
   - No UNIT_NAME Child Node?
     - Yes: Log Info and Return
     - No: Proceed
   ↓

5. **Get Table Name and Convert to Uppercase**
   - Log: Evaluating table name: TABLE_NAME
   ↓

6. **Match Table Name against TABLE_PATTERN**
   - Matches?
     - Yes: Log Info (Table name follows the naming conventions)
     - No: Log Warning and Add Issue (Table name does not follow the naming conventions)
   ↓

7. **End**
