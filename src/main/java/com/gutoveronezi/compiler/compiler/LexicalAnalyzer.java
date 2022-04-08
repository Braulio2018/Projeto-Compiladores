package com.gutoveronezi.compiler.compiler;

import com.gutoveronezi.compiler.enums.TokenType;
import com.gutoveronezi.compiler.exceptions.InvalidLiteralException;
import com.gutoveronezi.compiler.models.Token;
import com.gutoveronezi.compiler.utils.ConsoleUtils;
import com.gutoveronezi.compiler.utils.TokenUtils;
import java.util.LinkedList;
import org.apache.commons.lang3.StringUtils;

public class LexicalAnalyzer {

    private final ConsoleUtils console;
    private int line = 1;
    private int lineIndex = 0;
    private int startIndex = 0;
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
            while (isIndexWithinBound()) {
                char ch = chars[index]; 
    
                if (TokenUtils.isBreakline(ch)) {
                    nextLine();
                    nextIndex();
                    continue;
                }

                if (TokenUtils.isWhitespace(ch) || TokenUtils.isCarriageReturn(ch)) {
                    nextIndex();
                    continue;
                }

                if (TokenUtils.isStartOrEndOfLiteral(ch)) {
                    readLiteral();
                    continue;
                }
                
                char nextCh = TokenUtils.WHITESPACE;
                if (!isLastIndexOrAfter()) {
                    nextCh = chars[index + 1];
                }

                if (TokenUtils.isStartOfIntegerValue(ch, nextCh)) {
                    readInteger();
                    continue;
                }
   
                if (TokenUtils.isStartOfComment(ch, nextCh)) {
                    readComment();
                    continue;
                }

                readOtherTokenTypes();
            }
    
            return tokens;
        } catch (Exception e) {
            console.logInError(String.format("%s - %s", e.getClass().getSimpleName(), e.getMessage()));
            return null;
        } finally {
            console.logInInfo("Finalizing lexical analysis.");
        }
    }

    private boolean isIndexWithinBound() {
        return index < chars.length;
    }

    private void readLiteral() {
        setStartIndex();
        nextIndex();

        while (isIndexWithinBound()) {
            char ch = chars[index]; 

            if (TokenUtils.isStartOrEndOfLiteral(ch)) {
                saveToken(TokenType.LITERAL);
                nextIndex();
                return;
            }

            if (TokenUtils.isBreakline(ch)) {
                throw new InvalidLiteralException(String.format("String literal at line [%s], index [%s], contains a forbidden breakline.", line, lineIndex));
            }

            addCharToToken(ch);
            TokenUtils.validateLiteralToken(token, line, startIndex);

            nextIndex();
        }

        throw new InvalidLiteralException(String.format("Unclosed string literal at line [%s], index [%s].", line, lineIndex));
    }

    private void readInteger() {
        setStartIndex();
        char ch = chars[index];

        if (ch == TokenType.OPERATOR_MINUS.getSymbolAsChar()) {
            addCharToToken(ch);
            nextIndex();
        }

        while (isIndexWithinBound() && TokenUtils.isIntegerValue(chars[index])) {
            addCharToToken(chars[index]);
            nextIndex();
        }

        previousIndex();

        TokenUtils.validateIntegerToken(token, line, startIndex);
        saveToken(TokenType.INTERGER_NUMBER);
        nextIndex();
    }

    private void readComment() {
        nextIndex();
        nextIndex();

        while (!isLastIndexOrAfter()) {
            char ch = chars[index]; 
            char nextCh = chars[index + 1];

            if (TokenUtils.isEndOfComment(ch, nextCh)) {
                nextIndex();
                nextIndex();
                return;
            }

            if (TokenUtils.isBreakline(ch)) {
                nextLine();
            }

            nextIndex();
        }

        throw new InvalidLiteralException(String.format("Unclosed comment at line [%s], index [%s].", line, lineIndex));
    }

    private void readOtherTokenTypes() {
        setStartIndex();

        while (isIndexWithinBound()) {
            char ch = chars[index];
            if (TokenUtils.isCarriageReturn(ch)) {
                nextIndex();
                continue;
            }

            if (isStartOfComment(ch) || isDelimiter(ch) || isOperatorThatDoesNotNeedWhitespace(ch)) {
                return;
            }

            addCharToToken(ch);
            nextIndex();
        }

        saveToken();
    }

    private boolean isDelimiter(char ch) {
        if (!TokenUtils.isDelimiter(ch)) {
            return false;
        }

        if (TokenUtils.isSemicolon(ch)) {
            previousIndex();
            saveToken();
            nextIndex();
            setStartIndex();
            addCharToToken(ch);
        } else {
            previousIndex();
        }

        saveToken();
        nextIndex();
        return true;
    }

    private boolean isOperatorThatDoesNotNeedWhitespace(char ch) {
        char nextCh = TokenUtils.WHITESPACE;
        if (!isLastIndexOrAfter()) {
            nextCh = chars[index + 1];
        }

        boolean isOperatorWithLength1 = TokenType.isTypeThatDoesNotNeedWhitespace(String.valueOf(ch));
        boolean isOperatorWithLength2 = TokenType.isTypeThatDoesNotNeedWhitespace(String.format("%s%s", ch, nextCh));

        if (isOperatorWithLength1 || isOperatorWithLength2) {
            previousIndex();
            saveToken();
            nextIndex();
            setStartIndex();
            addCharToToken(ch);
            if (isOperatorWithLength2) {
                nextIndex();
                addCharToToken(nextCh);
            }
            saveToken();
            nextIndex();
            return true;
        }

        return false;
    }

    private boolean isStartOfComment(char ch) {
        char nextCh = TokenUtils.WHITESPACE;
        if (!isLastIndexOrAfter()) {
            nextCh = chars[index + 1];
        }

        if (!TokenUtils.isStartOfComment(ch, nextCh)) {
            return false;
        }

        previousIndex();
        saveToken();
        nextIndex();
        return true;
    }

    private void setStartIndex() {
        startIndex = lineIndex;
    }

    private void nextIndex() {
        index++;
        lineIndex++;
    }

    private void previousIndex() {
        index--;
        lineIndex--;
    }

    private void nextLine() {
        line++;
        lineIndex = -1;
    }

    private void saveToken() {
        saveToken(TokenType.getFromSymbol(token));
    }

    private void saveToken(TokenType type) {
        if (StringUtils.isBlank(token)) {
            return;
        }

        if (type == null) {
            type = TokenType.IDENTIFIER;
            TokenUtils.validateIdentifierToken(token, line, startIndex);
        }

        Token t = new Token();
        t.setStartIndex(startIndex);
        t.setEndIndex(lineIndex);
        t.setType(type);
        t.setLine(line);
        t.setContent(token);

        tokens.add(t);

        token = "";
    }

    private boolean isLastIndexOrAfter() {
        return index >= chars.length - 1;
    }

    private void addCharToToken(char ch) {
        token = String.format("%s%s", token, ch);
    }
}
