-- SchemaValidationTest.sql

-- Define the schema
define cmic_obj_backup = DA

-- Correct usage, matching the defined schema
CREATE TABLE DB.another_table (id NUMBER, description VARCHAR2(255)); -- Noncompliant {{Schema 'DB' does not match the approved schemas at Line #7.}}

-- Incorrect schema
CREATE TABLE DAD.another_table (id NUMBER, description VARCHAR2(255)); -- Noncompliant {{Schema 'DAD' does not match the approved schemas at Line #10.}}

-- Correct usage, but with no schema defined (which is a valid SQL case, but in the context of our rule might be flagged)
CREATE TABLE test_table (id NUMBER, name VARCHAR2(100)); -- Noncompliant {{No schema is being used here at Line #13.}}

-- ALTER TABLE using correct schema
ALTER TABLE DA.my_table ADD email VARCHAR2(255);

-- INSERT INTO with correct schema
INSERT INTO DA.my_table (id, name) VALUES (1, 'Test');

-- INSERT INTO with incorrect schema
--INSERT INTO DAR.another_table (id, description) VALUES (1, 'Test Description'); -- Noncompliant {{Schema 'DAR' does not match the approved schemas at Line #22.}}

-- ALTER TABLE using incorrect schema
--ALTER TABLE DS.just_table ADD phone VARCHAR2(20); -- Noncompliant {{Schema 'DS' does not match the approved schemas at Line #25.}}

-- For no schema provided
CREATE TABLE just_table (id NUMBER, name VARCHAR2(100)); -- Noncompliant {{No schema is being used here at Line #28.}}
ALTER TABLE DB.another_table ADD id NUMBER; -- Noncompliant {{Schema 'DB' does not match the approved schemas at Line #29.}}
ALTER TABLE DB.another_table ADD description VARCHAR2(255); -- Noncompliant {{Schema 'DB' does not match the approved schemas at Line #30.}}

-- DROP TABLE DB.another_table; -- Noncompliant {{Schema 'DB' does not match the approved schemas at Line #32.}}
