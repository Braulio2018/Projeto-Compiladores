package com.gutoveronezi.compiler.compiler;

import com.gutoveronezi.compiler.enums.SemanticTokenCategory;
import com.gutoveronezi.compiler.enums.SemanticTokenDatatype;
import com.gutoveronezi.compiler.enums.TokenType;
import com.gutoveronezi.compiler.exceptions.InvalidIdentifierException;
import com.gutoveronezi.compiler.models.SemanticToken;
import com.gutoveronezi.compiler.models.Token;
import com.gutoveronezi.compiler.utils.ConsoleUtils;
import com.gutoveronezi.compiler.utils.TokenUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

public class SemanticAnalyzer {
    private ConsoleUtils console;

    public SemanticAnalyzer(ConsoleUtils console) {
        this.console = console;
    }

    public void analyze(LinkedList<Token> userTokenList) {
        console.logInInfo("Starting semantic analysis.");

        Stack<Token> userTokensStack = TokenUtils.parseTokenListToReverseStack(userTokenList);

        //Removes first and second tokens because they are PROGRAM and its name
        userTokensStack.pop();
        userTokensStack.pop();
        SemanticTokenCategory category = null;
        int currentLevel = 0;

        Set<SemanticToken> availableSemanticTokens = new HashSet<>();

        while (!userTokensStack.isEmpty()) {
            Token token = userTokensStack.pop();

            removeScopeSemanticTokensIfEndOfScope(token, availableSemanticTokens, currentLevel);

            currentLevel = getSemanticTokenLevel(currentLevel, token, category);
            category = getSemanticTokenCategory(category, token);

            if (token.getType() == TokenType.IDENTIFIER) {

                if (category != null) {
                    addSemanticTokenToSet(new SemanticToken(token, category, SemanticTokenDatatype.INTEGER, currentLevel), availableSemanticTokens);
                    continue;
                }

                validateUseOfToken(token, availableSemanticTokens);
            }
        }

        console.logInInfo("The semantic analysis passed successfully.");
    }

    private void validateUseOfToken(Token token, Set<SemanticToken> availableSemanticTokens) {
        if (!availableSemanticTokens.stream().anyMatch(semanticToken -> semanticToken.getToken().getContent().equalsIgnoreCase(token.getContent()))) {
            throw new InvalidIdentifierException(String.format("Identifier [%s] at line [%s], starting at index [%s], has not been declared.",
                    token.getContent(), token.getLine(), token.getStartIndex()));
        }
    }

    private void removeScopeSemanticTokensIfEndOfScope(Token token, Set<SemanticToken> availableSemanticTokens, int currentLevel) {
        if (token.getType() != TokenType.END) {
            return;
        }

        availableSemanticTokens.removeIf(semanticToken -> semanticToken.getLevel() == currentLevel);
    }

    private void addSemanticTokenToSet(SemanticToken semanticToken, Set<SemanticToken> semanticTokens) {
        if (semanticTokens.contains(semanticToken)) {
            Token token = semanticToken.getToken();
            throw new InvalidIdentifierException(String.format("Identifier [%s] at line [%s], starting at index [%s], already has been declared in scope level [%s].",
                token.getContent(), token.getLine(), token.getStartIndex(), semanticToken.getLevel()));
        }

        semanticTokens.add(semanticToken);
    }

    private int getSemanticTokenLevel(int level, Token token, SemanticTokenCategory category) {
        if (category == SemanticTokenCategory.PROCEDURE && token.getType() == TokenType.OPEN_PARENTHESIS) {
            return ++level;
        }

        if (token.getType() == TokenType.END) {
            return --level;
        }

        return level;
    }

    private SemanticTokenCategory getSemanticTokenCategory(SemanticTokenCategory category, Token token) {
        if (token.getType() == TokenType.LABEL) {
            return SemanticTokenCategory.LABEL;
        }

        if (token.getType() == TokenType.PROCEDURE) {
            return SemanticTokenCategory.PROCEDURE;
        }

        if (token.getType() == TokenType.VAR) {
            return SemanticTokenCategory.VARIABLE;
        }

        if (token.getType() == TokenType.CONST) {
            return SemanticTokenCategory.CONSTANT;
        }

        if (category == SemanticTokenCategory.PROCEDURE && token.getType() == TokenType.OPEN_PARENTHESIS) {
            return SemanticTokenCategory.PARAM;
        }

        if (token.getType() == TokenType.BEGIN) {
            return null;
        }

        return category;
    }
}
