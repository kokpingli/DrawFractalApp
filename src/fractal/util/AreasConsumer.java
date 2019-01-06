package fractal.util;

import java.util.concurrent.BlockingQueue;

import fractal.model.Area;

public class AreasConsumer implements Runnable {
	private BlockingQueue<Area> queue;
	//private BlockingQueue<Integer> queue;
	private final Area poisonPill;
	//private final int poisonPill;
	
	public AreasConsumer(BlockingQueue<Area> queue, Area poisonPill) {
	//public AreasConsumer(BlockingQueue<Integer> queue, int poisonPill) {
		this.queue = queue;
		this.poisonPill = poisonPill;
	}
	
	public void run() {
		try {
			while (true) {
				Area area = queue.take();
				//Integer area = queue.take();
				if (area.equals(poisonPill)) {
					return;
				}
				System.out.println(Thread.currentThread().getName() + " result: " + area.getId());
				Thread.sleep(0);
			}
		} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
		}
	}
}
