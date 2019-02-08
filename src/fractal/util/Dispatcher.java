package fractal.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import fractal.MainApp;
import fractal.model.Area;
import fractal.model.Coordinate;
import fractal.model.Variable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;

public class Dispatcher implements Runnable {

    private double[][] numIteration;
    // should we remove width and height from here?
	private int width;
	private int height;
	private long maxIteration;
	private String equationField;
	private List<Variable> variableData;
	private GraphicsContext gc;
	
	public Dispatcher(GraphicsContext gc, long maxIteration, TextField equationField, MainApp mainApp, double[][] numIteration) {
		this.maxIteration = maxIteration;
		this.equationField = equationField.getText().trim();
		this.variableData = mainApp.getVariableData();
		this.width = (int) gc.getCanvas().getWidth();
		this.height = (int) gc.getCanvas().getHeight();
		this.gc = gc;
		
		numIteration = new double[this.width][this.height];
		this.numIteration = numIteration;
		// initialize the array
		for (int i = 0; i < gc.getCanvas().getWidth(); ++i)	{
			for (int j = 0; j < gc.getCanvas().getHeight(); ++j) {
				this.numIteration[i][j] = 10;
			}
		}
	}	
	
	public double[][] getNumIteration() {
		return numIteration;
	}
	
	private void setNumIteration(Coordinate coordinate, Double iteration) {
		numIteration[(int) coordinate.getX()][(int) coordinate.getY()] = iteration;
	}

	@Override
	public void run() {
		beginComputation();
	}
	
	private void updateArray(Map<Coordinate, Double> reply) {
		for (Entry<Coordinate, Double> entry : reply.entrySet()) {
			Coordinate coordinate = entry.getKey();
			Double iteration = entry.getValue();
			// TO DELETE - will implement create snapshot
			setNumIteration(coordinate, iteration);
		}
		
	}

	private void beginComputation() {
		
		int BOUND = 10;
		//int N_PRODUCERS = 4;
		int N_PRODUCERS = 1;
		int N_CONSUMERS = Runtime.getRuntime().availableProcessors();
		int id = Integer.MAX_VALUE;
		Area poisonPill = new Area(id, new Coordinate(Integer.MAX_VALUE, Integer.MAX_VALUE), 0, 0);
		int poisonPillPerProducer = N_CONSUMERS / N_PRODUCERS;
		int mod = N_CONSUMERS % N_PRODUCERS;
		
		BlockingQueue<Area> requests = new LinkedBlockingQueue<>(BOUND);
		BlockingQueue<Map<Coordinate, Double>> reply = new LinkedBlockingDeque<>();
		
		//for (int i = 1; i < N_PRODUCERS; ++i) {
		//	new Thread(new AreasProducer(gc, requests, poisonPill, poisonPillPerProducer)).start();
		//}
		new Thread(new AreasProducer(gc, requests, poisonPill, poisonPillPerProducer+mod)).start();
		
		ExecutorService executor = Executors.newFixedThreadPool(N_CONSUMERS);
		List<Computation> computationList = new ArrayList<>();
		
		for (int j = 0; j < N_CONSUMERS; ++j) {
			Computation computation = new Computation(width, height, maxIteration, equationField, variableData, requests, poisonPill);
			computationList.add(computation);
		}	
			
		try {
			List<Future<Map<Coordinate,Double>>> future = executor.invokeAll(computationList);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//new Thread(new AreasProducer(gc, requests, poisonPill, poisonPillPerProducer+mod)).start();	
/*		
			
		Coordinate nextCoord = null;
		
		double xCoord = 0.0;
		double yCoord = 0.0;
		
		//for (yCoord = 0.0; yCoord < height; ++yCoord) {
		//	for (xCoord = 0.0; xCoord < width; ++xCoord) {
		for (yCoord = 0.0; yCoord < trialHeight; yCoord += areaHeight) {
			for (xCoord = 0.0; xCoord < trialWidth; xCoord += areaWidth) {
				nextCoord = new Coordinate(xCoord, yCoord);
				try {
					Computation computation = new Computation(width, height, maxIteration, equationField, variableData, requests, reply);
					executor.execute(computation);
					Area area = new Area(nextCoord, areaWidth, areaHeight);
					//requests.add(area);
					requests.put(area);
				} catch (InterruptedException e) {
				//} catch (IllegalStateException e) {
					throw new RuntimeException(e);
				}
			}
		}
*/
		// queue test end
		//return replies;
/*		int areaWidth = 10;
		int areaHeight = 10;
		
		int trialWidth = 100;
		int trialHeight = 100;
		
		double counter = (trialHeight * trialWidth) / (areaWidth * areaHeight);
		while (counter != 0) {
			if (!reply.isEmpty()) {
				try {
					System.out.println(Thread.currentThread().getName()+" Starts.");
					// just for testing
					Map<Coordinate, Double> items = new HashMap<>();
					items = reply.take();

			        items.forEach((k,v)->System.out.println("Item : " + k + " Count : " + v));
			        System.out.println(Thread.currentThread().getName()+" Ends.");
			        // just for testing ends here
					//updateArray(reply.take());
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				counter--;
			}
		}
*/
		
		executor.shutdown();
		try {
			if (!executor.awaitTermination(30000, TimeUnit.MILLISECONDS))
				executor.shutdownNow();
		} catch (InterruptedException e) {
			executor.shutdownNow();
		}
		System.out.println("executor is shut down!");

	}

}
