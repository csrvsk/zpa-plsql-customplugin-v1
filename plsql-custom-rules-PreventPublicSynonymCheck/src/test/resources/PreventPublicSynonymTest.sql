-- PreventPublicSynonymTest.sql


CREATE PUBLIC SYNONYM CMiC_Financials FOR some_schema.financial_table; -- Noncompliant {{PUBLIC SYNONYM for CMiC object found at Line #4.}}


CREATE PUBLIC SYNONYM CMiC_Reports FOR some_schema.report_table; -- Noncompliant {{PUBLIC SYNONYM for CMiC object found at Line #7.}}

-- Compliant: Private synonym, should not trigger a violation
CREATE SYNONYM Local_Financials FOR some_schema.financial_table;


CREATE PUBLIC SYNONYM NonCmiC_Public FOR some_schema.other_table; -- Noncompliant {{PUBLIC SYNONYM for CMiC object found at Line #13.}}
