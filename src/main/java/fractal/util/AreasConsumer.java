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
			int N_CONSUMERS = Runtime.getRuntime().availableProcessors() * 2;
			ExecutorService executor = Executors.newFixedThreadPool(N_CONSUMERS);

			while (true) {
				Computation computation = new Computation(queue.take());
				executor.execute(computation);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
