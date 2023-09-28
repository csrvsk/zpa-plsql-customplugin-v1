-- TablenameCheck.sql

-- This table name should pass the check as it adheres to the naming convention
CREATE TABLE XX_TABLE_NAME_TN (
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(50)
);

-- This table name should fail the check as it does not adhere to the naming convention
CREATE TABLE INVALID_TABLE_NAME (
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(50)
);

-- Another table name that should pass the check
CREATE TABLE YY_ANOTHER_TABLE_TN (
    ID NUMBER PRIMARY KEY,
    DESCRIPTION VARCHAR2(100)
);

-- Another table name that should fail the check
CREATE TABLE zz_invalid (
    ID NUMBER PRIMARY KEY,
    VALUE VARCHAR2(25)
);

-- Additional table name that should pass the check
CREATE TABLE EMP_TABLE_TN (
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(100) NOT NULL,
    SALARY NUMBER NOT NULL
);
