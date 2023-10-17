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
package org.sonar.plsqlopen.metrics

import com.felipebz.flr.api.AstNode
import com.felipebz.flr.api.Token
import com.felipebz.flr.api.Trivia
import org.sonar.plugins.plsqlopen.api.PlSqlGrammar
import org.sonar.plugins.plsqlopen.api.checks.PlSqlCheck
import org.sonar.plugins.plsqlopen.api.squid.PlSqlCommentAnalyzer

class MetricsVisitor : PlSqlCheck() {

    var numberOfStatements: Int = 0
        private set
    private val linesOfCode = HashSet<Int>()
    private val linesOfComments = HashSet<Int>()
    private val noSonar = HashSet<Int>()
    private val executableLines = HashSet<Int>()

    val linesWithNoSonar: Set<Int>
        get() = noSonar

    override fun init() {
        subscribeTo(PlSqlGrammar.STATEMENT)
    }

    override fun visitNode(node: AstNode) {
        if (node.hasDirectChildren(PlSqlGrammar.BLOCK_STATEMENT))
            return

        numberOfStatements++
        executableLines.add(node.tokenLine)
    }

    override fun visitToken(token: Token) {
        for (line in token.line .. token.endLine) {
            linesOfCode.add(line)
        }
    }

    override fun visitComment(trivia: Trivia, content: String) {
        var line = trivia.token.line

        for (commentLine in content.lineSequence()) {
            if (commentLine.contains("NOSONAR")) {
                linesOfComments.remove(line)
                noSonar.add(line)
            } else if (!PlSqlCommentAnalyzer.isBlank(commentLine)) {
                linesOfComments.add(line)
            }
            line++
        }
    }

    fun getLinesOfCode(): Set<Int> = linesOfCode

    fun getLinesOfComments(): Set<Int> = linesOfComments

    fun getExecutableLines(): Set<Int> = executableLines

}
