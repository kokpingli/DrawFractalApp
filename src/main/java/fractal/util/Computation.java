package fractal.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

import fractal.model.Area;
import fractal.model.BinaryNode;
import fractal.model.ComplexNumber;
import fractal.model.Coordinate;
import fractal.model.Variable;
import javafx.util.Pair;

public class Computation implements Runnable {
	
	private Area area;
	private Display display;
	private BlockingQueue<ResponseMessage> responseQueue;
	
	public Computation(RequestMessage request) {
		this.area = request.getArea();
		this.display = request.getDisplay();
		this.responseQueue = request.getResponseQueue();
	}

	private Pair<Coordinate, Double> computeIteration(long maxIteration, String equation, List<Variable> variableList,
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
						new ComplexNumber(current.getX() * 2 / display.getWidth() - 1, current.getY() * (-2) / display.getHeight() + 1));
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

		Pair<Coordinate, Double> reply = new Pair<>(current, iteration);

		return reply;
	}

	@Override
	public void run() {
		HashMap<Coordinate, Double> areaIteration = new HashMap<>();

		List<Coordinate> coordinates = area.getCoordinates();

		for (Coordinate coordinate : coordinates) {
			Pair<Coordinate, Double> reply = computeIteration(display.MAX_ITERATION, display.getEquation(), display.getVariableList(), coordinate);
			areaIteration.put(reply.getKey(), reply.getValue());
		}
		
		responseQueue.add(new ResponseMessage(areaIteration));
		System.out.println("responseQueue: " + responseQueue.size());
	}

}
