package com.gutoveronezi.compiler.compiler;

import com.gutoveronezi.compiler.enums.TokenType;
import com.gutoveronezi.compiler.exceptions.IllegalLiteralException;
import com.gutoveronezi.compiler.exceptions.InvalidIntegerValueException;
import com.gutoveronezi.compiler.models.Token;
import com.gutoveronezi.compiler.utils.ConsoleUtils;
import com.gutoveronezi.compiler.utils.TokenUtils;
import java.util.LinkedList;
import java.util.List;

public class LexicalAnalyzer {

    private final ConsoleUtils console;
    private int line = 1;
    private int lineIndex = 0;
    private int startIndex = 0;
    private int endIndex = 0;
    private String token = "";
    private int index = 0;
    private char[] chars;
    private final LinkedList<Token> tokens = new LinkedList<>();

    public LexicalAnalyzer(ConsoleUtils console) {
        this.console = console;
    }

    public LinkedList<Token> analyze(String code) {
        chars = code.toCharArray();
 
        while (index < chars.length) {
            char ch = chars[index]; 

            if (TokenUtils.isStartOrEndOfLiteral(ch)) {
                readLiteral();
                continue;
            }
            
            if (isIndexOutOfBound()) {
               // verify what is the right behavior
            }

            char nextCh = chars[index + 1];

            if (TokenUtils.isStartOfIntegerValue(ch, nextCh)) {
                readInteger();
                continue;
            }

            if (TokenUtils.isStartOfComment(ch, nextCh)) {
                readComment();
            }
            
        }

        return tokens;
    }

    private void readLiteral() {
        setStartIndex();
        nextIndex();

        while (index < chars.length) {
            char ch = chars[index]; 

            if (TokenUtils.isStartOrEndOfLiteral(ch)) {
                setEndIndex();
                saveToken(TokenType.LITERAL);
                nextIndex();
                return;
            }

            validateLiteralToken();

            if (TokenUtils.isBreakline(ch)) {
                throwUnclosedLiteralException();
            }

            addCharToToken(ch);
            nextIndex();
        }

        throwUnclosedLiteralException();
    }

    private void readComment() {
        nextIndex();
        nextIndex();

        while (index < chars.length) {
            char ch = chars[index]; 

             if (isIndexOutOfBound()) {
               return;
            }

            char nextCh = chars[index + 1];

            if (TokenUtils.isEndOfComment(ch, nextCh)) {
                return;
            }

            nextIndex();
        }
    }

    private void readInteger() {
        setStartIndex();
        char ch = chars[index];

        if (ch == TokenType.OPERATOR_MINUS.getSymbolAsChar()) {
            addCharToToken(ch);
            nextIndex();
        }

        while (index < chars.length && TokenUtils.isIntegerValue(chars[index])) {
            addCharToToken(chars[index]);
            nextIndex();
        }

        setEndIndex();

        int tokenAsInt = Integer.parseInt(token);

        if (!TokenUtils.isValidIntegerValue(tokenAsInt)) {
            throw new InvalidIntegerValueException(String.format("Invalid integer value at line [%s], index [%s]. Value must be within -32767 and 32767", line, lineIndex));
        }

        saveToken(TokenType.INTERGER_NUMBER);
    }

    private void throwUnclosedLiteralException() {
        throw new IllegalLiteralException(String.format("Unclosed string literal at line [%s], index [%s].", line, lineIndex));
    }

    private void setStartIndex() {
        startIndex = lineIndex;
    }

    private void setEndIndex() {
        endIndex = lineIndex;
    }

    private void nextIndex() {
        index++;
        lineIndex++;
    }

    private void validateLiteralToken() {
        if (token.length() > TokenUtils.MAX_LITERAL_LENGTH) {
            throw new IllegalLiteralException(String.format("String literal at line [%s], starting at index [%s], has more than 255 characteres.", line, startIndex));     
        }
    }

    private void saveToken(TokenType type) {
        Token t = new Token();
        t.setStartIndex(startIndex);
        t.setEndIndex(endIndex);
        t.setType(type);
        t.setLine(line);
        t.setContent(token);
        
        tokens.add(t);

        token = "";
    }

    private boolean isIndexOutOfBound() {
        return index >= chars.length - 1;
    }

    private void addCharToToken(char ch) {
        token = String.format("%s%s", token, ch);
    }
}
