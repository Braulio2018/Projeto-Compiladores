package com.gutoveronezi.compiler.compiler;

import com.gutoveronezi.compiler.enums.TokenType;
import com.gutoveronezi.compiler.exceptions.IllegalLiteralException;
import com.gutoveronezi.compiler.exceptions.InvalidIntegerValueException;
import com.gutoveronezi.compiler.models.Token;
import com.gutoveronezi.compiler.utils.ConsoleUtils;
import com.gutoveronezi.compiler.utils.TokenUtils;
import java.util.LinkedList;

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
        console.logInInfo("Starting lexical analysis...");
        chars = code.toCharArray();
 
        try {
            while (index < chars.length) {
                char ch = chars[index]; 
    
                if (TokenUtils.isBreakline(ch)) {
                    saveToken();
                    nextLine();
                    continue;
                }
    
                if (TokenUtils.isWhitespace(ch)) {
                    saveToken();
                    continue;
                }
    
                if (TokenUtils.isStartOrEndOfLiteral(ch)) {
                    saveToken();
                    readLiteral();
                    continue;
                }
    
                char nextCh = '\n';
                if (!isLastIndex()) {
                    nextCh = chars[index + 1];
                }
    
                if (TokenUtils.isStartOfIntegerValue(ch, nextCh)) {
                    saveToken();
                    readInteger();
                    continue;
                }
    
                if (TokenUtils.isStartOfComment(ch, nextCh)) {
                    saveToken();
                    readComment();
                    continue;
                }
    
                if (TokenType.isTypeThatDoesNotNeedWhitespace(String.format("%s%s", ch, nextCh)) || TokenType.isTypeThatDoesNotNeedWhitespace(String.valueOf(ch))) {
                    saveToken();
                    addCharToToken(ch);
                    if (TokenType.isTypeThatDoesNotNeedWhitespace(String.valueOf(ch)) || TokenType.isTypeThatDoesNotNeedWhitespace(token)) {
                        saveToken();
                    }
 
                    if (TokenUtils.isDelimiter(nextCh)) {
                        nextIndex();
                    }
                    continue;
                } else {
                    addCharToToken(ch);
                }

    
                nextIndex();
            }
    
            saveToken();
            return tokens;
        } finally {
            console.logInInfo("Finalizing lexical analysis.");
        }
    }

    private void readLiteral() {
        nextIndex();

        while (index < chars.length) {
            char ch = chars[index]; 

            if (TokenUtils.isStartOrEndOfLiteral(ch)) {
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

             if (isLastIndex()) {
               return;
            }

            char nextCh = chars[index + 1];

            if (TokenUtils.isEndOfComment(ch, nextCh)) {
                nextIndex();
                nextIndex();
                return;
            }

            nextIndex();
        }
    }

    private void readInteger() {
        char ch = chars[index];

        if (ch == TokenType.OPERATOR_MINUS.getSymbolAsChar()) {
            addCharToToken(ch);
            nextIndex();
        }

        while (index < chars.length && TokenUtils.isIntegerValue(chars[index])) {
            addCharToToken(chars[index]);
            nextIndex();
        }

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

    private void nextLine() {
        line++;
        lineIndex = 0;
    }

    private void validateLiteralToken() {
        //validate according language manual
        if (token != null && token.length() > TokenUtils.MAX_LITERAL_LENGTH) {
            throw new IllegalLiteralException(String.format("String literal at line [%s], starting at index [%s], has more than 255 characteres.", line, startIndex));     
        }
    }

    private void saveToken() {
        saveToken(TokenType.getFromSymbol(token));
    }

    private void saveToken(TokenType type) {
        if (token == null) {
            setStartIndex();
            return;
        }

        setEndIndex();
        Token t = new Token();
        t.setStartIndex(startIndex);
        t.setEndIndex(index >= chars.length - 1 ? endIndex - 1 : endIndex);
        t.setType(type == null ? TokenType.IDENTIFIER : type);
        t.setLine(line);
        t.setContent(token);
        
        tokens.add(t);

        token = null;
        nextIndex();
        setStartIndex();
    }

    private boolean isLastIndex() {
        return index >= chars.length - 1;
    }

    private void addCharToToken(char ch) {
        if (token == null) {
            token = "";
        }
        token = String.format("%s%s", token, ch);
    }
}
