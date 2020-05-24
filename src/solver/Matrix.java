package solver;

import java.util.Arrays;

public class Matrix {
    private Row[] matrix;
    private int numRows;
    private int numColumns;
    private int numInnerColumns;

    public Matrix(Row[] rows) {
        this.matrix = rows;
        this.numRows = rows.length;
        this.numColumns = rows[0].getRow().length;
        this.numInnerColumns = numColumns - 1;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumInnerColumns() {
        return numInnerColumns;
    }

    public Row getRowFromIndex(int rowIndex) {
        return matrix[rowIndex];
    }

    public int getRowIndex(Row row) {
        int rowIndex = numColumns;
        for (int index = 0; index < numRows; index++) {
            if (matrix[index] == row) {
                rowIndex = index;
            }
        }
        return rowIndex;
    }

    public ComplexNumber getElementAt(int rowIndex, int columnIndex) {
        return matrix[rowIndex].getElementAt(columnIndex);
    }

    // Row Operations
    public void switchRows(int row1Index, int row2Index) {
        Row[] rowsToSwitch = new Row[] {matrix[row1Index], matrix[row2Index]};
        matrix[row1Index] = rowsToSwitch[1];
        matrix[row2Index] = rowsToSwitch[0];
        System.out.println("R" + row1Index + " <-> R" + row2Index);
        printMatrix();
    }

    public void switchColumns(int column1Index, int column2Index, Variables variables) {
        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            ComplexNumber[] valuesToSwitch = new ComplexNumber[] {matrix[rowIndex].getElementAt(column1Index), matrix[rowIndex].getElementAt(column2Index)};
            matrix[rowIndex].setElementAt(column1Index, valuesToSwitch[1]);
            matrix[rowIndex].setElementAt(column2Index, valuesToSwitch[0]);
        }
        variables.switchColumnIndexes(column1Index, column2Index);
        System.out.println("C" + column1Index + " <-> C" + column2Index);
        printMatrix();
    }

    public void subtractMatrixRow(Row rowToChange, Row rowToSubtract, ComplexNumber scalar) {
        System.out.print("R" + getRowIndex(rowToChange) + " - " + scalar.toString() + "R" + getRowIndex(rowToSubtract)  + " = ");
        rowToChange.subtractRow(rowToSubtract, scalar);
        System.out.println(Arrays.toString(rowToChange.getRow()));
        printMatrix();
    }

    public void multiplyMatrixRow(Row rowToChange, ComplexNumber scalar) {
        System.out.print("R" + getRowIndex(rowToChange) + " * " + scalar.toString()  + " = ");
        rowToChange.multiplyRow(scalar);
        System.out.println(Arrays.toString(rowToChange.getRow()));
        printMatrix();
    }

    public void multiplyMatrixRow(Row rowToChange, ComplexNumber numerator, double denominator) {
        System.out.print("R" + getRowIndex(rowToChange) + " * " + numerator.toString() + " / " + denominator + " = ");
        rowToChange.multiplyRow(numerator, denominator);
        System.out.println(Arrays.toString(rowToChange.getRow()));
        printMatrix();
    }

    // returns how many zeroes are in a given column below the main diagonal
    public int findNumZeroesInColBelowDiagonal(int columnIndex) {
        int counter = 0;
        if (columnIndex < numRows - 1) { // should this be numInnerColumns?
            for (int rowIndex = columnIndex + 1; rowIndex < numRows; rowIndex++) {
                if (getElementAt(rowIndex, columnIndex).isZero()) {
                    counter += 1;
                }
            }
        }
        return counter;
    }

    // returns true if all values on the diagonal or lower in this column are equal to zero
    public boolean hasNoValues(int columnIndex) {
        int counter = 0;
        if (columnIndex < numRows - 1) {
            for (int rowIndex = columnIndex; rowIndex < numRows; rowIndex++) {
                if (!getElementAt(rowIndex, columnIndex).isZero()) {
                    counter += 1;
                }
            }
        }
        return counter == 0;
    }

    // returns how many ones (positive or negative) are in a given column on the main diagonal or lower
    public int findNumOnesInColOnDiagonalOrLower(int columnIndex) {
        int counter = 0;
        if (hasOneOnDiagonal(columnIndex)) {
            counter += 1;
        }
        counter += findNumOnesInColBelowDiagonal(columnIndex);
        return counter;
    }

