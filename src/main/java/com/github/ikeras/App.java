package com.github.ikeras;

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class App {
    public static void main(String[] args) {
        System.out.println("This a math game where the goal is to create an expression using 5 provided numbers");
        System.out.println("that evaluates to the goal number. For example, if the goal number is 13 and the");
        System.out.println("provided numbers are 2, 6, 4, 5, and 6 then a correct answer would be: 2 + 6 - 5 + 4 + 6.");
        System.out.println("The only operators that may be used are +, -, *, /. Order of operations apply but can be");
        System.out.println("overridden using parantheses. Good luck and have fun!\n");

        try (Scanner scanner = new Scanner(System.in)) {
            String userInput = "";
            int count = 0;
            int solved = 0;

            while (true) {
                count++;
                Problem problem = new Problem(5);
                System.out.println("Problem #" + count);
                System.out.println("Goal: " + problem.getResult());
                System.out.print("Operands: ");
                for (int operand : problem.getOperands()) {
                    System.out.print(operand + " ");
                }
                System.out.println("\n");
                System.out.print("Your expression guess: ");
                userInput = scanner.nextLine();

                if (userInput.equals("exit")) {
                    break;
                }

                try {
                    MathExpression expression = new MathExpression(userInput);

                    Map<Integer, Integer> operandCounts = new HashMap<>();
                    for (int operand : problem.getOperands()) {
                        operandCounts.put(operand, operandCounts.getOrDefault(operand, 0) + 1);
                    }

                    Map<Integer, Integer> expressionOperandCounts = new HashMap<>();
                    for (int operand : expression.getOperands()) {
                        expressionOperandCounts.put(operand, expressionOperandCounts.getOrDefault(operand, 0) + 1);
                    }

                    if (operandCounts.equals(expressionOperandCounts)) {
                        if (expression.getResult() == problem.getResult()) {
                            solved++;
                            System.out.println("Excellent job! You have solved this correctly!");
                        } else {
                            System.out.println("Unfortunately, the expected result was " + problem.getResult() + " and your result was " + expression.getResult());
                            System.out.println("A possible solution would have been " + problem.getCalculatedExpression());
                        }
                    } else {
                        System.out.print("Unfortunately, you did not use the correct operands. Expected: ");
                        for (int operand : problem.getOperands()) {
                            System.out.print(operand + " ");
                        }
                        System.out.print("Used: ");
                        for (int operand : expression.getOperands()) {
                            System.out.print(operand + " ");
                        }
                        System.out.println();
                    }
                } catch (IllegalArgumentException err) {
                    System.out.println("Unfortunately your expression guess resulted in an error: " + err.getMessage() + ".");
                }

                System.out.println();
            }

            System.out.println("You solved " + solved + " out of " + count + " problems!");
        }
    }
}
