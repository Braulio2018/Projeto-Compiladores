package com.gutoveronezi.compiler.utils;

import com.gutoveronezi.compiler.enums.TokenType;

public class TokenUtils {

    public static final int MAX_LITERAL_LENGTH = 255;

    public static boolean isDelimiter(char ch) {
        return isWhitespace(ch) || isBreakline(ch);
    }

    public static boolean isWhitespace (char ch) {
        return ch == ' ';
    }

    public static boolean isBreakline (char ch) {
        return ch == '\n';
    }

    public static boolean isStartOrEndOfLiteral(char ch) {
        return ch == '\'';
    }

    public static boolean isStartOfIntegerValue(char ch, char nextCh) {
        return isIntegerValue(ch) || (ch == TokenType.OPERATOR_MINUS.getSymbolAsChar() && isIntegerValue(nextCh));
    }

    public static boolean isStartOfComment(char ch, char nextCh) {
        return ch == TokenType.OPEN_PARENTHESIS.getSymbolAsChar() && nextCh == TokenType.OPERATOR_MULTIPLIER.getSymbolAsChar();
    }

    public static boolean isEndOfComment(char ch, char nextCh) {
        return ch == TokenType.OPERATOR_MULTIPLIER.getSymbolAsChar() && nextCh == TokenType.OPEN_PARENTHESIS.getSymbolAsChar();
    }

    public static boolean isIntegerValue(char ch) {
        return Character.isDigit(ch);
    }

    public static boolean isValidIntegerValue(int value) {
        return value > Short.MIN_VALUE && value <= Short.MAX_VALUE;
    }
}
