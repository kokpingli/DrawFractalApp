package fractal.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorkerPool {
	private AtomicBoolean terminated;

	public WorkerPool(BlockingQueue<RequestMessage> queue) {
		FractalComputer computer = new FractalComputer();

		terminated = new AtomicBoolean(false);

		int size = Runtime.getRuntime().availableProcessors() * 2;

		for(int i=0;i<size;i++) {
			Thread t = new Thread(()->{
				while (!terminated.get()) {
					try {
						RequestMessage request = queue.poll(10, TimeUnit.MILLISECONDS);
						if(request != null) {
							request.getResponseQueue().add(new ResponseMessage(computer.computeArea(request.getArea(), request.getRenderingParameters())));
						}
					} catch (InterruptedException ex) {
						throw new RuntimeException(ex);
					}
				}
			},"Worker thread #"+i);
			t.setDaemon(true);
			t.start();
		}
	}

	public void shutdown() {
		terminated.set(true);
	}

}
