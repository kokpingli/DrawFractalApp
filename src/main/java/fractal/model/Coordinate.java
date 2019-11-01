package fractal.model;

public class Coordinate {
	double x;
	double y;
	double originalY;

	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void update(double xCoord, double yCoord) {
		this.originalY = y;
		this.x = xCoord;
		this.y = yCoord;
	}

	public void increaseX() {
		this.x += 1;
	}

	public void increaseY() {
		this.y += 1;
	}

	public void resetX() {
		this.x = 0;
	}

	public void resetY() {
		this.y = 0;
	}

	public void restoreY() {
		this.y = originalY;
	}
}
