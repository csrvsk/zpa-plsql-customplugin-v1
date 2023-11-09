-- Sample SQL for testing data type rules

CREATE TABLE Employees (
    emp_id NUMBER, --Compliant
    emp_name VARCHAR2(255), --Compliant
    emp_birth_date DATE, --Compliant
    emp_notes CLOB --Noncompliant (Specialty datatype found at Line #7.)
);

CREATE TABLE Products (
    product_id NUMBER, --Compliant
    product_name VARCHAR2(100), --Compliant
    product_image BLOB, --Noncompliant (Specialty datatype found)
    release_date TIMESTAMP --Noncompliant (Specialty datatype found at Line #14.)
);

CREATE TABLE Orders (
    order_id RAW(16), --Noncompliant (Unapproved datatype found at Line #18.)
    customer_id NUMBER, --Compliant
    product_id VARCHAR2(10), --Compliant
    order_date DATE --Compliant
);
