package com.gutoveronezi.compiler.compiler;

import com.gutoveronezi.compiler.exceptions.InvalidStateException;
import com.gutoveronezi.compiler.models.Token;
import com.gutoveronezi.compiler.utils.ConsoleUtils;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

public class SintacticalAnalyzer {

    private final ConsoleUtils console;

    public SintacticalAnalyzer(ConsoleUtils console) {
        this.console = console;
    }

    public void analyze(LinkedList<Token> tokenList) {
        Stack<Token> tokenStack = parseTokenListToReverseStack(tokenList);

        if (tokenStack.isEmpty()) {
            throw new InvalidStateException("There are no tokens to analyze.");
        }
    }

    private Stack<Token> parseTokenListToReverseStack(LinkedList<Token> tokenList) {
        Stack<Token> tokenStack = new Stack<>();
        Collections.reverse(tokenList);

        for (Token token : tokenList) {
            tokenStack.add(token);
        }

        return tokenStack;
    }
}
