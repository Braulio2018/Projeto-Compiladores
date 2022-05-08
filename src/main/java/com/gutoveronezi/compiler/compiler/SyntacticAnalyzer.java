package com.gutoveronezi.compiler.compiler;

import com.gutoveronezi.compiler.enums.TokenType;
import com.gutoveronezi.compiler.exceptions.InvalidStateException;
import com.gutoveronezi.compiler.exceptions.InvalidSyntaxException;
import com.gutoveronezi.compiler.models.Token;
import com.gutoveronezi.compiler.parser.TokenParser;
import com.gutoveronezi.compiler.utils.ConsoleUtils;
import com.gutoveronezi.compiler.utils.TokenUtils;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;
import org.apache.commons.lang3.tuple.Pair;

public class SyntacticAnalyzer {

    private final ConsoleUtils console;
    private Stack<Token> userTokensStack;
    private Stack<TokenType> systemTokenTypeStack;

    public SyntacticAnalyzer(ConsoleUtils console, LinkedList<Token> userTokenList, Stack<TokenType> systemTokenTypeStack) {
        this.console = console;
        setUserTokensStack(userTokenList);
        this.systemTokenTypeStack = systemTokenTypeStack;
    }

    private Stack<Token> parseTokenListToReverseStack(LinkedList<Token> tokenList) {
        Stack<Token> tokenStack = new Stack<>();
        if (tokenList == null) {
            return tokenStack;
        }
        Collections.reverse(tokenList);

        for (Token token : tokenList) {
            tokenStack.add(token);
        }

        return tokenStack;
    }

    public Pair<Stack<TokenType>, Stack<Token>> processNextSystemToken() {
        if (userTokensStack == null) {
            return Pair.of(new Stack<>(), new Stack<>());
        }
 
        if (userTokensStack.isEmpty()) {
            throw new InvalidSyntaxException("User's token list get empty while there are non terminal tokens to validate. The code does not match the language syntax.");
        }

        Token userToken = userTokensStack.get(userTokensStack.size() - 1);
        TokenType systemTokenType = systemTokenTypeStack.pop();

        if (systemTokenType.isTerminal()) {
            handleTerminalToken(systemTokenType, userToken);
        } else {
            handleNonTerminalToken(systemTokenType, userToken);
        }
     
        return Pair.of(systemTokenTypeStack, userTokensStack);
    }

    private void setUserTokensStack(LinkedList<Token> tokenList) {
        Stack<Token> tokenStack = parseTokenListToReverseStack(tokenList);

        if (tokenStack == null || tokenStack.isEmpty()) {
            console.logInDebug("There are no tokens to analyze.");
            return;
        }

        this.userTokensStack = tokenStack;
    }

    private void handleNonTerminalToken(TokenType systemTokenType, Token userToken) {
        String key = TokenUtils.buildTokenParserKey(systemTokenType, userToken);
        Stack<String> tokenCommands = TokenParser.getTokenCommands(key);

        if (tokenCommands == null) {
            throwInvalidStateException(systemTokenType, userToken);
        }

        tokenCommands.forEach(command -> systemTokenTypeStack.add(TokenType.getFromSymbol(command)));
    }

    private void handleTerminalToken(TokenType systemTokenType, Token userToken) {
        if (systemTokenType != userToken.getType()) {
            throwInvalidStateException(systemTokenType, userToken);
        }

        userTokensStack.pop();
    }

    private void throwInvalidStateException(TokenType systemTokenType, Token userToken) throws InvalidStateException {
        throw new InvalidStateException(String.format("Token [%s], at line [%s], starting at index [%s] is not expected. Expecting [%s].", userToken.getType(), userToken.getLine(),
                userToken.getStartIndex(), systemTokenType));
    }
}
