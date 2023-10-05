CREATE TABLE JSR_INVALID_TABLE_TN (  -- Noncompliant {{Table names should not start with JSR or JSD.}}
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(50)
);

CREATE TABLE JSD_ANOTHER_INVALID_TABLE_TN (  -- Noncompliant {{Table names should not start with JSR or JSD.}}
    ID NUMBER PRIMARY KEY,
    DESCRIPTION VARCHAR2(100)
);

CREATE TABLE INVALID_NAME (  -- Noncompliant {{Table name does not follow the naming conventions.}}
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(100) NOT NULL
);

CREATE TABLE DATA_DESCRIPTION_TABLE (  -- Noncompliant {{Table name does not follow the naming conventions.}}
    ID NUMBER PRIMARY KEY,
    DESCRIPTION VARCHAR2(100) NOT NULL
);

-- Table name exceeds the maximum allowed length.
-- CREATE TABLE TMP_INVALID_TABLE_INVALID_TABLE_INVALID_TABLE_TN (  -- Noncompliant {{Table name does not follow the naming conventions.}}
CREATE TABLE TMP_INVALID_TABLE_INVALID_TABLE_INVALID_TABLE_TN (  -- Noncompliant {{Table name exceeds the maximum allowed length.}}
    ID NUMBER PRIMARY KEY,
    TEMP_DATA VARCHAR2(100) NOT NULL
);
