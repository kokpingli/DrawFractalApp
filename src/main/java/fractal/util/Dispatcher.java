package fractal.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import fractal.MainApp;
import fractal.model.Area;
import fractal.model.Caretaker;
import fractal.model.Coordinate;
import fractal.model.Memento;
import fractal.model.Originator;
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

	private void beginComputation() {
		
		int BOUND = 10;
		int N_PRODUCERS = 1;
		int N_CONSUMERS = Runtime.getRuntime().availableProcessors() * 2;
		//int N_CONSUMERS = 25;
		int id = Integer.MAX_VALUE;
		Area poisonPill = new Area(id, new Coordinate(Integer.MAX_VALUE, Integer.MAX_VALUE), 0, 0);
		//int poisonPillPerProducer = N_CONSUMERS / N_PRODUCERS;
		//int mod = N_CONSUMERS % N_PRODUCERS;
		
		BlockingQueue<Area> requests = new LinkedBlockingQueue<>(BOUND);
		
		//new Thread(new AreasProducer(gc, requests, poisonPill, poisonPillPerProducer+mod)).start();
		new Thread(new AreasProducer(gc, requests)).start();
		
		ExecutorService executor = Executors.newFixedThreadPool(N_CONSUMERS);

		Caretaker caretaker = new Caretaker();
		Originator originator = new Originator();
		
		for (int j = 0; j < N_CONSUMERS; ++j) {
			//Computation computation = new Computation(width, height, maxIteration, equationField, variableData, requests, poisonPill);
			Computation computation = new Computation(width, height, maxIteration, equationField, variableData, requests);
			CompletableFuture<Map<Coordinate,Double>> getIterations = CompletableFuture.supplyAsync(computation, executor);
			CompletableFuture<Memento> savedMementos = getIterations.thenApply(originator.store);
			
			savedMementos.thenAccept(caretaker);
		}
		
		// get snapshot after savedMementos
//		for (int j = 0; j < 10; ++j) {
//			Map<Coordinate, Double> snapshot = originator.restoreFromMemento(caretaker.getMemento(j));
//			
//			//theArticle.setText(textBoxString);
//			for (Entry<Coordinate, Double> entry : snapshot.entrySet()) {
//				Coordinate coordinate = entry.getKey();
//				Double iteration = entry.getValue();
//				System.out.println("iteration: " + iteration);
//				setNumIteration(coordinate, iteration);
//			}
//		}
		
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
