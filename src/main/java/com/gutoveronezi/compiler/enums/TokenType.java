package com.gutoveronezi.compiler.enums;

public enum TokenType {
    BEGIN(6, "begin"),
    CALL(11, "call"),
    CASE(29, "case"),
    CLOSE_BACKET(35, "]"),
    CLOSE_PARENTHESIS(37, ")"),
    COLON(39, ":"),
    COMMA(46, ","),
    CONST(3, "const"),
    DATA_TYPE_ARRAY(9, "array"),
    DATA_TYPE_INTEGER(8, "integer"),
    DO(17, "do"),
    DOT(49, "."),
    DOUBLE_DOT(50, ".."),
    ELSE(15, "else"),
    END(7, "end"),
    FOR(27, "for"),
    GOTO(12, "goto"),
    IDENTIFIER(25),
    IF(13, "if"),
    INITIAL_SYMBOL(51),
    INTERGER_NUMBER(26),
    LABEL(2, "label"),
    LITERAL(48, ""),
    OF(10, "of"),
    OPEN_BRACKET(34, "["),
    OPEN_PARENTHESIS(36, "("),
    OPERATOR_AND(23, "and"),
    OPERATOR_ASSIGN(38, ":="),
    OPERATOR_DIFFERENCE(45, "<>"),
    OPERATOR_DIVIDER(33, "/"),
    OPERATOR_EQUAL(40, "="),
    OPERATOR_GE(42, ">="),
    OPERATOR_GT(41, ">"),
    OPERATOR_LE(44, "<="),
    OPERATOR_LT(43, "<"),
    OPERATOR_MINUS(31, "-"),
    OPERATOR_MULTIPLIER(32, "*"),
    OPERATOR_NOT(24, "not"),
    OPERATOR_OR(22, "or"),
    OPERATOR_PLUS(30, "+"),
    PROCEDURE(5, "procedure"),
    PROGRAM(1, "program"), 
    READLN(20, "readln"),
    REPEAT(18, "repeat"),
    SEMICOLON(47, ";"),
    THEN(14, "then"),
    TO(28, "to"),
    UNTIL(19, "until"),
    VAR(4, "var"),
    WHILE(16, "while"),
    WRITELN(21, "writeln");

    private final int id;
    private final String symbol;

    private TokenType(int id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }
    
    private TokenType(int id) {
        this(id, null);
    }
    public int getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }
}
