package fractal.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fractal.model.Area;
import fractal.model.BinaryNode;
import fractal.model.ComplexNumber;
import fractal.model.Coordinate;
import fractal.model.Variable;

public class FractalComputer {

	private List<Coordinate> getCoordinates(Area area) {
		List<Coordinate> coordList = new ArrayList<>(area.getWidth() * area.getHeight());
		int x = area.getTopLeft().getX();
		int y = area.getTopLeft().getY();

		for (int xCoord = x; xCoord < x + area.getWidth(); ++xCoord) {
			for (int yCoord = y; yCoord < y + area.getHeight(); ++yCoord) {
				Coordinate coord = new Coordinate(xCoord, yCoord);
				coordList.add(coord);
			}
		}

		return coordList;
	}

	private Double computeSingleElement(BinaryNode<String> root, RenderingParameters parameters, Coordinate location) {
		Map<String, ComplexNumber> variables = new HashMap<>();

		double iteration = 0;
		String resultVariable = "";
		for (Variable variable : parameters.getVariableList()) {
			if (variable.isInput()) {
				resultVariable = variable.getName();
			}
			if (variable.isIterable()) {
				variables.put(variable.getName(),
						new ComplexNumber(location.getX() * 2.0 / parameters.getWidth() - 1, location.getY() * (-2.0) / parameters.getHeight() + 1));
			} else {
				// for constant?
				variables.put(variable.getName(), variable.getComplexNumber());
			}
		}

		ComplexNumber result = StringOperations.evaluateTree(root, variables);

		int maxIteration = parameters.getIterations();
		while (result.modulus() < 2 * 2 && iteration < maxIteration) {
			result = StringOperations.evaluateTree(root, variables);
			variables.put(resultVariable, result);
			iteration++;
		}

		if (iteration < maxIteration) {
			double log_zn = Math.log(result.modulus()) / 2;
			double nu = Math.log(log_zn / Math.log(2)) / Math.log(2);
			iteration = (iteration + 1.0) - nu;
		}

		return iteration;
	}

	public Map<Coordinate, Double> computeArea(Area area,RenderingParameters parameters) {
		HashMap<Coordinate, Double> areaIteration = new HashMap<>();

		List<Coordinate> coordinates = getCoordinates(area);

		BinaryNode<String> root = StringOperations.evaluate(parameters.getEquation());

		for (Coordinate coordinate : coordinates) {
			double value = computeSingleElement(root, parameters, coordinate);
			areaIteration.put(coordinate, value);
		}

		return areaIteration;
	}

}
