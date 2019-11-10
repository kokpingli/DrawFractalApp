package fractal.model;

import java.util.ArrayList;
import java.util.List;

public class Area {
	private Coordinate topLeft;
	private int width;
	private int height;

	public Area(Coordinate start, int width, int height) {
		this.topLeft = start;
		this.width = width;
		this.height = height;
	}


	public Coordinate getTopLeft() {
		return topLeft;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
