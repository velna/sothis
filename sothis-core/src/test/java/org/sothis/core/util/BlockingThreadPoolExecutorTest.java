package org.sothis.core.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BlockingThreadPoolExecutorTest {

	@Test
	public void test() {
		int minPoolSize = 1;
		int maxPoolSize = 10;
		BlockingThreadPoolExecutor poolExecutor = new BlockingThreadPoolExecutor("Test", minPoolSize, maxPoolSize, 50, 3000);
		Assert.assertEquals(minPoolSize, poolExecutor.getPoolSize());
		for (int r = 0; r < 2; r++) {
			CountDownLatch countDownLatch = new CountDownLatch(100);
			for (int i = 0; i < 100; i++) {
				MyRunnable myRunnable = new MyRunnable(countDownLatch);
				poolExecutor.execute(myRunnable);
			}
			try {
				while (!countDownLatch.await(100, TimeUnit.MILLISECONDS)) {
					Assert.assertEquals(maxPoolSize, poolExecutor.getPoolSize());
				}
				Thread.sleep(5000);
				Assert.assertEquals(minPoolSize, poolExecutor.getPoolSize());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private class MyRunnable implements Runnable {
		private CountDownLatch countDownLatch;

		public MyRunnable(CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
		}

		public void run() {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			countDownLatch.countDown();
		}

	}
}
