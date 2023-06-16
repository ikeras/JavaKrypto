package com.github.ikeras;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Scanner implements Iterable<Token> {
    private final String input;
    private final int inputLength;

    public Scanner(String input) {
        this.input = input;
        this.inputLength = input.length();
    }

    @Override
    public Iterator<Token> iterator() {
        return new TokenIterator();
    }

    private class TokenIterator implements Iterator<Token> {        
        private int offset = 0;

        @Override
        public boolean hasNext() {
            while (offset < inputLength && Character.isWhitespace(input.charAt(offset))) {
                offset++;
            }

            return offset < inputLength;
        }

        @Override
        public Token next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            char inputChar = input.charAt(offset);

            switch (inputChar) {
                case '√':
                    offset++;
                    return new Token(TokenKind.SQUARE_ROOT, 3, Associativity.RIGHT);
                case '^':
                    offset++;
                    return new Token(TokenKind.EXPONENTIATION, 3, Associativity.RIGHT);
                case '%':
                    offset++;
                    return new Token(TokenKind.MOD, 2, Associativity.LEFT);
                case '*':
                    offset++;
                    return new Token(TokenKind.MULTIPLY, 2, Associativity.LEFT);
                case '/':
                    offset++;
                    return new Token(TokenKind.DIVIDE, 2, Associativity.LEFT);
                case '+':
                    offset++;
                    return new Token(TokenKind.PLUS, 1, Associativity.LEFT);
                case '-':
                    offset++;
                    return new Token(TokenKind.MINUS, 1, Associativity.LEFT);
                case '(':
                    offset++;
                    return new Token(TokenKind.OPEN_PAREN);
                case ')':
                    offset++;
                    return new Token(TokenKind.CLOSE_PAREN);
                default:
                    if (Character.isDigit(inputChar)) {
                        int value = 0;

                        while (offset < inputLength && Character.isDigit(input.charAt(offset))) {
                            value = value * 10 + Character.getNumericValue(input.charAt(offset));
                            offset++;
                        }

                        return new Token(TokenKind.NUMBER, value);
                    } else {
                        throw new IllegalArgumentException("Unexpected input: " + inputChar);
                    }
            }
        }
    }
}
