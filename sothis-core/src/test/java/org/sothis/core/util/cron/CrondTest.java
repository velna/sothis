package org.sothis.core.util.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class CrondTest {

	private final static Logger LOGGER = LoggerFactory.getLogger(CrondTest.class);

	@Test
	public void test() throws InterruptedException {
		Crond crond = new Crond();
		crond.start();
		crond.set(1, Cron.compile("*/5 * * * * *"), new Runnable() {
			@Override
			public void run() {
				LOGGER.info("running 1");
			}
		});
		Thread.sleep(10000);
		crond.set(2, Cron.compile("*/10 * * * * *"), new Runnable() {
			@Override
			public void run() {
				LOGGER.info("running 2");
			}
		});
		Thread.sleep(10000);
		crond.set(3, Cron.compile("*/20 * * * * *"), new Runnable() {
			@Override
			public void run() {
				LOGGER.info("running 3");
			}
		});
		crond.stop();
		Thread.sleep(30000);
	}
}
