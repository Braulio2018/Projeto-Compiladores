package com.gutoveronezi.compiler.models;

import com.gutoveronezi.compiler.enums.SemanticTokenCategory;
import com.gutoveronezi.compiler.enums.SemanticTokenDatatype;

import java.util.Objects;

public class SemanticToken {
    private Token token;
    private SemanticTokenCategory category;
    private SemanticTokenDatatype datatype;
    private int level;

    public SemanticToken(Token token, SemanticTokenCategory category, SemanticTokenDatatype datatype, int level) {
        this.token = token;
        this.category = category;
        this.datatype = datatype;
        this.level = level;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public SemanticTokenCategory getCategory() {
        return category;
    }

    public void setCategory(SemanticTokenCategory category) {
        this.category = category;
    }

    public SemanticTokenDatatype getDatatype() {
        return datatype;
    }

    public void setDatatype(SemanticTokenDatatype datatype) {
        this.datatype = datatype;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SemanticToken that = (SemanticToken) o;
        return level == that.level && this.getToken().getContent().equalsIgnoreCase(that.getToken().getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(token.getContent(), level);
    }
}
