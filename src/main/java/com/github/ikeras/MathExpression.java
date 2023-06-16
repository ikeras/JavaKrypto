package com.github.ikeras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class MathExpression {
    private final Scanner scanner;
    private List<Integer> operands;
    private double result;
    private Queue<Token> postFixTokens;

    public MathExpression(String input) {
        this.scanner = new Scanner(input);
        this.operands = new ArrayList<>();
        this.postFixTokens = convertToPostfix();
        this.result = evaluate();
    }

    public List<Integer> getOperands() {
        return Collections.unmodifiableList(this.operands);
    }

    public double getResult() {
        return this.result;
    }    

    // Convert to postfix (reverse polish notation) using the shunting yard algorithm
    // http://en.wikipedia.org/wiki/Shunting-yard_algorithm
    private Queue<Token> convertToPostfix() {
        Queue<Token> output = new java.util.LinkedList<>();
        Stack<Token> operators = new Stack<>();

        for (Token token : this.scanner) {
            if (token.kind() == TokenKind.NUMBER) {
                output.add(token);
                this.operands.add(token.value());
            } else if (token.isOperator()) {
                while (operators.size() > 0) {
                    Token top = operators.peek();

                    if (((token.associativity() == Associativity.LEFT) && (token.precedence() <= top.precedence())) ||
                            ((token.associativity() == Associativity.RIGHT)
                                    && (token.precedence() < top.precedence()))) {
                        output.add(operators.pop());
                    } else {
                        break;
                    }
                }

                operators.push(token);
            } else if (token.kind() == TokenKind.OPEN_PAREN) {
                operators.push(token);
            } else if (token.kind() == TokenKind.CLOSE_PAREN) {
                boolean openParenFound = false;

                while (!openParenFound && operators.size() > 0) {
                    Token top = operators.pop();

                    if (top.kind() == TokenKind.OPEN_PAREN) {
                        openParenFound = true;
                    } else {
                        output.add(top);
                    }
                }

                if (!openParenFound) {
                    throw new IllegalArgumentException("Found extra closing parentheses");
                }
            }
        }

        while (operators.size() > 0) {
            Token top = operators.pop();

            if (top.kind() == TokenKind.OPEN_PAREN || top.kind() == TokenKind.CLOSE_PAREN) {
                throw new IllegalArgumentException("Found extra parentheses");
            }

            output.add(top);
        }

        return output;
    }

    public double evaluate() {
        Stack<Double> stack = new Stack<>();

        for (Token token : this.postFixTokens) {
            if (token.kind() == TokenKind.NUMBER) {
                stack.push((double)token.value());
            } else if (token.kind() == TokenKind.SQUARE_ROOT) {
                if (stack.size() < 1) {
                    throw new IllegalArgumentException("Not enough operands for operator " + token.kind());
                }

                double right = stack.pop();

                stack.push(Math.sqrt(right));
            } else if (token.isOperator()) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Not enough operands for operator " + token.kind());
                }

                double right = stack.pop();
                double left = stack.pop();

                switch (token.kind()) {
                    case PLUS:
                        stack.push(left + right);
                        break;
                    case MINUS:
                        stack.push(left - right);
                        break;
                    case MOD:
                        stack.push(left % right);
                        break;
                    case MULTIPLY:
                        stack.push(left * right);
                        break;
                    case DIVIDE:
                        stack.push(left / right);
                        break;
                    case EXPONENTIATION:
                        stack.push(Math.pow(left, right));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown operator " + token.kind());
                }
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }

        return stack.pop();
    }
}
