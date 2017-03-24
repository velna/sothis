package org.sothis.core.util;

import java.util.ArrayList;
import java.util.List;

import org.sothis.core.util.Counters.Counter;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CountersTest {

	@Test
	public void test() {
		Counters counters = new Counters();
		CounterThread thread = new CounterThread(counters);
		thread.start();
		List<Long> lasts = new ArrayList<>();
		List<Long> snapshots = new ArrayList<>();
		for (Counter counter : counters) {
			lasts.add(counter.getLast());
			snapshots.add(counter.getSnapshot());
		}
		for (int i = 0; i < 10; i++) {
			Assert.assertEquals(snapshots, lasts);
			snapshots.clear();
			for (Counter counter : counters) {
				snapshots.add(counter.getSnapshot());
			}
			counters.snapshot();
			lasts.clear();
			for (Counter counter : counters) {
				lasts.add(counter.getLast());
			}
			try {
				Thread.sleep(100);
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
			for (int i = 0; i < 100; i++) {
				total.add(10);
				ok.add(5);
				error.incr();
				hide.incr();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					break;
				}
			}
		}

	}
}