    // returns whether column has one (positive or negative) on the main diagonal
    public boolean hasOneOnDiagonal(int columnIndex) {
        return getElementAt(columnIndex, columnIndex).isOne();
    }

    // returns whether column has a positive one on the main diagonal
    public boolean hasPositiveOneOnDiagonal(int columnIndex) {
        return getElementAt(columnIndex, columnIndex).isPositiveOne();
    }

    // returns how many ones (positive or negative) are in a given column below the main diagonal
    public int findNumOnesInColBelowDiagonal(int columnIndex) {
        int counter = 0;
        if (columnIndex < numRows - 1) {
            for (int rowIndex = columnIndex + 1; rowIndex < numRows; rowIndex++) {
                if (getElementAt(rowIndex, columnIndex).isOne()) {
                    counter += 1;
                }
            }
        }
        return counter;
    }

    // returns the first row index below the main diagonal which contains a positive or negative one
    public int findFirstMobileOneInCol(int columnIndex) {
        for (int rowIndex = columnIndex + 1; rowIndex < numRows; rowIndex++) {
            if (getElementAt(rowIndex, columnIndex).isOne()) {
                return rowIndex;
            }
        }
        return 0;
    }

    // returns true if any ones in this column on or below the diagonal are negative
    public boolean hasNegativeOneInCol(int columnIndex) {
        for (int rowIndex = columnIndex; rowIndex < numRows; rowIndex++) {
            if (getElementAt(rowIndex, columnIndex).isNegativeOne()) {
                return true;
            }
        }
        return false;
    }

    // returns true if main diagonal consists entirely of positive ones or zeroes
    public boolean checkMainDiagonal() {
        int counter = 0;
        for (int columnIndex = 0; columnIndex < numRows; columnIndex++) {
            ComplexNumber currentNum = getElementAt(columnIndex, columnIndex);
            if (currentNum.isPositiveOne() || currentNum.isZero()) {
                counter += 1;
            }
        }
        return counter == numRows;
    }

    // returns true if everything below the main diagonal is a zero
    public boolean checkLowerZeroes() {
        int counter = 0;
        int numZeroesNeeded = (int) (numRows * numColumns / 2 - 0.5 * numColumns);
        for (int columnIndex = 0; columnIndex < numRows; columnIndex++) {
            if (columnIndex + 1 < numRows) {
                for (int rowIndex = columnIndex + 1; rowIndex < numRows; rowIndex++) {
                    if (getElementAt(rowIndex, columnIndex).isZero()) {
                        counter += 1;
                    }
                }
            }
        }
        return counter >= numZeroesNeeded;
    }

    // returns true if everything above the main diagonal is a zero
    public boolean checkUpperZeroes() {
        int counter = 0;
        int numZeroesNeeded = (int) (numRows * numColumns / 2 - 0.5 * numColumns);
        for (int columnIndex = 0; columnIndex < numRows; columnIndex++) {
            for (int rowIndex = 0; rowIndex < columnIndex; rowIndex++) {
                if (getElementAt(rowIndex, columnIndex).isZero()) {
                    counter += 1;
                }
            }
        }
        return counter == numZeroesNeeded;
    }

    public boolean checkForEchelonForm() {
        return checkMainDiagonal() && checkLowerZeroes();
    }

    public boolean checkForSolvedForm() {
        return checkForEchelonForm() && checkUpperZeroes();
    }

    // returns true if a row has all null values except the last column
    public boolean checkForNoSolutions() {
        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            if (matrix[rowIndex].isNullRowExceptLastColumn()) {
                return true;
            }
        }
        return false;
    }

    // returns true if the main diagonal consists entirely of positive ones (i.e. has no zeroes)
    public boolean checkForFiniteSolutions() {
        int counter = 0;
        for (int columnIndex = 0; columnIndex < numInnerColumns && columnIndex < numRows; columnIndex++) {
            if (getElementAt(columnIndex, columnIndex).isPositiveOne()) {
                counter += 1;
            }
        }
        return counter == numInnerColumns;
    }

    public void printMatrix() {
        System.out.println();
        for (Row row : matrix) {
            System.out.println(Arrays.toString(row.getRow()));
        }
        System.out.println();
    }
}
