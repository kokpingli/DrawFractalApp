package fractal.util;

import java.util.Map;

import fractal.model.Coordinate;

public class ResponseMessage {
	private final Map<Coordinate, Double> computedValues;
	
	public ResponseMessage(Map<Coordinate, Double> numIterations) {
		this.computedValues = numIterations;
	}
	
	public Map<Coordinate, Double> getComputedValues() {
		return computedValues;
	}
}
