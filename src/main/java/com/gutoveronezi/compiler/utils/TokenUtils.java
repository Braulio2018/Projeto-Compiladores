package com.gutoveronezi.compiler.utils;

import com.gutoveronezi.compiler.enums.TokenType;
import com.gutoveronezi.compiler.exceptions.InvalidIdentifierException;
import com.gutoveronezi.compiler.exceptions.InvalidLiteralException;
import com.gutoveronezi.compiler.exceptions.InvalidIntegerValueException;
import com.gutoveronezi.compiler.models.Token;
import java.util.regex.Pattern;

public class TokenUtils {

    public static final int MAX_LITERAL_LENGTH = 255;
    public static final char CARRIAGE_RETURN = '\r';
    public static final char BREAKLINE = '\n';
    public static final char WHITESPACE = ' ';

    public static boolean isDelimiter(char ch) {
        return isWhitespace(ch) || isBreakline(ch) || isSemicolon(ch);
    }

    public static boolean isWhitespace(char ch) {
        return ch == WHITESPACE;
    }

    public static boolean isBreakline(char ch) {
        return ch == BREAKLINE;
    }

    public static boolean isCarriageReturn(char ch) {
        return ch == CARRIAGE_RETURN;
    }

    public static boolean isSemicolon(char ch) {
        return ch == TokenType.SEMICOLON.getSymbolAsChar();
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
        return ch == TokenType.OPERATOR_MULTIPLIER.getSymbolAsChar() && nextCh == TokenType.CLOSE_PARENTHESIS.getSymbolAsChar();
    }

    public static boolean isIntegerValue(char ch) {
        return Character.isDigit(ch);
    }

    public static boolean isValidIntegerValue(int value) {
        return value > Short.MIN_VALUE && value <= Short.MAX_VALUE;
    }

    public static boolean isValidIdentifier(String token) {
        Pattern pattern = Pattern.compile("[a-z][0-9a-z]{0,29}$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(token).matches();
    }

    public static void validateIntegerToken(String token, int line, int startIndex) {
        if (!isValidIntegerValue(Integer.valueOf(token))) {
            throw new InvalidIntegerValueException(String.format("Invalid integer value at line [%s], starting at index [%s]. Value must be within -32767 and 32767", line,
                    startIndex));
        }
        
    }

    public static void validateLiteralToken(String token, int line, int startIndex) {
        if (token != null && token.length() > TokenUtils.MAX_LITERAL_LENGTH) {
            throw new InvalidLiteralException(String.format("String literal at line [%s], starting at index [%s], has more than 255 characteres.", line, startIndex));
        }
    }

    public static void validateIdentifierToken(String token, int line, int startIndex) {
        if (!isValidIdentifier(token)) {
            throw new InvalidIdentifierException(String.format("Identifier at line [%s], starting at index [%s], has [%s] characters. Identifiers must be composed of an alphabetical"
                    + " character followed by alphanumeric characters and have at maximum 30 characters", line, startIndex, token.length()));     
        }
    }

    public static String buildTokenParserKey(Token t1, Token t2) {
        return String.format("%s,%s", t1.getType().getId(), t2.getType().getId());
    }
}
