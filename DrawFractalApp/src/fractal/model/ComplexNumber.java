package fractal.model;

public class ComplexNumber {
	private double real;
	private double imaginary;
	
	public ComplexNumber(double r, double i) {
		real = r;
		imaginary = i;
	}
	
	public ComplexNumber add(ComplexNumber other) {
		ComplexNumber addition = new ComplexNumber(real+other.real, imaginary+other.imaginary);
		return addition;
	}
	
	public ComplexNumber subtract(ComplexNumber other) {
		ComplexNumber subtraction = new ComplexNumber(real-other.real, imaginary-other.imaginary);
		return subtraction;
	}
	
	public ComplexNumber multiply(ComplexNumber other) {
		ComplexNumber multiplication = new ComplexNumber(real*other.real-imaginary*other.imaginary, real*other.imaginary+imaginary*other.real);
		return multiplication;
	}
	
	public ComplexNumber divide(ComplexNumber other) {
		ComplexNumber numerator = this.multiply(this.conjugate());
		ComplexNumber denominator = other.multiply(this.conjugate());
		ComplexNumber division = new ComplexNumber(numerator.real/denominator.real, numerator.imaginary/denominator.real);
		return division;
	}
	
	public ComplexNumber modulo(ComplexNumber other) {
		if (imaginary != 0)
			throw new IllegalArgumentException("Unable to modulo by complex number!");
		ComplexNumber moduloResult = new ComplexNumber(this.real%other.real, imaginary);
		return moduloResult;
	}
	
	ComplexNumber conjugate() {
		ComplexNumber conjugate = new ComplexNumber(real,-imaginary);
		return conjugate;
	}
	
	public double modulus() {
		return real*real + imaginary*imaginary;
	}
	
	public double getReal() {
		return real;
	}
	
	public double getImaginary() {
		return imaginary;
	}
}
