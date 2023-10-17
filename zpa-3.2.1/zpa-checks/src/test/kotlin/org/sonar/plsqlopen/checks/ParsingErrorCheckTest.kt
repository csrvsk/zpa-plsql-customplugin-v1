/**
 * Z PL/SQL Analyzer
 * Copyright (C) 2015-2023 Felipe Zorzo
 * mailto:felipe AT felipezorzo DOT com DOT br
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plsqlopen.checks

import com.felipebz.flr.api.RecognitionException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.plsqlopen.parser.PlSqlParser
import org.sonar.plsqlopen.squid.PlSqlConfiguration
import org.sonar.plugins.plsqlopen.api.PlSqlVisitorContext
import java.io.File
import java.nio.charset.StandardCharsets

class ParsingErrorCheckTest : BaseCheckTest() {

    @Test
    fun test() {
        val file = File("src/test/resources/checks/parsing_error.sql")

        val parser = PlSqlParser.create(PlSqlConfiguration(StandardCharsets.UTF_8))
        val context: PlSqlVisitorContext
        try {
            parser.parse(file)
            throw IllegalStateException("Expected RecognitionException")
        } catch (e: RecognitionException) {
            context = PlSqlVisitorContext(null, e, null)
        }

        val check = ParsingErrorCheck()
        val issues = check.scanFileForIssues(context)
        assertThat(issues).hasSize(1)
        assertThat(issues[0].primaryLocation().startLine()).isEqualTo(1)
    }

    @Test
    fun testTolerantParsing() {
        val file = File("src/test/resources/checks/parsing_error.sql")

        val parser = PlSqlParser.create(PlSqlConfiguration(StandardCharsets.UTF_8, isErrorRecoveryEnabled = true))

        val rootTree = parser.parse(file)
        val context = PlSqlVisitorContext(rootTree, null, null)

        val check = ParsingErrorCheck()
        val issues = check.scanFileForIssues(context)
        assertThat(issues).hasSize(1)
        assertThat(issues[0].primaryLocation().startLine()).isEqualTo(1)
    }

}
