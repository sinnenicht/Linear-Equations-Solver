package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static boolean isRunning = true;
    public static String[] inAndOut = new String[2];

    public static void main(String[] args) {
        int numArgs = args.length;
        int index = 0;
        while (isRunning && index < numArgs) {
            int nextIndex = index + 1;
            if ("-in".equals(args[index])) {
                if (args[nextIndex] != null) {
                    inAndOut[0] = args[nextIndex];
                } else {
                    System.out.println("Error: invalid import file.");
                    isRunning = false;
                }
                index += 1;
            } else if ("-out".equals(args[index])) {
                if (args[nextIndex] != null) {
                    inAndOut[1] = args[nextIndex];
                } else {
                    System.out.println("Error: invalid export file.");
                    isRunning = false;
                }
                index += 1;
            } else {
                index += 1;
            }
        }
        if (isRunning) {
            programRuns(inAndOut[0], inAndOut[1]);
        }
    }

    public static void programRuns(String importFile, String exportFile) {
        Matrix equationSystem = readFromFile(importFile);

        LinearEquationSolver solveEquation = new LinearEquationSolver(equationSystem);

        if (solveEquation.isHasFiniteSolutions()) {
            saveToFile(exportFile, solveEquation.getSolutions());
        } else {
            saveToFile(exportFile, solveEquation.isHasNoSolutions());
        }
    }

    public static Matrix readFromFile(String filePath) {
        File importFile = new File("./" + filePath);
        int numVariables;
        int numEquations;
        Row[] rows = null;

        try (Scanner fileInput = new Scanner(importFile)) {
            numVariables = fileInput.nextInt();
            numEquations = fileInput.nextInt();
            String empty = fileInput.nextLine();
            System.out.println("[" + numVariables + ", " + numEquations + "]");

            rows = new Row[numEquations];
            for (int index = 0; index < numEquations; index++) {
                rows[index] = new Row(fileInput.nextLine(), numVariables + 1);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: file not found.");
            isRunning = false;
        }
        return new Matrix(rows);
    }

    public static void saveToFile(String filePath, ComplexNumber[] solutions) {
        File exportFile = new File("./" + filePath);
        try (PrintWriter writer = new PrintWriter(exportFile)) {
            for (ComplexNumber solution : solutions) {
                writer.println(solution.toString());
            }
            System.out.println("Saved to file " + filePath);
        } catch (IOException e) {
            System.out.println("Error: an IO exception occurs.");
        }
    }

    public static void saveToFile(String filePath, boolean hasNoSolutions) {
        File exportFile = new File("./" + filePath);
        try (PrintWriter writer = new PrintWriter(exportFile)) {
            if (hasNoSolutions) {
                writer.println("No solutions");
            } else {
                writer.println("Infinitely many solutions");
            }
        } catch (IOException e) {
            System.out.println("Error: an IO exception occurs.");
        }
    }
}
