-- SchemaValidationTest.sql

-- Define the schema
define cmic_obj_backup = DA

-- Correct usage, matching the defined schema
CREATE TABLE DB.another_table (id NUMBER, description VARCHAR2(255)); -- Noncompliant {{The schema 'DB' does not match the defined schema 'DA' at Line #7}}

-- Incorrect schema
CREATE TABLE DAR.another_table (id NUMBER, description VARCHAR2(255)); -- Noncompliant {{The schema 'DAR' does not match the defined schema 'DA' at Line #10}}

-- Correct usage, but with no schema defined (which is a valid SQL case, but in the context of our rule might be flagged)
CREATE TABLE test_table (id NUMBER, name VARCHAR2(100)); -- Noncompliant {{Missing expected schema 'DA' at Line #13}}

-- ALTER TABLE using correct schema
ALTER TABLE DA.my_table ADD email VARCHAR2(255);

-- INSERT INTO with correct schema
INSERT INTO DA.my_table (id, name) VALUES (1, 'Test');

-- INSERT INTO with incorrect schema
--INSERT INTO DAR.another_table (id, description) VALUES (1, 'Test Description'); -- Noncompliant {{The schema 'DAR' does not match the defined schema 'DA' at Line #22}}

-- ALTER TABLE using incorrect schema
--ALTER TABLE DS.just_table ADD phone VARCHAR2(20); -- Noncompliant {{The schema 'DS' does not match the defined schema 'DA' at Line #25}}

-- For no schema provided
CREATE TABLE just_table (id NUMBER, name VARCHAR2(100)); -- Noncompliant {{Missing expected schema 'DA' at Line #28}}
