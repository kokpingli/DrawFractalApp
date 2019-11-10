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
import javafx.util.Pair;

public class Computation implements Runnable {
	
	private RequestMessage request;

	public Computation(RequestMessage request) {
		this.request = request;
	}

	private List<Coordinate> getCoordinates(Area area) {
		List<Coordinate> coordList = new ArrayList<>(area.getWidth() * area.getHeight());
		double x = area.getTopLeft().getX();
		double y = area.getTopLeft().getY();

		for (double xCoord = x; xCoord < x + area.getWidth(); ++xCoord) {
			for (double yCoord = y; yCoord < y + area.getHeight(); ++yCoord) {
				Coordinate coord = new Coordinate(xCoord, yCoord);
				coordList.add(coord);
			}
		}

		return coordList;
	}

	private Double computeSingleElement(long maxIteration, String equation, List<Variable> variableList,
														  Coordinate current) {
		Map<String, ComplexNumber> variables = new HashMap<>();

		double iteration = 0;
		String resultVariable = "";
		for (Variable variable : variableList) {
			if (variable.isInput()) {
				resultVariable = variable.getName();
			}
			if (variable.isIterable()) {
				variables.put(variable.getName(),
						new ComplexNumber(current.getX() * 2 / request.getDisplay().getWidth() - 1, current.getY() * (-2) / request.getDisplay().getHeight() + 1));
			} else {
				// for constant?
				variables.put(variable.getName(), variable.getComplexNumber());
			}
		}

		BinaryNode<String> root = StringOperations.evaluate(equation, variables);

		ComplexNumber result = StringOperations.evaluateTree(root, variables);

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

	@Override
	public void run() {
		HashMap<Coordinate, Double> areaIteration = new HashMap<>();

		List<Coordinate> coordinates = getCoordinates(request.getArea());

		for (Coordinate coordinate : coordinates) {
			double value = computeSingleElement(request.getIterations(), request.getDisplay().getEquation(), request.getDisplay().getVariableList(), coordinate);
			areaIteration.put(coordinate, value);
		}
		
		request.getResponseQueue().add(new ResponseMessage(areaIteration));
	}

}
