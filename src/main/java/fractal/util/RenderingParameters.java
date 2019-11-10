package fractal.util;

import java.util.List;

import fractal.model.Variable;

public class RenderingParameters {
	private final int width;
	private final int height;
	private final String equation;
	private final List<Variable> variableList;
	private final int iterations;
	
	public RenderingParameters(int width, int height, String equation, List<Variable> variableList, int iterations) {
		this.width = width;
		this.height = height;
		this.equation = equation;
		this.variableList = variableList;
		this.iterations = iterations;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public String getEquation() {
		return equation;
	}
	
	public List<Variable> getVariableList() {
		return variableList;
	}

	public int getIterations() { return iterations; }
}
