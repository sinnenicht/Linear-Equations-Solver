package solver;

import java.util.Arrays;

public class Row {
    private ComplexNumber[] row;
    private int length;
    private int innerLength;

    public Row(String numberString, int numVariables) {
        this.length = numVariables;
        this.innerLength = length - 1;
        String[] numbersAsStrings = numberString.split(" ");

        ComplexNumber[] numbers = new ComplexNumber[length];
        for (int numbersIndex = 0; numbersIndex < length; numbersIndex++) {
            double[] realAndImaginary = parseComplexNumber(numbersAsStrings[numbersIndex]);
            numbers[numbersIndex] = new ComplexNumber(realAndImaginary[0], realAndImaginary[1]);
        }

        this.row = numbers;
        System.out.println(Arrays.toString(row));
    }

    public ComplexNumber[] getRow() {
        return row;
    }

    public void setElementAt(int index, ComplexNumber element) {
        row[index] = element;
    }

    public ComplexNumber getElementAt(int index) {
        return row[index];
    }

    private double[] parseComplexNumber(String numberString) {
        int stringLength = numberString.length();
        boolean hasRealNum = hasRealNumber(numberString);
        boolean hasImaginaryNum = numberString.contains("i");
        boolean hasOperator = hasRealNum && hasImaginaryNum;

        boolean foundOperator = false;
        StringBuilder realNum = new StringBuilder();
        StringBuilder imaginaryNum = new StringBuilder();

        for (int index = 0; index < stringLength; index++) {
            if (numberString.charAt(index) == '+' || numberString.charAt(index) == '-') {
                foundOperator = true;
            }

            if (hasOperator) {
                if (!foundOperator) {
                    realNum.append(numberString.charAt(index));
                } else if (numberString.charAt(index) != '+' && numberString.charAt(index) != 'i') {
                    imaginaryNum.append(numberString.charAt(index));
                }
            } else {
                if (hasRealNum) {
                    realNum.append(numberString.charAt(index));
                } else if (numberString.charAt(index) != 'i') {
                    imaginaryNum.append(numberString.charAt(index));
                }
            }
        }

        double[] realAndImaginary = new double[2];
        try {
            if (hasRealNum) {
                realAndImaginary[0] = Double.parseDouble(String.valueOf(realNum));
            } else {
                realAndImaginary[0] = 0;
            }

            if (hasImaginaryNum) {
                if (imaginaryNum.length() >= 1 && !"-".equals(String.valueOf(imaginaryNum))) {
                    realAndImaginary[1] = Double.parseDouble(String.valueOf(imaginaryNum));
                } else if ("-".equals(String.valueOf(imaginaryNum))) {
                    realAndImaginary[1] = -1;
                } else {
                    realAndImaginary[1] = 1;
                }
            } else {
                realAndImaginary[1] = 0;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: number format exception occurs.");
        }

        return realAndImaginary;
    }

    private boolean hasRealNumber(String numberString) {
        StringBuilder stringBuilder = new StringBuilder(numberString);
        stringBuilder.deleteCharAt(0);
        return numberString.contains("+") || String.valueOf(stringBuilder).contains("-") || (!numberString.contains("+") && !String.valueOf(stringBuilder).contains("-") && !numberString.contains("i"));
    }

    public boolean isNullRowExceptLastColumn() {
        int counter = 0;
        for (int index = 0; index < innerLength; index++) {
            if (row[index].isZero()) {
                counter += 1;
            }
        }
        if (counter == innerLength && !row[innerLength].isZero()) {
            counter += 1;
        }
        return counter == length;
    }

    // Row Operations
    public void subtractRow(Row rowToSubtract, ComplexNumber scalar) {
        ComplexNumber scalarNum = new ComplexNumber(scalar.getReal(), scalar.getImaginary());
        for (int index = 0; index < length; index++) {
            row[index].subtract(rowToSubtract.getElementAt(index), scalarNum);
        }
    }

    public void multiplyRow(ComplexNumber scalar) {
        for (int index = 0; index < length; index++) {
            row[index].multiply(scalar);
        }
    }

    public void multiplyRow(ComplexNumber numerator, double denominator) {
        for (int index = 0; index < length; index++) {
            row[index].multiply(numerator, denominator);
        }
    }

    public void divideRow(ComplexNumber scalar) {
        for (int index = 0; index < length; index++) {
            row[index].divide(scalar);
        }
    }
}
