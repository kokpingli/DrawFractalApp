package fractal.util;

import java.util.HashSet;
import java.util.Set;

import fractal.model.ComplexNumber;
import fractal.model.Variable;

public class StringUtil {
	
	private static boolean isValidEquation(String str) {
		String regex = "^[0-9a-zA-Z+\\-*/%().]$";
		return str.matches(regex) ? true : false; 
	}
	
	private static boolean isValidComplexNumber(String str) {
		String regex = "([-]?\\d*\\.?\\d*?[+-]?\\d*\\.?\\d*?[ij]?)";
		return str.matches(regex) ? true : false;
	}
	
	public static Set<Variable> extractVariables(String str) {
		str = str.trim();
		
		Set<Variable> variables = new HashSet<>();
		for (int index = 0; index < str.length(); index++) {
			char charAt = str.charAt(index);
			if (!isValidEquation(Character.toString(charAt))) {
				throw new IllegalArgumentException("Please enter alphanumeric, operators and parentheses only!");
			}
			if(Character.isLetter(charAt)) {
				Variable variable = new Variable();
				variable.setName(Character.toString(charAt));
				variable.setInitialValue(Integer.toString(0));
				variables.add(variable);
			}
		}
		return variables;
	}
	
	public static ComplexNumber toComplexNumber(String str) {
		if (!isValidComplexNumber(str))
			throw new IllegalArgumentException("Please enter a valid complex number!");
		
		int opIndex = 0;
		if (str.startsWith("-")) {
			opIndex = Math.max(str.indexOf("+", 1),str.indexOf("-", 1));
		} else {
			opIndex = Math.max(str.indexOf("+"),str.indexOf("-"));
		}
		
		if (opIndex == -1) {
			return new ComplexNumber(Double.parseDouble(str), 0.0);
		}
		else {
			int withoutOne = 2;
			String imaginaryPart = str.substring(opIndex,str.length()-1);
			if (str.length() - opIndex == withoutOne) {
				imaginaryPart = str.substring(opIndex, str.length()-1) + "1";
			}
			return new ComplexNumber(Double.parseDouble(str.substring(0,opIndex)), Double.parseDouble(imaginaryPart));
		}
	}
	
}
