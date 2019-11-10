package fractal.util;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import fractal.MainApp;
import fractal.model.Coordinate;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class Dispatcher implements Runnable {
	
	final static Logger logger = Logger.getLogger(Dispatcher.class);

	private double[][] numIteration;
	private RenderingParameters display;
	BlockingQueue<RequestMessage> requests;
	BlockingQueue<ResponseMessage> responses;

	public Dispatcher(GraphicsContext gc, TextField equationField, MainApp mainApp,
			double[][] numIteration) {
		
		display = new RenderingParameters((int)gc.getCanvas().getWidth(), (int)gc.getCanvas().getHeight(), equationField.getText().trim(), mainApp.getVariableData());
	
		requests = new LinkedBlockingQueue<RequestMessage>();
		responses = new LinkedBlockingQueue<ResponseMessage>();
		
		numIteration = new double[display.getWidth()][display.getHeight()];
		this.numIteration = numIteration;
		// initialize the array
		for (int i = 0; i < display.getWidth(); ++i) {
			for (int j = 0; j < display.getHeight(); ++j) {
				this.numIteration[i][j] = 0;
			}
		}
	}
	
	public void run() {
		BasicConfigurator.configure();
		try {
			beginComputation();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void beginComputation() throws InterruptedException {
		AreasProducer areasProducer = new AreasProducer(requests, display, responses);
		
		Thread tAreasProducer = new Thread(areasProducer, "AreasProducer thread");
		tAreasProducer.start();
		
		AreasConsumer areasConsumer = new AreasConsumer(requests);
		
		Thread tAreasConsumer = new Thread(areasConsumer, "AreasConsumer thread");
		tAreasConsumer.start();
	}
	
	public double[][] getNumIteration() {
		int count = responses.size() < 10 ? responses.size() : 10;
		while(!responses.isEmpty() && count > 0) {
			try {
				ResponseMessage response = responses.take();
				Map<Coordinate, Double> iterationsMap = response.getComputedValues();
				for (Map.Entry<Coordinate, Double> entry : iterationsMap.entrySet()) {
				    numIteration[(int) entry.getKey().getX()][(int) entry.getKey().getY()] = entry.getValue();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			--count;
		}
		return numIteration;
	}
}