package com.company.plsql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlSqlCustomRulesPluginTest {

    @Test
    public void testRepository() {
        PlSqlCustomRulesDefinition plugin = new PlSqlCustomRulesDefinition();
        assertEquals("CMiC Custom PLSQL", plugin.repositoryName());
        assertEquals("CMiC-Custom-PLSQL", plugin.repositoryKey());
        assertEquals(6, plugin.checkClasses().length);
    }
}
