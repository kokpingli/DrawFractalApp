package fractal.util;

import java.util.Map;

import fractal.model.Coordinate;

public class ResponseMessage {
	private Map<Coordinate, Double> numIterations;
	
	ResponseMessage(Map<Coordinate, Double> numIterations) {
		this.numIterations = numIterations;
	}
	
	public Map<Coordinate, Double> getNumIterations() {
		return numIterations;
	}
}
