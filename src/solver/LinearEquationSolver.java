package solver;

import java.util.Arrays;

public class LinearEquationSolver {
    private ComplexNumber[] solutions;
    private boolean remainingValuesAreNull;
    private boolean hasFiniteSolutions = true;
    private boolean hasNoSolutions = false;

    public LinearEquationSolver(Matrix matrix) {
        System.out.println("Start solving the equation.");

        Variables variables = new Variables(matrix);

        if (!matrix.checkForEchelonForm()) {
            solveToEchelonForm(matrix, variables);
        }

        if (matrix.checkForNoSolutions()) {
            hasNoSolutions = true;
            hasFiniteSolutions = false;
            System.out.println("No solutions");
        } else if (!matrix.checkForFiniteSolutions()) {
            hasFiniteSolutions = false;
            System.out.println("Infinitely many solutions");
        } else if (!matrix.checkForSolvedForm()) {
            backSolve(matrix);
        }

        if (hasFiniteSolutions) {
            setSolutions(matrix, variables);
        }
    }

    public boolean isHasFiniteSolutions() {
        return hasFiniteSolutions;
    }

    public boolean isHasNoSolutions() {
        return hasNoSolutions;
    }

    private void solveToEchelonForm(Matrix matrix, Variables variables) {
        int numRows = matrix.getNumRows();
        int numInnerColumns = matrix.getNumInnerColumns();
        int zeroesNeededForColumn = numRows - 1;

        for (int columnIndex = 0; columnIndex < numInnerColumns; columnIndex++) {
            if (zeroesNeededForColumn == 0 && !matrix.getElementAt(columnIndex, columnIndex).isZero()) {
                matrix.multiplyMatrixRow(matrix.getRowFromIndex(columnIndex), matrix.getElementAt(columnIndex, columnIndex).getConjugate(), matrix.getElementAt(columnIndex, columnIndex).getNorm());
            }

            while (matrix.findNumZeroesInColBelowDiagonal(columnIndex) < zeroesNeededForColumn && columnIndex < numRows|| !matrix.hasPositiveOneOnDiagonal(columnIndex)) {

                int numLowerOnes = matrix.findNumOnesInColOnDiagonalOrLower(columnIndex);

                if (numLowerOnes > 0) {
                    if (matrix.hasNegativeOneInCol(columnIndex)) {
                        makeOnesPositive(matrix, columnIndex);
                    }

                    if (!matrix.getElementAt(columnIndex, columnIndex).isPositiveOne()) {
                        moveLeadingOne(matrix, columnIndex);
                    }

                    nullifyLowerHalf(matrix, columnIndex);
                } else {
                    if (!matrix.hasNoValues(columnIndex)) {
                        createLeadingOne(matrix, columnIndex);
                    } else {
                        moveNearestNonZeroValue(matrix, columnIndex, variables);
                        if (remainingValuesAreNull) {
                            break;
                        }
                    }
                }
            }

            if (remainingValuesAreNull) {
                break;
            }

            zeroesNeededForColumn -= 1;

            if (columnIndex == numRows - 1) {
                break;
            }
        }
    }

    private void makeOnesPositive(Matrix matrix, int columnIndex) {
        System.out.println("Making ones in column " + columnIndex + " positive.");

        for (int rowIndex = columnIndex; rowIndex < matrix.getNumRows(); rowIndex++) {
            if (matrix.getElementAt(rowIndex, columnIndex).isNegativeOne()) {
                matrix.multiplyMatrixRow(matrix.getRowFromIndex(rowIndex), new ComplexNumber(-1, 0));
            }
        }
    }

    private void moveLeadingOne(Matrix matrix, int columnIndex) {
        System.out.println("Moving a one into the leading one position in column " + columnIndex + ".");

        matrix.switchRows(columnIndex, matrix.findFirstMobileOneInCol(columnIndex));
    }

