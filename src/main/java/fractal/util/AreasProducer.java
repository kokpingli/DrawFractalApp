package fractal.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import fractal.model.Area;
import fractal.model.Coordinate;
import javafx.scene.canvas.GraphicsContext;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class AreasProducer implements Runnable {
	final static Logger logger = Logger.getLogger(AreasProducer.class);
	private Window window;
	private final double height;
	private final double width;
	private Display display;
	private BlockingQueue<RequestMessage> requests;
	private BlockingQueue<ResponseMessage> responses;

	public AreasProducer(BlockingQueue<RequestMessage> requests, Display display, BlockingQueue<ResponseMessage> responses) {
		this.height = display.getHeight();
		this.width = display.getWidth();

		this.display = display;
		this.requests = requests;
		this.responses = responses;
		
		int WINDOW_SIZE = 20;
		this.window = new Window(WINDOW_SIZE, requests);
	}

	public void run() {
		BasicConfigurator.configure();

		try {
			generateAreas();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (!this.requests.isEmpty() && (this.window.getNumOfElements() < this.window.getCapacity())) {
			this.window.insertElement(this.requests.remove());
		}
	}

	public void generateAreas() throws InterruptedException {
		int areaHeight = 30;
		int areaWidth = 30;
		int id = 0;

		for (double yCoord = 0.0; yCoord < height; yCoord += areaHeight) {
			for (double xCoord = 0.0; xCoord < width; xCoord += areaWidth) {
				Coordinate nextCoord = new Coordinate(xCoord, yCoord);
				Area area = new Area(id, nextCoord, areaWidth, areaHeight);
				this.requests.put(new RequestMessage(area, display, responses));
				Thread.sleep(50);
				++id;
			}
		}
	}
}
