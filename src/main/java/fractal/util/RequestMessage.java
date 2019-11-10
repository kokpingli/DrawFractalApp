package fractal.util;

import java.util.concurrent.BlockingQueue;

import fractal.model.Area;

public class RequestMessage {
	private Area area;
	private RenderingParameters display;
	private int iterations;
	private BlockingQueue<ResponseMessage> responseQueue;
	
	RequestMessage(Area area, RenderingParameters display, int iterations, BlockingQueue<ResponseMessage> responseQueue) {
		this.area = area;
		this.display = display;
		this.iterations = iterations;
		this.responseQueue = responseQueue;
	}

	public Area getArea() {
		return area;
	}
	
	public RenderingParameters getDisplay() {
		return display;
	}

	public int getIterations() {
		return iterations;
	}

	public BlockingQueue<ResponseMessage> getResponseQueue() {
		return responseQueue;
	}
}
