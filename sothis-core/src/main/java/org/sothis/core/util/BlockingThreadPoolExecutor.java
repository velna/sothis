package org.sothis.core.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockingThreadPoolExecutor implements Executor {
	private final static Logger LOGGER = LoggerFactory.getLogger(BlockingThreadPoolExecutor.class);

	private final BlockingQueue<Runnable> taskQueue;
	private final int minThreadCount;
	private final int maxThreadCount;
	private final long idleTime;
	private final String threadNamePrefix;
	private final AtomicInteger poolSize = new AtomicInteger(0);
	private final boolean[] poolIds;
	private final ThreadGroup threadGroup = new ThreadGroup("BlockingThreadPoolExecutor");

	public BlockingThreadPoolExecutor(String threadNamePrefix, int minThreadCount, int maxThreadCount,
			int taskQueueSize, long idleTime) {
		this.threadNamePrefix = threadNamePrefix;
		this.minThreadCount = minThreadCount;
		this.maxThreadCount = maxThreadCount;
		this.idleTime = idleTime;
		this.poolIds = new boolean[maxThreadCount + 1];

		taskQueue = new LinkedBlockingQueue<Runnable>(taskQueueSize);
		for (int i = 0; i < minThreadCount; i++) {
			createNewThread();
		}
	}

	public int getPoolSize() {
		return poolSize.get();
	}

	public void execute(Runnable command) {
		if (null == command) {
			throw new NullPointerException();
		}
		try {
			taskQueue.put(command);
			checkThreadPool();
		} catch (InterruptedException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("", e);
			}
		}
	}

	private void checkThreadPool() {
		while (taskQueue.size() > poolSize.get() && poolSize.get() < maxThreadCount) {
			createNewThread();
		}
	}

	private synchronized int getPoolId() {
		for (int i = 0; i < poolIds.length; i++) {
			if (poolIds[i] == false) {
				poolIds[i] = true;
				return i + 1;
			}
		}
		return 0;
	}

	private synchronized void freePoolId(int i) {
		poolIds[i - 1] = false;
	}

	private void createNewThread() {
		String threadName = threadNamePrefix + "-" + getPoolId();
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create new [" + threadNamePrefix + "] thread: " + threadName);
		}
		Thread thread = new Thread(threadGroup, new ExecutorRunnable(), threadName);
		thread.setDaemon(true);
		thread.start();
		poolSize.incrementAndGet();
	}

	private class ExecutorRunnable implements Runnable {

		public void run() {
			try {
				while (true) {
					try {
						Runnable command = taskQueue.poll(idleTime, TimeUnit.MILLISECONDS);
						if (null != command) {
							command.run();
						} else if (poolSize.get() > minThreadCount) {
							poolSize.decrementAndGet();
							break;
						}
					} catch (InterruptedException e) {
						break;
					} catch (Exception e) {
						if (LOGGER.isErrorEnabled()) {
							LOGGER.error("", e);
						}
					}
				}
			} finally {
				String threadName = Thread.currentThread().getName();
				int i = threadName.lastIndexOf('-');
				freePoolId(Integer.parseInt(threadName.substring(i + 1)));
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("die: " + threadName);
				}
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		threadGroup.interrupt();
	}
}
