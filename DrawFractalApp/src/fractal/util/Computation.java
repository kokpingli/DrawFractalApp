package fractal.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import fractal.model.Area;
import fractal.model.BinaryNode;
import fractal.model.ComplexNumber;
import fractal.model.Coordinate;
import fractal.model.Variable;
import javafx.util.Pair;

public class Computation implements Runnable {
	
	private long maxIteration;
	private String equation;
	private List<Variable> variableList;
	private int width;
	private int height;
	private Area poisonPill;
	
	BlockingQueue<Area> in;
	BlockingQueue<Map<Coordinate, Double>> out;
	
	public Computation(int width, int height, long maxIteration, String equation, List<Variable> variableList, BlockingQueue<Area> requests, BlockingQueue<Map<Coordinate, Double>> replies, Area poisonPill) {
		this.maxIteration = maxIteration;
		this.equation = equation;
		this.variableList = variableList;
		this.width = width;
		this.height = height;
		this.poisonPill = poisonPill;
		
		this.in = requests;
		this.out = replies;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				Area area = in.take();
				
				if (area.equals(poisonPill)) {
					return;
				}
				List<Coordinate> coordList = area.getCoordinates();
				HashMap<Coordinate, Double> areaIteration = new HashMap<>();
			
				for (Coordinate toCompute : coordList) {
					System.out.println(Thread.currentThread().getName()+" Start. coordinate = " + toCompute.getX() + "," + toCompute.getY());
					//Pair<Coordinate, Double> reply = computeIteration(maxIteration, equation, variableList, toCompute);
					//areaIteration.put(reply.getKey(), reply.getValue());
				}
			
				/*try {
					out.put(areaIteration);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				 */
				//System.out.println(Thread.currentThread().getName()+" End.");
			}
			
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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

}
