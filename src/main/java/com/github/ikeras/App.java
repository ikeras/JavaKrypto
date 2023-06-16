package com.github.ikeras;

import java.util.Scanner;

import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;

import java.util.Map;
import java.util.HashMap;

public class App {
    public static void main(String[] args) {
        AnsiConsole.systemInstall();

        System.out.println(ansi().fgBrightBlue().a("=========================================================================================").reset());
        System.out.println(ansi().fgBrightBlue().a("""            
            This a math game where the goal is to create an expression using 5 provided numbers
            that evaluates to the goal number. For example, if the goal number is 13 and the
            provided numbers are 2, 6, 4, 5, and 6 then a correct answer would be: 2 + 6 - 5 + 4 + 6.
            The only operators that may be used are +, -, *, /. Order of operations apply but can be
            overridden using parantheses. Good luck and have fun!""").reset());
        System.out.println(ansi().fgBrightBlue().a("=========================================================================================\n").reset());

        try (Scanner scanner = new Scanner(System.in)) {
            String userInput = "";
            int count = 0;
            int solved = 0;

            while (true) {
                count++;
                Problem problem = new Problem(5);
                System.out.println("Problem #" + count);
                System.out.println(ansi().fgBrightYellow().a("Goal: " + problem.getResult()).reset());
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
                            printSuccess("Excellent job! You have solved this correctly!");
                        } else {
                            printError("Unfortunately, the expected result was " + problem.getResult() + " and your result was " + expression.getResult());
                            printError("A possible solution would have been " + problem.getCalculatedExpression());
                        }
                    } else {
                        printError("Unfortunately, you did not use the correct operands. Expected: ", false);
                        for (int operand : problem.getOperands()) {
                            printError(operand + " ", false);
                        }
                        printError("Used: ", false);
                        for (int operand : expression.getOperands()) {
                            printError(operand + " ", false);
                        }
                        System.out.println();
                    }
                } catch (IllegalArgumentException err) {
                    printError("Unfortunately your expression guess resulted in an error: " + err.getMessage() + ".");
                }

                System.out.println();
            }

            printSuccess("You solved " + solved + " out of " + (count - 1) + " problems!");
        }
    }

    private static void printError(String message, boolean newline) {
        if (newline) message += "\n";
        System.out.print(ansi().fgBrightRed().a(message).reset());
    }

    private static void printError(String message) {
        printError(message, true);
    }

    private static void printSuccess(String message) {
        System.out.println(ansi().fgBrightGreen().a(message).reset());
    }
}
