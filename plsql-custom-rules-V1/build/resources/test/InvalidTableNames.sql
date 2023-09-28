CREATE TABLE INVALID_TABLE_NAME ( -- Noncompliant {{Table name does not follow the naming conventions.}}
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(50)
);

CREATE TABLE zz_invalid ( -- Noncompliant {{Table name does not follow the naming conventions.}}
    ID NUMBER PRIMARY KEY,
    VALUE VARCHAR2(25)
);
