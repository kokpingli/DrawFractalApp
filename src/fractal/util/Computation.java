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

public class Computation implements Supplier<Map<Coordinate, Double>> {	
	
	private long maxIteration;
	private String equation;
	private List<Variable> variableList;
	private int width;
	private int height;
	private Area poisonPill;
	
	BlockingQueue<Area> in;
	
	public Computation(int width, int height, long maxIteration, String equation, List<Variable> variableList, BlockingQueue<Area> requests, Area poisonPill) {
		this.maxIteration = maxIteration;
		this.equation = equation;
		this.variableList = variableList;
		this.width = width;
		this.height = height;
		this.poisonPill = poisonPill;
		
		this.in = requests;
	}
		
	private Pair<Coordinate, Double> computeIteration(long maxIteration, String equation, List<Variable> variableList, Coordinate current) {
		Map<String,ComplexNumber> variables = new HashMap<>();

		double iteration = 0;
		String resultVariable = "";
		for (Variable variable : variableList) {
			if (variable.isInput()) {
				resultVariable = variable.getName();
			}
			if (variable.isIterable()) {
				variables.put(variable.getName(), new ComplexNumber(current.getX()*2/width - 1,
								                                current.getY()*(-2)/height + 1));
			} else {
				// for constant?
				variables.put(variable.getName(), variable.getComplexNumber());
			}
		}
				
		BinaryNode<String> root = StringOperations.evaluate(equation,variables);
				
		ComplexNumber result = StringOperations.evaluateTree(root, variables);
				
		while (result.modulus() < 2*2 && iteration < maxIteration) {
			result = StringOperations.evaluateTree(root, variables);
			variables.put(resultVariable, result);
			iteration++;
		}
				
		if (iteration < maxIteration) {
			double log_zn = Math.log(result.modulus()) / 2;
			double nu = Math.log(log_zn/Math.log(2))/Math.log(2);
			iteration = (iteration + 1.0) - nu;
		}
		
		Pair<Coordinate, Double> reply = new Pair<>(current, iteration);
		
		return reply;
	}

	@Override
	public Map<Coordinate, Double> get() {
		try {
			HashMap<Coordinate, Double> areaIteration = new HashMap<>();
			
			while(true) {
				Area area = in.take();
				System.out.println("area: " + area.getId());
				
				List<Coordinate> coordList = area.getCoordinates();
				//System.out.println("startCoord: " + coordList.get(0));
				
				if (area.equals(poisonPill)) {
					System.out.println(Thread.currentThread().getName());
					System.out.println("poisonPill received!");
					System.out.println("areaIteration size: " + areaIteration.size());
					return areaIteration;
				}
			
				for (Coordinate toCompute : coordList) {
					//System.out.println(Thread.currentThread().getName() + " Start. coordinate = " + toCompute.getX() + "," + toCompute.getY());
					Pair<Coordinate, Double> reply = computeIteration(maxIteration, equation, variableList, toCompute);
					//System.out.println("Coordinate: " + reply.getKey() + " Double: " + reply.getValue());
					areaIteration.put(reply.getKey(), reply.getValue());
				}
			}
			
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
