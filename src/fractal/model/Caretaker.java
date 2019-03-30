package fractal.model;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Caretaker implements Consumer<Memento> {
	ArrayList<Memento> savedSnapshots = new ArrayList<Memento>();
	
	public void addMemento(Memento m) { savedSnapshots.add(m); }
	
	public Memento getMemento(int index) { return savedSnapshots.get(index); }

	@Override
	public void accept(Memento m) {
		addMemento(m);
	}
 
}
