-- Compliant examples
CREATE TABLE AP_MY_TABLE_TN (id NUMBER);
CREATE TABLE TMP_MY_TABLE_TEMP (id NUMBER);

-- Non-compliant examples

-- Violates Step-1 (invalid prefix)
CREATE TABLE INVALIDPREFIX_MY_TABLE_TN (id NUMBER); -- Noncompliant {{Invalid table name prefix at Line #8.}}

-- Violates Step-1.1 (invalid suffix for valid prefix)
CREATE TABLE AP_MY_TABLE_INVALIDSUFFIX (id NUMBER); -- Noncompliant {{Invalid table name suffix at Line #11.}}

-- Violates Step-2 (invalid TMP prefix)
CREATE TABLE INVALIDTMP_MY_TABLE_TMP (id NUMBER); -- Noncompliant {{Invalid table name prefix at Line #14.}}

-- Violates Step-2.1 (invalid TMP suffix)
CREATE TABLE TMP_MY_TABLE_INVALID (id NUMBER); -- Noncompliant {{Invalid TMP suffix for the table name at Line #17.}}
