package fractal.model;

import java.util.Map;

public class Memento {
	//private String article;
	private Map<Coordinate, Double> snapshot;
	
	//public Memento(String articleSave) { article = articleSave; }
	public Memento(Map<Coordinate, Double> snapshotSave) { snapshot = snapshotSave; }
	
	//public String getSavedArticle() { return article; }
	public Map<Coordinate, Double> getSavedSnapshot() { return snapshot; }
}
