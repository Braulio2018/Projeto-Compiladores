package com.gutoveronezi.compiler.enums;

import java.util.Set;

import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.ObjectUtils;

public enum TokenType {
    PROGRAM(1, "program", true), 
    LABEL(2, "label", true),
    CONST(3, "const", true),
    VAR(4, "var", true),
    PROCEDURE(5, "procedure", true),
    BEGIN(6, "begin", true),
    END(7, "end", true),
    DATA_TYPE_INTEGER(8, "integer", true),
    DATA_TYPE_ARRAY(9, "array", true),
    OF(10, "of", true),
    CALL(11, "call", true),
    GOTO(12, "goto", true),
    IF(13, "if", true),
    THEN(14, "then", true),
    ELSE(15, "else", true),
    WHILE(16, "while", true),
    DO(17, "do", true),
    REPEAT(18, "repeat", true),
    UNTIL(19, "until", true),
    READLN(20, "readln", true),
    WRITELN(21, "writeln", true),
    OPERATOR_OR(22, "or", true),
    OPERATOR_AND(23, "and", true),
    OPERATOR_NOT(24, "not", true),
    IDENTIFIER(25),
    INTERGER_NUMBER(26),
    FOR(27, "for", true),
    TO(28, "to", true),
    CASE(29, "case", true),
    OPERATOR_PLUS(30, "+", true),
    OPERATOR_MINUS(31, "-", true),
    OPERATOR_MULTIPLIER(32, "*", true),
    OPERATOR_DIVIDER(33, "/", true),
    OPEN_BRACKET(34, "[", true),
    CLOSE_BACKET(35, "]", true),
    OPEN_PARENTHESIS(36, "(", true),
    CLOSE_PARENTHESIS(37, ")", true),
    OPERATOR_ASSIGN(38, ":=", true),
    COLON(39, ":", true),
    OPERATOR_EQUAL(40, "=", true),
    OPERATOR_GT(41, ">", true),
    OPERATOR_GE(42, ">=", true),
    OPERATOR_LT(43, "<", true),
    OPERATOR_LE(44, "<=", true),
    OPERATOR_DIFFERENCE(45, "<>", true),
    COMMA(46, ",", true),
    SEMICOLON(47, ";", true),
    LITERAL(48),
    DOT(49, ".", true),
    DOUBLE_DOT(50, "..", true),
    INITIAL_SYMBOL(51),
    
    PROGRAMA(52, "PROGRAMA", false),
    BLOCO(53, "BLOCO", false),
    DCLROT(54, "DCLROT", false),
    LID(55, "LID", false),
    REPIDENT(56, "REPIDENT", false),
    DCLCONST(57, "DCLCONST", false),
    LDCONST(58, "LDCONST", false),
    DCLVAR(59, "DCLVAR", false),
    LDVAR(60, "LDVAR", false),
    TIPO(61, "TIPO", false),
    DCLPROC(62, "DCLPROC", false),
    DEFPAR(63, "DEFPAR", false),
    CORPO(64, "CORPO", false),
    REPCOMANDO(65, "REPCOMANDO", false),
    COMANDO(66, "COMANDO", false),
    RCOMID(67, "RCOMID", false),
    RVAR(68, "RVAR", false),
    PARAMETROS(69, "PARAMETROS", false),
    REPPAR(70, "REPPAR", false),
    ELSEPARTE(71, "ELSEPARTE", false),
    VARIAVEL(72, "VARIAVEL", false),
    VARIAVEL1(73, "VARIAVEL1", false),
    REPVARIAVEL(74, "REPVARIAVEL", false),
    ITEMSAIDA(75, "ITEMSAIDA", false),
    REPITEM(76, "REPITEM", false),
    EXPRESSAO(77, "EXPRESSAO", false),
    REPEXPSIMP(78, "REPEXPSIMP", false),
    EXPSIMP(79, "EXPSIMP", false),
    REPEXP(80, "REPEXP", false),
    TERMO(81, "TERMO", false),
    REPTERMO(82, "REPTERMO", false),
    FATOR(83, "FATOR", false),
    CONDCASE(84, "CONDCASE", false),
    CONTCASE(85, "CONTCASE", false),
    RPINTEIRO(86, "RPINTEIRO", false),
    SEMEFEITO(87, "SEMEFEITO", false);

    private final int id;
    private final String symbol;
    private final boolean isTerminal;
    private static final  Set<TokenType> typesThatDoNotNeedWhitespace = Sets.newHashSet(CLOSE_BACKET, CLOSE_PARENTHESIS, COLON, COMMA, DOT, DOUBLE_DOT, OPEN_BRACKET, 
            OPEN_PARENTHESIS, OPERATOR_ASSIGN, OPERATOR_DIFFERENCE, OPERATOR_DIVIDER, OPERATOR_EQUAL, OPERATOR_GE, OPERATOR_GT, OPERATOR_LE, OPERATOR_LT, OPERATOR_MINUS,
            OPERATOR_MULTIPLIER, OPERATOR_PLUS, SEMICOLON);

    private TokenType(int id, String symbol, boolean isTerminal) {
        this.id = id;
        this.symbol = symbol;
        this.isTerminal = isTerminal;
    }
    
    private TokenType(int id) {
        this(id, null, true);
    }
    public int getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public char getSymbolAsChar() {
       return this.symbol.toCharArray()[0];
    }

    public static TokenType getTerminalFromSymbol(String symbol) {
        for (TokenType type : values()) {
            if (type.isTerminal() && ObjectUtils.allNotNull(type.getSymbol(), symbol) && type.getSymbol().equalsIgnoreCase(symbol)) {
                return type;
            }
        }

        return null;
    }

    public static TokenType getNonTerminalFromSymbol(String symbol) {
        for (TokenType type : values()) {
            if (!type.isTerminal() && type.getSymbol().equalsIgnoreCase(symbol)) {
                return type;
            }
        }

        return null;
    }

    public static TokenType getTokenTypeFromId(int id) {
        for (TokenType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        return null;
    }

    public static boolean isTerminalTypeThatDoesNotNeedWhitespace(String symbol) {
        TokenType type = getTerminalFromSymbol(symbol);
        return typesThatDoNotNeedWhitespace.contains(type);
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", id, super.toString(), isTerminal ? "terminal" : "não terminal");
    }
}
