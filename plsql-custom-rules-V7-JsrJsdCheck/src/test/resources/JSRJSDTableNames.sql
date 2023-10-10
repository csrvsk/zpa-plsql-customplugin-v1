-- JSRJSDTableNamesTest.sql

-- This table name contains "JSR" and should trigger a violation
CREATE TABLE JSR_MY_TABLE (id NUMBER); -- Noncompliant {{Table names should not start with JSR or JSD.}}

-- This table name is valid and should not trigger any violation
CREATE TABLE VALID_TABLE_1 (id NUMBER);

-- This table name contains "JSD" and should trigger a violation
CREATE TABLE JSD_MY_JSD_TABLE (id NUMBER, name VARCHAR2(50)); -- Noncompliant {{Table names should not start with JSR or JSD.}}

-- Another valid table name
CREATE TABLE VALID_TABLE_2 (id NUMBER, description VARCHAR2(200));

-- Yet another valid table name
CREATE TABLE ANOTHER_VALID_TABLE (id NUMBER, value NUMBER);

-- This table name contains "JSR" and should trigger a violation
CREATE TABLE JSR_ANOTHER_TABLE (id NUMBER); -- Noncompliant {{Table names should not start with JSR or JSD.}}
