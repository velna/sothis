package org.sothis.core.util;

import org.sothis.core.util.Counters.Counter;
import org.testng.annotations.Test;

public class CountersTest {

	@Test
	public void test() {
		Counters counters = new Counters();
		new CounterThread(counters).start();
		for (int i = 0; i < 50; i++) {
			counters.snapshot();
			System.out.println(counters.toString());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	private class CounterThread extends Thread {
		private Counter total;
		private Counter ok;
		private Counter error;
		private Counter hide;

		public CounterThread(Counters counters) {
			total = counters.create("test", "total", Counters.OPT_TOTAL_ONLY);
			ok = counters.create("test", "ok", 0);
			error = counters.create("test", "error", 0);
			hide = counters.create("test", "hide", Counters.OPT_NO_PRINT);
		}

		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				total.add(10);
				ok.add(5);
				error.incr();
				hide.incr();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
			}
		}

	}
}
