package com.company.plsql;
import org.sonar.plugins.plsqlopen.api.CustomPlSqlRulesDefinition;
public class PlSqlCustomRulesDefinition extends CustomPlSqlRulesDefinition {  
    @Override
    public String repositoryName() {
        return "CMiC Custom PLSQL";
    }
    @Override
    public String repositoryKey() {
        return "CMiC-Custom-PLSQL";
    }
    @Override
    public Class[] checkClasses() {
        return new Class[] {
            ForbiddenDmlCheck.class,
			ForbiddenDmlCheck2.class,
			TableNamingCheck.class
        };
    }
}
