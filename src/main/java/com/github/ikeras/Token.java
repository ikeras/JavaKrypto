package com.github.ikeras;

public record Token(TokenKind kind, int value, int precedence, Associativity associativity) {
    public Token(TokenKind kind) {
        this(kind, -1, -1, Associativity.NONE);
    }

    public Token(TokenKind kind, int value) {
        this(kind, value, -1, Associativity.NONE);
    }

    public Token(TokenKind kind, int precedence, Associativity associativity) {
        this(kind, -1, precedence, associativity);
    }

    public boolean isOperator() {
        return kind == TokenKind.DIVIDE ||
               kind == TokenKind.MINUS ||
               kind == TokenKind.MOD ||
               kind == TokenKind.EXPONENTIATION ||
               kind == TokenKind.MULTIPLY ||
               kind == TokenKind.PLUS ||
               kind == TokenKind.SQUARE_ROOT;
    }
}