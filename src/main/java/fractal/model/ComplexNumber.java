package fractal.model;

public class ComplexNumber {
	private final double real;
	private final double imaginary;

	public ComplexNumber(double r, double i) {
		real = r;
		imaginary = i;
	}

	public ComplexNumber add(ComplexNumber other) {
		return new ComplexNumber(real + other.real, imaginary + other.imaginary);
	}

	public ComplexNumber subtract(ComplexNumber other) {
		return new ComplexNumber(real - other.real, imaginary - other.imaginary);
	}

	public ComplexNumber multiply(ComplexNumber other) {
		return new ComplexNumber(real * other.real - imaginary * other.imaginary,
				real * other.imaginary + imaginary * other.real);
	}

	public ComplexNumber divide(ComplexNumber other) {
		ComplexNumber numerator = this.multiply(this.conjugate());
		ComplexNumber denominator = other.multiply(this.conjugate());
		return new ComplexNumber(numerator.real / denominator.real,
				numerator.imaginary / denominator.real);
	}

	public ComplexNumber modulo(ComplexNumber other) {
		if (imaginary != 0)
			throw new IllegalArgumentException("Unable to modulo by complex number!");
		return new ComplexNumber(this.real % other.real, imaginary);
	}

	public ComplexNumber conjugate() {
		return new ComplexNumber(real, -imaginary);
	}

	public double modulus() {
		return real * real + imaginary * imaginary;
	}

	public double getReal() {
		return real;
	}

	public double getImaginary() {
		return imaginary;
	}
}
