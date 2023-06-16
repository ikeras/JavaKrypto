package com.github.ikeras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Problem {
    private final List<Integer> operands;
    private double result;
    private String calculatedExpression;

    public Problem(int numOperands) {
        this.operands = new ArrayList<>();

        Random random = new Random();
        int currentValue = random.nextInt(9) + 1;

        StringBuilder calculatedExpressionBuilder = new StringBuilder("((((" + currentValue);

        this.operands.add(currentValue);

        for (int i = 0; i < numOperands - 1; i++) {
            int operand = random.nextInt(9) + 1;

            this.operands.add(operand);

            char symbol;

            int op = random.nextInt(4);

            // case +
            if (op == 0) {
                if (currentValue > 30 && currentValue - operand > 0) {
                    symbol = '-';
                    currentValue -= operand;
                } else {
                    symbol = '+';
                    currentValue += operand;
                }
                // case -
            } else if (op == 1) {
                if (currentValue - operand > 0) {
                    symbol = '-';
                    currentValue -= operand;
                } else {
                    symbol = '+';
                    currentValue += operand;
                }
                // case *
            } else if (op == 2) {
                if (currentValue > 30 && currentValue % operand == 0 && currentValue > operand) {
                    symbol = '/';
                    currentValue /= operand;
                } else if (currentValue > 30 && currentValue - operand > 0) {
                    symbol = '-';
                    currentValue -= operand;
                } else {
                    symbol = '*';
                    currentValue *= operand;
                }
                // case /
            } else {
                if (currentValue % operand == 0 && currentValue > operand) {
                    symbol = '/';
                    currentValue /= operand;
                } else if (currentValue - operand > 0) {
                    symbol = '-';
                    currentValue -= operand;
                } else {
                    symbol = '+';
                    currentValue += operand;
                }
            }

            calculatedExpressionBuilder.append(" ").append(symbol).append(" ").append(operand).append(")");
        }

        this.calculatedExpression = calculatedExpressionBuilder.toString();

        this.result = new MathExpression(this.calculatedExpression).getResult();

        Collections.shuffle(this.operands);
    }

    public String getCalculatedExpression() {
        return this.calculatedExpression;
    }

    public List<Integer> getOperands() {
        return Collections.unmodifiableList(this.operands);
    }

    public double getResult() {
        return this.result;
    }
}