-- This is a compliant CREATE VIEW statement
CREATE VIEW DA_MY_VIEW AS SELECT * FROM MY_TABLE;

-- This is a compliant ALTER VIEW statement
ALTER VIEW DA_MY_VIEW AS SELECT * FROM MY_OTHER_TABLE;

-- This is a noncompliant CREATE VIEW statement
CREATE VIEW JSR_MY_VIEW AS SELECT * FROM MY_TABLE; -- Noncompliant {{View name 'JSR_MY_VIEW' is not compliant with naming conventions at Line #8.}}


-- This is a noncompliant ALTER VIEW statement
ALTER VIEW JSD_MY_VIEW AS SELECT * FROM MY_OTHER_TABLE; -- Noncompliant {{View name 'JSD_MY_VIEW' is not compliant with naming conventions at Line #12.}}
