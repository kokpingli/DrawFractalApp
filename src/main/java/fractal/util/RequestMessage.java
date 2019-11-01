package fractal.util;

import java.util.concurrent.BlockingQueue;

import fractal.model.Area;

public class RequestMessage {
	private Area area;
	private Display display;
	private BlockingQueue<ResponseMessage> responseQueue;
	
	RequestMessage(Area area, Display display, BlockingQueue<ResponseMessage> responseQueue) {
		this.area = area;
		this.display = display;
		this.responseQueue = responseQueue;
	}
	
	public Area getArea() {
		return area;
	}
	
	public Display getDisplay() {
		return display;
	}
	
	public BlockingQueue<ResponseMessage> getResponseQueue() {
		return responseQueue;
	}
}
