package fractal.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AreasConsumer implements Runnable {
	private BlockingQueue<RequestMessage> queue;

	public AreasConsumer(BlockingQueue<RequestMessage> queue) {
		this.queue = queue;
	}

	public void run() {
		try {
			while (true) {
				int N_CONSUMERS = Runtime.getRuntime().availableProcessors() * 2;
				ExecutorService executor = Executors.newFixedThreadPool(N_CONSUMERS);
				
				Computation computation = new Computation(queue.take());

				executor.execute(computation);
				
				Thread.sleep(0);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
