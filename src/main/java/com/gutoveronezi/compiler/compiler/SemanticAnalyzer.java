package com.gutoveronezi.compiler.compiler;

import com.gutoveronezi.compiler.enums.SemanticTokenCategory;
import com.gutoveronezi.compiler.enums.SemanticTokenDatatype;
import com.gutoveronezi.compiler.enums.TokenType;
import com.gutoveronezi.compiler.exceptions.InvalidIdentifierException;
import com.gutoveronezi.compiler.models.SemanticToken;
import com.gutoveronezi.compiler.models.Token;
import com.gutoveronezi.compiler.parser.TokenParser;
import com.gutoveronezi.compiler.utils.ConsoleUtils;
import com.gutoveronezi.compiler.utils.TokenUtils;

import java.util.*;

public class SemanticAnalyzer {
    private ConsoleUtils console;
    private final int MAX_PROCEDURE_PARAMETERS = 1;

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

        LinkedList<SemanticToken> availableSemanticTokens = new LinkedList<>();
        int qtParameterInProcedure = 0;
        SemanticToken currentProcedure = null;
        List<SemanticToken> tokensToSetType = new ArrayList<>();

        while (!userTokensStack.isEmpty()) {
            Token token = userTokensStack.pop();

            removeScopeSemanticTokensIfEndOfScope(token, availableSemanticTokens, currentLevel);

            currentLevel = getSemanticTokenLevel(currentLevel, token, category);
            category = getSemanticTokenCategory(category, token);

            if (token.getType() == TokenType.IDENTIFIER) {
                if (category != null) {
                    SemanticToken semanticToken = new SemanticToken(token, category, currentLevel);
                    addSemanticTokenToSet(semanticToken, availableSemanticTokens);

                    if (category == SemanticTokenCategory.PROCEDURE) {
                        qtParameterInProcedure = 0;
                        currentProcedure = semanticToken;
                        semanticToken.setDatatype(SemanticTokenDatatype.PROCEDURE);
                    }

                    if (category == SemanticTokenCategory.LABEL) {
                        semanticToken.setDatatype(SemanticTokenDatatype.STRING);
                    }

                    if (category == SemanticTokenCategory.PARAM) {
                        validateMaxProcedureParameters(++qtParameterInProcedure, currentProcedure);
                        currentProcedure.addParameter(semanticToken);
                        semanticToken.setDatatype(SemanticTokenDatatype.INTEGER);
                    }

                    if (category == SemanticTokenCategory.CONSTANT) {
                        semanticToken.setDatatype(SemanticTokenDatatype.INTEGER);
                    }

                    if (category == SemanticTokenCategory.VARIABLE) {
                        tokensToSetType.add(semanticToken);
                    }

                    continue;
                }

                validateUseOfIdentifier(token, availableSemanticTokens, userTokensStack);
            }

            if (category == SemanticTokenCategory.VARIABLE) {
                if (token.getType() == TokenType.DATA_TYPE_INTEGER) {
                    tokensToSetType.forEach(tokenToSetType -> tokenToSetType.setDatatype(SemanticTokenDatatype.INTEGER));
                    tokensToSetType = new ArrayList<>();
                }

                if (token.getType() == TokenType.DATA_TYPE_ARRAY) {
                    tokensToSetType.forEach(tokenToSetType -> tokenToSetType.setDatatype(SemanticTokenDatatype.ARRAY_INTEGER));
                    tokensToSetType = new ArrayList<>();
                }
            }
        }

