-- JSRJSDTableNamesTest.sql

-- This table name contains "JSR" and should trigger a violation
CREATE TABLE JSR_MY_TABLE (id NUMBER); -- Noncompliant {{Table name started with JSR or JSD at Line #4}}

-- This table name is valid and should not trigger any violation
CREATE TABLE VALID_TABLE_1 (id NUMBER);

-- This table name contains "JSD" and should trigger a violation
CREATE TABLE JSD_MY_JSD_TABLE (id NUMBER, name VARCHAR2(50)); -- Noncompliant {{Table name started with JSR or JSD at Line #10}}

-- Another valid table name
CREATE TABLE VALID_TABLE_2 (id NUMBER, description VARCHAR2(200));

-- Yet another valid table name
CREATE TABLE ANOTHER_VALID_TABLE (id NUMBER, value NUMBER);

-- This table name contains "JSR" and should trigger a violation
CREATE TABLE JSR_ANOTHER_TABLE (id NUMBER); -- Noncompliant {{Table name started with JSR or JSD at Line #19}}

--testing table name with schema
CREATE TABLE da.JSD_MY_JSD_TABLE (id NUMBER); -- Noncompliant {{Table name started with JSR or JSD at Line #22}}
