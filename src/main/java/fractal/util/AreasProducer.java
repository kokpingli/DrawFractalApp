package fractal.util;

import java.util.concurrent.BlockingQueue;

import fractal.model.Area;
import fractal.model.Coordinate;
import javafx.scene.canvas.GraphicsContext;

public class AreasProducer implements Runnable {

	private BlockingQueue<Area> areasQueue;
	//private final Area poisonPill;
	//private final int poisonPillPerProducer;
	private final double height;
	private final double width;
	
	//public AreasProducer(GraphicsContext gc, BlockingQueue<Area> areasQueue, Area poisonPill, int poisonPillPerProducer) {
	public AreasProducer(GraphicsContext gc, BlockingQueue<Area> areasQueue) {
		this.areasQueue = areasQueue;
		//this.poisonPill = poisonPill;
		//this.poisonPillPerProducer = poisonPillPerProducer;
		this.height = gc.getCanvas().getHeight();
		this.width = gc.getCanvas().getWidth();
	}
	
	public void run() {
		try {
			generateAreas();
			// prepare first 20 areas in window
			// put these 20 areas in queue
			// everytime any returns, add 1 more in queue (who is aware of return of computation?)
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private void generateAreas() throws InterruptedException {
		int areaHeight = 20;
		int areaWidth = 20;
		//int areaHeight = 2;
		//int areaWidth = 2;
		int id = 0;
		
		for (double yCoord = 0.0; yCoord < height; yCoord += areaHeight) {
			for (double xCoord = 0.0; xCoord < width; xCoord += areaWidth) {
		//for (double yCoord = 0.0; yCoord < 5; yCoord += areaHeight) {
		//	for (double xCoord = 0.0; xCoord < 5; xCoord += areaWidth) {
				Coordinate nextCoord = new Coordinate(xCoord, yCoord);
				Area area = new Area(id, nextCoord, areaWidth, areaHeight);
				areasQueue.put(area);
				++id;
			}
		}
		
		//for (int j = 0; j < poisonPillPerProducer; ++j) {
		//	areasQueue.put(poisonPill);
		//}
		
	}
}
