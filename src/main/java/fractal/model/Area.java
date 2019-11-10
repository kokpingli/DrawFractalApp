package fractal.model;

public class Area {
	private final Coordinate topLeft;
	private final int width;
	private final int height;

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
