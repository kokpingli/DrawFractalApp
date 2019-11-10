package fractal.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import fractal.model.Area;
import fractal.model.Coordinate;
import fractal.model.Variable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;

public class Dispatcher implements Runnable {
	
	private final double[][] data;
	private final RenderingParameters parameters;
	private final BlockingQueue<RequestMessage> requests;
	private final BlockingQueue<ResponseMessage> responses;

	public Dispatcher(GraphicsContext gc, TextField equationField, List<Variable> variables, int iterations) {
		
		this.parameters = new RenderingParameters((int)gc.getCanvas().getWidth(), (int)gc.getCanvas().getHeight(), equationField.getText().trim(), variables, iterations);
	
		this.requests = new LinkedBlockingQueue<>();
		this.responses = new LinkedBlockingQueue<>();

		this.data = new double[parameters.getWidth()][parameters.getHeight()];
	}

	@Override
	public void run() {
		WorkerPool pool = new WorkerPool(requests);
		try {
			int pendingResponse = 0;

			//Step 1: generate all the requests
			int areaHeight = 30;
			int areaWidth = 30;
			for (int yCoord = 0; yCoord < parameters.getHeight(); yCoord += areaHeight) {
				for (int xCoord = 0; xCoord < parameters.getWidth(); xCoord += areaWidth) {
					Coordinate nextCoord = new Coordinate(xCoord, yCoord);
					Area area = new Area(nextCoord, areaWidth, areaHeight);
					requests.put(new RequestMessage(area, parameters, responses));
					pendingResponse++;
				}
			}

			//Step 2: collate all the responses back
			while(pendingResponse > 0) {
				ResponseMessage response = responses.take();
				Map<Coordinate, Double> iterationsMap = response.getComputedValues();
				for (Map.Entry<Coordinate, Double> entry : iterationsMap.entrySet()) {
					data[entry.getKey().getX()][entry.getKey().getY()] = entry.getValue();
				}

				pendingResponse--;
			}
		} catch(InterruptedException ex) {
			throw new RuntimeException(ex);
		} finally {
			pool.shutdown();
		}

	}
	
	public double[][] getData() {
		return data;
	}
}