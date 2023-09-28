# Test File Explanation

This test file contains a series of SQL `CREATE TABLE` statements that are designed to test the `TableNamingCheck` class. The statements are divided into two sections, representing valid and invalid table names according to the naming conventions enforced by the rule.

## Valid Table Names

The first section of the test file contains statements with valid table names according to the given specifications:

```sql
CREATE TABLE APP_DESCRIPTIVE_TN (
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(50)
);
```

- This table name follows the naming convention, having a prefix `APP_` and a descriptive part followed by `_TN`.

```sql
CREATE TABLE XY_SECURED_TABLE_TN (
    ID NUMBER PRIMARY KEY,
    DESCRIPTION VARCHAR2(100)
);
```

- Another valid name with a different prefix `XY_` and descriptive part followed by `_TABLE_TN`.

```sql
CREATE TABLE TEMPORARY_GTT (
    ID NUMBER PRIMARY KEY,
    TEMP_DATA VARCHAR2(100) NOT NULL
);
```

- This table name ends with `_GTT`, marking it as an exception to the naming conventions and therefore valid.

```sql
CREATE TABLE MV_MATERIALIZED_MV (
    ID NUMBER PRIMARY KEY,
    MATERIALIZED_DATA VARCHAR2(100) NOT NULL
);
```

- This table name ends with `_MV`, another exception to the naming conventions, making it valid.

```sql
CREATE TABLE TMP_TEMPORARY_TMP (
    ID NUMBER PRIMARY KEY,
    TEMP_DATA VARCHAR2(100) NOT NULL
);
```

- Starts with `TMP_` and ends with `_TMP`, both are exceptions, hence valid.

```sql
CREATE TABLE DATA_TEMPORARY_TEMP (
    ID NUMBER PRIMARY KEY,
    TEMP_DATA VARCHAR2(100) NOT NULL
);
```

- Ends with `_TEMP`, marking it as an exception and therefore valid.

```sql
CREATE TABLE TMP_INVALID_TABLE_TN (
    ID NUMBER PRIMARY KEY,
    TEMP_DATA VARCHAR2(100) NOT NULL
);
```

- Starts with `TMP_`, an exception to the naming conventions, hence valid.

## Invalid Table Names

The second section contains statements with invalid table names, which should raise issues when checked against the `TableNamingCheck` class:

```sql
CREATE TABLE JSR_INVALID_TABLE_TN (  -- Noncompliant {{Table names should not start with JSR or JSD.}}
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(50)
);
```

- Starts with `JSR`, which is explicitly checked and logged as noncompliant in the rule class.

```sql
CREATE TABLE JSD_ANOTHER_INVALID_TABLE_TN (  -- Noncompliant {{Table names should not start with JSR or JSD.}}
    ID NUMBER PRIMARY KEY,
    DESCRIPTION VARCHAR2(100)
);
```

- Starts with `JSD`, another prefix that is checked and logged as noncompliant.

```sql
CREATE TABLE INVALID_NAME (  -- Noncompliant {{Table name does not follow the naming conventions.}}
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(100) NOT NULL
);
```

- This table name doesn’t follow the defined pattern, hence it is noncompliant.

```sql
CREATE TABLE DATA_DESCRIPTION_TABLE (  -- Noncompliant {{Table name does not follow the naming conventions.}}
    ID NUMBER PRIMARY KEY,
    DESCRIPTION VARCHAR2(100) NOT NULL
);
```

- Another example of a table name that doesn’t match the defined pattern, hence noncompliant.

## Conclusion

This test file is designed to validate the `TableNamingCheck` class by creating tables with both valid and invalid names according to the rule’s naming conventions and exceptions, and expecting the rule to raise issues for the noncompliant table names.