    private void nullifyLowerHalf(Matrix matrix, int columnIndex) {
        System.out.println("Nullifying values below the main diagonal in column " + columnIndex + ".");

        for (int rowIndex = columnIndex + 1; rowIndex < matrix.getNumRows(); rowIndex++) {
            if (!matrix.getElementAt(rowIndex, columnIndex).isZero()) {
                System.out.println(matrix.getElementAt(rowIndex, columnIndex));
                matrix.subtractMatrixRow(matrix.getRowFromIndex(rowIndex), matrix.getRowFromIndex(columnIndex), matrix.getElementAt(rowIndex, columnIndex));
            }
        }
    }

    private void createLeadingOne(Matrix matrix, int columnIndex) {
        System.out.println("Creating a leading one in column " + columnIndex + ".");

        if (!matrix.getElementAt(columnIndex, columnIndex).isZero()) {
            matrix.multiplyMatrixRow(matrix.getRowFromIndex(columnIndex), matrix.getElementAt(columnIndex, columnIndex).getConjugate(), matrix.getElementAt(columnIndex, columnIndex).getNorm());
        } else {
            for (int rowIndex = columnIndex + 1; rowIndex < matrix.getNumRows(); rowIndex++) {
                if (!matrix.getElementAt(rowIndex, columnIndex).isZero()) {
                    matrix.multiplyMatrixRow(matrix.getRowFromIndex(rowIndex), matrix.getElementAt(rowIndex, columnIndex).getConjugate(), matrix.getElementAt(rowIndex, columnIndex).getNorm());
                    break;
                }
            }
        }
    }

    private void moveNearestNonZeroValue(Matrix matrix, int originalColumnIndex, Variables variables) {
        System.out.println("Trying to move the nearest non-zero value into leading one position in column " + originalColumnIndex + ".");

        int numRows = matrix.getNumRows();
        int columnToSwitchIndex = originalColumnIndex;
        int originalRowIndex = originalColumnIndex + 1;
        int rowToSwitchIndex = originalRowIndex;

        for (int columnIndex = originalRowIndex; columnIndex < numRows; columnIndex++) {
            for (int rowIndex = originalRowIndex; rowIndex < numRows; rowIndex++) {
                if (!matrix.getElementAt(rowIndex, columnIndex).isZero()) {
                    if (rowIndex > originalColumnIndex + 1) {
                        rowToSwitchIndex = rowIndex;
                    }
                    columnToSwitchIndex = columnIndex;
                    break;
                }
            }

            if (columnToSwitchIndex != originalColumnIndex) {
                break;
            }
        }

        if (rowToSwitchIndex != originalRowIndex) {
            matrix.switchRows(originalRowIndex, rowToSwitchIndex);
        }

        if (columnToSwitchIndex != originalColumnIndex) {
            matrix.switchColumns(originalColumnIndex, columnToSwitchIndex, variables);
        }

        if (columnToSwitchIndex == originalColumnIndex) {
            remainingValuesAreNull = true;
        }
    }

    private void backSolve(Matrix matrix) {
        System.out.println("Start back solving.");

        int numRows = matrix.getNumRows();
        int numInnerColumns = matrix.getNumInnerColumns();

        for (int rowIndex = numRows - 2; rowIndex >= 0; rowIndex--) {
            for (int columnIndex = numInnerColumns - 1; columnIndex >= 0; columnIndex--) {
                if (rowIndex != columnIndex && !matrix.getElementAt(rowIndex, columnIndex).isZero()) {
                    matrix.subtractMatrixRow(matrix.getRowFromIndex(rowIndex), matrix.getRowFromIndex(columnIndex), matrix.getElementAt(rowIndex, columnIndex));
                }
            }
        }
    }

    private void setSolutions(Matrix matrix, Variables variables) {
        int numInnerColumns = matrix.getNumInnerColumns();
        ComplexNumber[] solutions = new ComplexNumber[numInnerColumns];
        int[] columnIndexes = variables.getColumnIndexes();
        for (int solutionsIndex = 0; solutionsIndex < numInnerColumns; solutionsIndex++) {
            solutions[solutionsIndex] = matrix.getElementAt(columnIndexes[solutionsIndex], matrix.getNumInnerColumns());
        }

        this.solutions = solutions;
        System.out.println("Solutions: " + Arrays.toString(solutions));
    }

    public ComplexNumber[] getSolutions() {
        return solutions;
    }
}