        console.logInInfo("The semantic analysis passed successfully.");
    }

    private void validateMaxProcedureParameters(int qtParameterInProcedure, SemanticToken currentProcedure) {
        if (qtParameterInProcedure > MAX_PROCEDURE_PARAMETERS) {
            throw new InvalidIdentifierException(String.format("Procedure [%s] cannot have more than [%s] parameters.", currentProcedure.getToken().getContent(),
                    MAX_PROCEDURE_PARAMETERS));
        }
    }

    private void validateUseOfIdentifier(Token token, LinkedList<SemanticToken> availableSemanticTokens, Stack<Token> userTokensStack) {
        validateIfIdentifierWasDeclared(token, availableSemanticTokens);
        validateAssignmentOfValueToIdentifier(token, availableSemanticTokens, userTokensStack);
        validateIfValueBeingAssignedIsTheSameTypeOfIdentifier(token, availableSemanticTokens, userTokensStack);
        validateCallProcedure(token, availableSemanticTokens, userTokensStack);
    }

    private void validateCallProcedure(Token token, LinkedList<SemanticToken> availableSemanticTokens, Stack<Token> userTokensStack) {
        SemanticToken semanticToken = getFirstValidIdentifierDeclaration(availableSemanticTokens, token);
        if (semanticToken.getCategory() != SemanticTokenCategory.PROCEDURE) {
            return;
        }

        int qtParams = 0;

        while (userTokensStack.get(userTokensStack.size()-1).getType() != TokenType.CLOSE_PARENTHESIS) {
            userTokensStack.pop();
            qtParams++;

            if (qtParams > semanticToken.getParameters().size()) {
                throw new InvalidIdentifierException(String.format("Number of parameters informed in procedure [%s], at line [%s], is invalid. Expecting [%s] parameters.", semanticToken.getToken().getContent(),
                        semanticToken.getToken().getLine(), semanticToken.getParameters().size()));
            }

            SemanticTokenDatatype type = getExpressionDatatype(userTokensStack, availableSemanticTokens);
            SemanticToken param = semanticToken.getParameters().get(qtParams - 1);
            if (param.getDatatype() != type) {
                throw new InvalidIdentifierException(String.format("Procedure [%s]'s [#%s] parameter is of type [%s], cannot pass a type [%s] at line [%s].",
                        param.getToken().getContent(), qtParams, param.getDatatype(), type, token.getLine()));
            }

        }

    }

    private void validateIfValueBeingAssignedIsTheSameTypeOfIdentifier(Token token, LinkedList<SemanticToken> availableSemanticTokens, Stack<Token> userTokensStack) {
        Token nextToken = userTokensStack.get(userTokensStack.size() - 1);
        if (nextToken.getType() != TokenType.OPERATOR_ASSIGN) {
            return;
        }

        userTokensStack.pop();

        SemanticTokenDatatype type = getExpressionDatatype(userTokensStack, availableSemanticTokens);
        SemanticToken semanticToken = getFirstValidIdentifierDeclaration(availableSemanticTokens, token);
        if (semanticToken.getDatatype() != type) {
            throw new InvalidIdentifierException(String.format("Unable to assign [%s] to a type [%s] at line [%s].", type, semanticToken.getDatatype(), token.getLine()));
        }
    }

    private SemanticTokenDatatype getExpressionDatatype(Stack<Token> userTokensStack, LinkedList<SemanticToken> availableSemanticTokens) {
        Stack<TokenType> systemTokenTypeStack = new Stack<>();

        systemTokenTypeStack.add(TokenType.EXPRESSAO);
        SemanticTokenDatatype result = null;

        while (!systemTokenTypeStack.isEmpty()) {
            Token userToken = userTokensStack.get(userTokensStack.size() - 1);
            TokenType systemTokenType = systemTokenTypeStack.pop();

            if (systemTokenType.isTerminal()) {
                userTokensStack.pop();
                if (systemTokenType == TokenType.IDENTIFIER || systemTokenType == TokenType.INTERGER_NUMBER || systemTokenType == TokenType.LITERAL) {
                    if (systemTokenType == systemTokenType.IDENTIFIER) {
                        validateIfIdentifierWasDeclared(userToken, availableSemanticTokens);
                    }

                    if (result == null) {
                        if (systemTokenType == TokenType.INTERGER_NUMBER) {
                            result = SemanticTokenDatatype.INTEGER;
                        } else if (systemTokenType == TokenType.LITERAL) {
                            result = SemanticTokenDatatype.STRING;
                        } else {
                            result = getFirstValidIdentifierDeclaration(availableSemanticTokens, userToken).getDatatype();
                        }
                    } else if (systemTokenType != TokenType.INTERGER_NUMBER){
                        SemanticToken semanticToken = getFirstValidIdentifierDeclaration(availableSemanticTokens, userToken);
                        SemanticTokenDatatype semanticTokenDatatype = semanticToken.getDatatype();
                        if (semanticTokenDatatype == null || result != semanticTokenDatatype) {
                            throw new InvalidIdentifierException(String.format("Unable to operate with different types at line [%s] - [%s] and [%s].", userToken.getLine(),
                                    result, semanticTokenDatatype == null ? semanticToken.getCategory() : semanticTokenDatatype));
                        }
                    }
                }
            } else {
                handleNonTerminalToken(systemTokenType, userToken, systemTokenTypeStack);
            }
        }

        return result;
    }

    private void handleNonTerminalToken(TokenType systemTokenType, Token userToken, Stack<TokenType> systemTokenTypeStack) {
        String key = TokenUtils.buildTokenParserKey(systemTokenType, userToken);
        Stack<String> tokenCommands = TokenParser.getTokenCommands(key);

        tokenCommands.forEach(command -> systemTokenTypeStack.add(TokenType.getFromSymbol(command)));
    }

    private void validateAssignmentOfValueToIdentifier(Token token, LinkedList<SemanticToken> availableSemanticTokens, Stack<Token> userTokensStack) {
        Token nextToken = userTokensStack.get(userTokensStack.size() - 1);
        if (nextToken.getType() != TokenType.OPERATOR_ASSIGN) {
            return;
        }

        SemanticToken semanticToken = getFirstValidIdentifierDeclaration(availableSemanticTokens, token);

        if (semanticToken.getCategory() != SemanticTokenCategory.VARIABLE) {
            throw new InvalidIdentifierException(String.format("Identifier [%s] at line [%s], starting at index [%s], is a [%s]. Value assignment can be done only to" +
                    " variables.", token.getContent(), token.getLine(), token.getStartIndex(), semanticToken.getCategory()));
        }
    }

    private void validateIfIdentifierWasDeclared(Token token, LinkedList<SemanticToken> availableSemanticTokens) {
        if (getFirstValidIdentifierDeclaration(availableSemanticTokens, token) == null) {
            throw new InvalidIdentifierException(String.format("Identifier [%s] at line [%s], starting at index [%s], has not been declared.",
                    token.getContent(), token.getLine(), token.getStartIndex()));
        }
    }

    private SemanticToken getFirstValidIdentifierDeclaration(LinkedList<SemanticToken> availableSemanticTokens, Token token) {
        for (int i = availableSemanticTokens.size() - 1; i >= 0; i--) {
            SemanticToken semanticToken = availableSemanticTokens.get(i);
            if (semanticToken.getToken().getContent().equalsIgnoreCase(token.getContent())) {
                return semanticToken;
            }
        }

        return null;
    }

    private void removeScopeSemanticTokensIfEndOfScope(Token token, LinkedList<SemanticToken> availableSemanticTokens, int currentLevel) {
        if (token.getType() != TokenType.END) {
            return;
        }

        availableSemanticTokens.removeIf(semanticToken -> semanticToken.getLevel() == currentLevel);
    }

    private void addSemanticTokenToSet(SemanticToken semanticToken, LinkedList<SemanticToken> semanticTokens) {
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
