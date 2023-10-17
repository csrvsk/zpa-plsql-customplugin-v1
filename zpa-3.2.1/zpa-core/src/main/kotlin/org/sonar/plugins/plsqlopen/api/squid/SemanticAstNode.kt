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
package org.sonar.plugins.plsqlopen.api.squid

import com.felipebz.flr.api.AstNode
import org.sonar.plsqlopen.sslr.PlSqlGrammarBuilder
import org.sonar.plsqlopen.sslr.Tree
import org.sonar.plsqlopen.sslr.TreeImpl

import org.sonar.plugins.plsqlopen.api.symbols.PlSqlType
import org.sonar.plugins.plsqlopen.api.symbols.Symbol
import org.sonar.plugins.plsqlopen.api.symbols.datatype.PlSqlDatatype
import org.sonar.plugins.plsqlopen.api.symbols.datatype.UnknownDatatype

class SemanticAstNode(private val astNode: AstNode) : AstNode(astNode.type, astNode.name, astNode.token) {

    var symbol: Symbol? = null
        set(symbol) {
            field = symbol

            val child = if (children.count() == 1) children[0] else return
            (child as SemanticAstNode).symbol = symbol
        }

    var plSqlDatatype: PlSqlDatatype? = null
        get() = this.symbol?.datatype ?: field

    val plSqlType: PlSqlType?
        get() = plSqlDatatype?.type

    val tree: Tree by lazy {
        var type = PlSqlGrammarBuilder.classForType(astNode.type)
        if (type == TreeImpl::class.java) {
            var node = astNode
            while (type == TreeImpl::class.java && node.numberOfChildren == 1) {
                node = node.firstChild
                type = PlSqlGrammarBuilder.classForType(node.type)
            }
        }

        type.getDeclaredConstructor(SemanticAstNode::class.java)
            .newInstance(this)
    }

    val allTokensToString: String by lazy {
        tokens.joinToString(" ") { it.originalValue }
    }

    override fun toString(): String {
        return super.toString() + if (plSqlDatatype != null && plSqlDatatype !is UnknownDatatype) {
            " datatype=$plSqlDatatype"
        } else {
            ""
        }
    }

    init {
        super.fromIndex = astNode.fromIndex
        super.toIndex = astNode.toIndex
    }
}
