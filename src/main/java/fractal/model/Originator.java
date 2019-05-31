package fractal.model;

import java.util.Map;
import java.util.function.Function;

public class Originator {

	private Map<Coordinate, Double> snapshot;
	
	public void set(Map<Coordinate, Double> newSnapshot) {	
		//System.out.println("From Originator: Current Version of Article\n" + newSnapshot + "\n");
		
		snapshot = newSnapshot;
	}
	
	public Memento storeInMemento() {
		//System.out.println("From Originator: Saving to Memento");
		
		return new Memento(snapshot);
	}
	
	public Map<Coordinate, Double> restoreFromMemento(Memento memento) {	
		snapshot = memento.getSavedSnapshot();
		
		//System.out.println("From Originator: Previous Article Saved in Memento\n" + snapshot + "\n");
		
		return snapshot;
	}

	public Function<Map<Coordinate, Double>, Memento> store = iterations -> {
		set(iterations);
		return storeInMemento();
	};

}
