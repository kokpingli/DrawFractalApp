package fractal.util;

import java.util.List;

import fractal.model.Variable;

public class RenderingParameters {
	private int width;
	private int height;
	private String equation;
	private List<Variable> variableList;
	
	public RenderingParameters(int width, int height, String equation, List<Variable> variableList) {
		this.width = width;
		this.height = height;
		this.equation = equation;
		this.variableList = variableList;
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
}
