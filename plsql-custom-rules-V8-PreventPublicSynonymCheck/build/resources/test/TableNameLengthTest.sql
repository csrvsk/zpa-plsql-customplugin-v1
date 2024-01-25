-- This is a valid table name, so no annotation is needed
CREATE TABLE VALID_TABLE_NAME (id NUMBER);

-- This table name is too long and exceeds the 30 character limit, so it's annotated with an expected error message
CREATE TABLE AN_EXCEPTIONALLY_LONG_TABLE_NAME_EXCEEDING_LIMIT (id NUMBER); -- Noncompliant {{Table name exceeds 30 characters length at Line #5.}}

-- Another table name that's too long
CREATE TABLE ANOTHER_TABLE_NAME_THAT_IS_TOO_LONG_FOR_LIMIT (id NUMBER); -- Noncompliant {{Table name exceeds 30 characters length at Line #8.}}

-- Yet another overly lengthy table name
CREATE TABLE YET_ANOTHER_OVERLY_LENGTHY_TABLE_NAME_HERE (id NUMBER); -- Noncompliant {{Table name exceeds 30 characters length at Line #11.}}

-- This is another valid table name, so no annotation
CREATE TABLE ANOTHER_VALID_NAME (id NUMBER);
