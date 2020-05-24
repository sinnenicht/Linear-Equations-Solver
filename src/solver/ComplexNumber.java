package solver;

public class ComplexNumber {
    private double real;
    private double imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public double getNorm() {
        return real * real + imaginary * imaginary;
    }

    public ComplexNumber getConjugate() {
        return new ComplexNumber(real, -1 * imaginary);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean hasReal = false;
        if (!checkThreshold(real, 0)) {
            stringBuilder.append(real);
            hasReal = true;
        }
        if (!checkThreshold(imaginary, 0)) {
            if (imaginary > 0 && hasReal) {
                stringBuilder.append("+");
            }
            if (checkThreshold(imaginary, -1)) {
                stringBuilder.append("-");
            }
            if (!checkThreshold(Math.abs(imaginary), 1)) {
                stringBuilder.append(imaginary);
            }
            stringBuilder.append("i");
        }
        if (checkThreshold(real, 0) && checkThreshold(imaginary, 0)) {
            stringBuilder.append(0);
        }
        return String.valueOf(stringBuilder);
    }

    private boolean checkThreshold(double number1, double number2) {
        final double THRESHOLD = 0.0001;
        return Math.abs(number1 - number2) < THRESHOLD;
    }

    public boolean isZero() {
        return checkThreshold(real, 0) && checkThreshold(imaginary, 0);
    }

    public boolean isOne() {
        return checkThreshold(Math.abs(real), 1) && checkThreshold(imaginary, 0);
    }

    public boolean isPositiveOne() {
        return checkThreshold(real, 1) && checkThreshold(imaginary, 0);
    }

    public boolean isNegativeOne() {
        return checkThreshold(real, -1) && checkThreshold(imaginary, 0);
    }

    // Operations
    public void subtract(ComplexNumber number, ComplexNumber scalar) {
        ComplexNumber scaledNumber = new ComplexNumber(number.getReal(), number.getImaginary());
        ComplexNumber scalarNum = new ComplexNumber(scalar.getReal(), scalar.getImaginary());
        scaledNumber.multiply(scalarNum);
        this.real -= scaledNumber.real;
        this.imaginary -= scaledNumber.imaginary;
    }

    public void multiply(ComplexNumber scalar) {
        ComplexNumber scalarNum = new ComplexNumber(scalar.real, scalar.imaginary);
        double currentReal = this.real;
        double currentImaginary = this.imaginary;
        this.real = currentReal * scalarNum.real - currentImaginary * scalarNum.imaginary;
        this.imaginary = (currentReal * scalarNum.imaginary) + (currentImaginary * scalarNum.real);
    }

    public void multiply(ComplexNumber numerator, double denominator) {
        double currentReal = this.real;
        double currentImaginary = this.imaginary;
        this.real = (currentReal * numerator.real - currentImaginary * numerator.imaginary) / denominator;
        this.imaginary = (currentReal * numerator.imaginary + currentImaginary * numerator.real) / denominator;
    }

    public void divide(ComplexNumber scalar) {
        ComplexNumber numerator = new ComplexNumber(real, imaginary);
        ComplexNumber denominator = new ComplexNumber(scalar.real, scalar.imaginary);
        ComplexNumber conjugate = denominator.getConjugate();

        numerator.multiply(conjugate);
        denominator.multiply(conjugate);

        this.real = numerator.real / denominator.real;
        this.imaginary = numerator.imaginary / denominator.real;
    }
}
