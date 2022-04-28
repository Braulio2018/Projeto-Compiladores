package com.gutoveronezi.compiler.compiler;

import com.gutoveronezi.compiler.enums.TokenType;
import com.gutoveronezi.compiler.exceptions.InvalidStateException;
import com.gutoveronezi.compiler.models.Token;
import com.gutoveronezi.compiler.parser.TokenParser;
import com.gutoveronezi.compiler.utils.ConsoleUtils;
import com.gutoveronezi.compiler.utils.TokenUtils;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

public class SyntacticAnalyzer {

    private final ConsoleUtils console;
    private Stack<Token> userTokensStack;
    private Stack<Token> systemTokensStack;

    public SyntacticAnalyzer(ConsoleUtils console, LinkedList<Token> userTokensList, Stack<Token> systemTokensStack) {
        this.console = console;
        setUserTokensStack(userTokensList);
        this.systemTokensStack = systemTokensStack;
    }

    private Stack<Token> parseTokenListToReverseStack(LinkedList<Token> tokenList) {
        Stack<Token> tokenStack = new Stack<>();
        Collections.reverse(tokenList);

        for (Token token : tokenList) {
            tokenStack.add(token);
        }

        return tokenStack;
    }

    public void processNextSystemToken() {
        Token userToken = userTokensStack.get(userTokensStack.size() - 1);
        Token systemToken = systemTokensStack.pop();

        if (systemToken.isTerminal()) {
            handleNonTerminalToken(systemToken, userToken);
        }
    }

    private void setUserTokensStack(LinkedList<Token> tokenList) {
        Stack<Token> tokenStack = parseTokenListToReverseStack(tokenList);

        if (tokenStack.isEmpty()) {
            throw new InvalidStateException("There are no tokens to analyze.");
        }

        this.userTokensStack = tokenStack;
    }

    private void handleNonTerminalToken(Token systemToken, Token userToken) {
        String key = TokenUtils.buildTokenParserKey(systemToken, userToken);
        Stack<String> tokenCommands = TokenParser.getTokenCommands(key);

        if (tokenCommands == null) {
            //throw exception here
            return;
        }

        tokenCommands.forEach(command -> {
            systemTokensStack.add(new Token(TokenType.getNonTerminalFromSymbol(command)));
        });
    }
}
