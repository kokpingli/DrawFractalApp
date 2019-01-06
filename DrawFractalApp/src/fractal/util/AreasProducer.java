package fractal.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import fractal.model.Area;
import fractal.model.Coordinate;
import javafx.scene.canvas.GraphicsContext;

public class AreasProducer implements Runnable {

	private BlockingQueue<Area> areasQueue;
	//private BlockingQueue<Integer> areasQueue;
	private final Area poisonPill;
	//private final int poisonPill;
	private final int poisonPillPerProducer;
	private final double height;
	private final double width;
	
	public AreasProducer(GraphicsContext gc, BlockingQueue<Area> areasQueue, Area poisonPill, int poisonPillPerProducer) {
	//public AreasProducer(GraphicsContext gc, BlockingQueue<Integer> areasQueue, int poisonPill, int poisonPillPerProducer) {
		this.areasQueue = areasQueue;
		this.poisonPill = poisonPill;
		this.poisonPillPerProducer = poisonPillPerProducer;
		this.height = gc.getCanvas().getHeight();
		this.width = gc.getCanvas().getWidth();
	}
	
	public void run() {
		try {
			generateAreas();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private void generateAreas() throws InterruptedException {
		int areaHeight = 10;
		int areaWidth = 10;
		int id = 0;
		
		//for (int i = 0; i < 100; i++) {
		for (double yCoord = 0.0; yCoord < height; yCoord += areaHeight) {
			for (double xCoord = 0.0; xCoord < width; xCoord += areaWidth) {
				Coordinate nextCoord = new Coordinate(xCoord, yCoord);
				Area area = new Area(id, nextCoord, areaWidth, areaHeight);
				//areasQueue.put(ThreadLocalRandom.current().nextInt(100));
				areasQueue.put(area);
				++id;
			}
		}
		
/*		for (double yCoord = 0.0; yCoord < height; yCoord += areaHeight) {
			for (double xCoord = 0.0; xCoord < width; xCoord += areaWidth) {
				Coordinate nextCoord = new Coordinate(xCoord, yCoord);
				Area area = new Area(nextCoord, areaWidth, areaHeight);
				areasQueue.put(area);
			}
		}
*/		for (int j = 0; j < poisonPillPerProducer; ++j) {
			areasQueue.put(poisonPill);
		}
		
	}
}
