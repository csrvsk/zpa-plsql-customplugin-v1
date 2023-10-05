# Creating Custom Rules with ZPA PLSQLplugin

When creating custom rules for ZPA PLSQLplugin, you'll be working with a variety of grammars that help process different SQL operations. This guide will give you a step-by-step process and a deep dive into the different grammars you can leverage.

## **1. Introduction**

To create a custom rule for the ZPA PLSQLplugin, it's vital to understand the SQL language's structure, and the ZPA's PLSQLplugin architecture. With this understanding, you can determine what part of the SQL language you want your rule to target.

## **2. Set Up**

- Make sure you have the ZPA PLSQLplugin project set up and available.
- Inside the project, focus on the `plsql-custom-rules` submodule, where custom rules are written.

## **3. Decide On What The Rule Targets**

Your rule can target:
- **DML (Data Manipulation Language)**: Statements like `SELECT`, `INSERT`, `UPDATE`.
- **DDL (Data Definition Language)**: Operations like `CREATE TABLE`, `ALTER`.
- **TCL (Transaction Control Language)**: Commands like `COMMIT`, `ROLLBACK`.
- **SQL Plus Commands**: Commands specific to Oracle's SQLPlus tool.
- Other SQL operations and structures.

## **4. Writing The Rule**

### **a. Extend the Base Rule Class**

Your custom rule will often extend a base rule class:

```java
public class YourNewRule extends PlSqlCheck { 
    // Your rule logic here
}
```

### **b. Subscribe To The Necessary Grammars**

Depending on your rule's target, you will subscribe to the relevant grammars:

```java
@Override
public void init() {
    if(targeting DML) {
        subscribeTo(DmlGrammar.SELECT_EXPRESSION);
    }
    else if(targeting DDL) {
        subscribeTo(DdlGrammar.CREATE_TABLE);
    }
    // Add other conditions for other grammars
}
```

### **c. Implement Your Rule's Logic**

Use the `visitNode` method to provide the logic of your rule:

```java
@Override
public void visitNode(AstNode node) {
    // Logic to analyze and evaluate the SQL code
}
```

Use helper methods and properties, such as `getFirstChild()`, to navigate the AST (Abstract Syntax Tree) of the SQL code.

## **5. Testing Your Rule**

Thoroughly test your rule:

- Create **Positive Test Cases**: SQL code snippets where your rule should identify an issue.
- Create **Negative Test Cases**: SQL code snippets where your rule should not identify an issue.

## **6. Documentation**

Document your rule. This documentation should provide:

- **Purpose**: The rationale behind your rule.
- **Examples**: Give SQL code examples to show when your rule applies and when it doesn't.
- **Parameters**: If your rule has parameters, explain each one.

By following this guide and understanding the different SQL grammars, you can create effective and precise custom rules for the ZPA PLSQLplugin.
