package fractal.util;

import java.util.concurrent.BlockingQueue;

import fractal.model.Area;

public class RequestMessage {
	private final Area area;
	private final RenderingParameters renderingParameters;
	private final BlockingQueue<ResponseMessage> responseQueue;
	
	public RequestMessage(Area area, RenderingParameters display, BlockingQueue<ResponseMessage> responseQueue) {
		this.area = area;
		this.renderingParameters = display;
		this.responseQueue = responseQueue;
	}

	public Area getArea() {
		return area;
	}
	
	public RenderingParameters getRenderingParameters() {
		return renderingParameters;
	}

	public BlockingQueue<ResponseMessage> getResponseQueue() {
		return responseQueue;
	}
}
