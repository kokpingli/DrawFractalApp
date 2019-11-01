package fractal.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import fractal.model.Area;

public class Window {
	private final int capacity;
	private BlockingQueue<RequestMessage> requests;
	
	public Window(int size, BlockingQueue<RequestMessage> requests) {
		this.capacity = size;
		this.requests = requests;
	}
	
	public RequestMessage getElement() {
		return this.requests.remove();
	}
	
	public void insertElement(RequestMessage requestMessage) {
		this.requests.add(requestMessage);
	}
	
	public int getNumOfElements() {
		return this.requests.size();
	}
	
	public int getCapacity() {
		return this.capacity;
	}
}
