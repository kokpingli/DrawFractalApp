package fractal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Area {
	int id;
	Coordinate start;
	int width;
	int height;
	Map<Coordinate, Long> iteration;

	public Area(int id, Coordinate start, int width, int height) {
		this.id = id;
		this.start = start;
		this.width = width;
		this.height = height;
	}

	public List<Coordinate> getCoordinates() {
		List<Coordinate> coordList = new ArrayList<>(width * height);
		double x = start.getX();
		double y = start.getY();

		for (double xCoord = x; xCoord < x + width; ++xCoord) {
			for (double yCoord = y; yCoord < y + height; ++yCoord) {
				Coordinate coord = new Coordinate(xCoord, yCoord);
				coordList.add(coord);
			}
		}

		return coordList;
	}

	public int getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Area other = (Area) o;
		return start == other.start && width == other.width && height == other.height;
	}

	public int hashcode() {
		return id;
	}
}
