-- Table names should not start with JSR or JSD
CREATE TABLE JSR_INVALID_TABLE_TN (  -- Noncompliant {{Table names should not start with JSR or JSD.}}
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(50)
);

CREATE TABLE JSD_ANOTHER_INVALID_TABLE_TN (  -- Noncompliant {{Table names should not start with JSR or JSD.}}
    ID NUMBER PRIMARY KEY,
    DESCRIPTION VARCHAR2(100)
);

-- Invalid name because it does not start with a valid prefix and does not follow naming conventions
CREATE TABLE INVALID_NAME (  -- Noncompliant {{Table name does not start with a valid prefix.}}
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(100) NOT NULL
);

-- Invalid name because it does not start with a valid prefix and does not follow naming conventions
CREATE TABLE DATA_DESCRIPTION_TABLE (  -- Noncompliant {{Table name does not start with a valid prefix.}}
    ID NUMBER PRIMARY KEY,
    DESCRIPTION VARCHAR2(100) NOT NULL
);

-- Exceeds the maximum allowed length
CREATE TABLE TMP_INVALID_TABLE_INVALID_TABLE_INVALID_TABLE_TN (  -- Noncompliant {{Table name exceeds the maximum allowed length.}}
    ID NUMBER PRIMARY KEY,
    TEMP_DATA VARCHAR2(100) NOT NULL
);

-- Does not start with a valid prefix but follows naming conventions
CREATE TABLE XYZ_ANOTHER_INVALID_TABLE (  -- Noncompliant {{Table name does not start with a valid prefix.}}
    ID NUMBER PRIMARY KEY,
    DESCRIPTION VARCHAR2(100)
);

-- Does not start with a valid prefix and does not follow naming conventions
CREATE TABLE TEST_WRONG_NAME (  -- Noncompliant {{Table name does not start with a valid prefix.}}
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(100) NOT NULL
);

-- Does not start with a valid prefix and does not follow naming conventions
CREATE TABLE INVALIDNAME (  -- Noncompliant {{Table name does not start with a valid prefix.}}
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(100) NOT NULL
);

-- Exceeds the maximum allowed length
CREATE TABLE INVALID_NAME_WITHOUTPREFIX (  -- Noncompliant {{Table name does not start with a valid prefix.}}
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(100) NOT NULL
);

-- Does not start with a valid prefix and does not follow naming conventions
CREATE TABLE INVALIDNAME_TN (  -- Noncompliant {{Table name does not start with a valid prefix.}}
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(100) NOT NULL
);

-- Does not start with a valid prefix and does not follow naming conventions
CREATE TABLE IN1VALID_NAME (  -- Noncompliant {{Table name does not start with a valid prefix.}}
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(100) NOT NULL
);

-- Does not start with a valid prefix but follows naming conventions
CREATE TABLE XYZ (  -- Noncompliant {{Table name does not start with a valid prefix.}}
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(100) NOT NULL
);

CREATE TABLE TMP_INVALID_ENDINGNAME_ABC ( -- Noncompliant {{Table name does not follow the naming conventions.}}
    ID NUMBER PRIMARY KEY,
    DESCRIPTION VARCHAR2(100)
);

CREATE TABLE TMP_TEMPORARY_GTT (  -- Noncompliant {{Table name does not follow the naming conventions.}}
    ID NUMBER PRIMARY KEY,
    TEMP_DATA VARCHAR2(100) NOT NULL
);
