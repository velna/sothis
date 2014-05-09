package com.zamplus.thrift.hugecabinet;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sothis.core.util.PerformanceStats;
import org.sothis.thrift.AsyncHandler;
import org.sothis.thrift.Sync;
import org.sothis.thrift.TException;
import org.sothis.thrift.protocol.TBinaryProtocol;
import org.sothis.thrift.protocol.TCall;
import org.sothis.thrift.protocol.TMessage;

public class CabinetManagerTest implements Runnable {
	private final static PerformanceStats requestStat = PerformanceStats.newStat("zamplus");
	private final static Logger LOGGER = LoggerFactory.getLogger(CabinetManagerTest.class);

	private final AtomicInteger total = new AtomicInteger(0);
	private final AtomicInteger ok = new AtomicInteger(0);
	private final AtomicInteger error = new AtomicInteger(0);

	private final CabinetManager client;
	private final int n;
	private final CountDownLatch countDownLatch;
	private final int sleep;

	public CabinetManagerTest(CabinetManager client, int n, int sleep, CountDownLatch countDownLatch) {
		super();
		this.client = client;
		this.n = n;
		this.sleep = sleep;
		this.countDownLatch = countDownLatch;
	}

	private void stat(TMessage msg, long start) {
		long now = System.currentTimeMillis();
		long cost = System.currentTimeMillis() - start;
		LOGGER.debug("[{}] recv {}, cost {}", now, msg.seqid, cost);
		requestStat.stat(cost);
	}

	@Override
	public void run() {
		for (int i = 0; i < n; i++) {
			try {
				final long start = System.currentTimeMillis();
				// LOGGER.info("[{}]", start);
				Sync sync = client.QueryItem(new QueryItemREQ(String.valueOf(i).getBytes("UTF-8")), "test_kv2",
						new AsyncHandler<TCall<QueryItem_args, QueryItem_result>>() {

							@Override
							public void operationCompleted(TCall<QueryItem_args, QueryItem_result> call) {
								stat(call.getMessage(), start);
								ok.incrementAndGet();
								if (total.incrementAndGet() == n) {
									countDownLatch.countDown();
								}
							}

							@Override
							public void operationFailed(TCall<QueryItem_args, QueryItem_result> call, TException e) {
								error.incrementAndGet();
								if (total.incrementAndGet() == n) {
									countDownLatch.countDown();
								}
							}

						});
				if (sleep > 0 && i % sleep == 0) {
					sync.sync();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private final static String[] DEFAULT_ARGS = "-h 61.152.223.3 -n 100 -s 1".split(" ");

	public static void main(String[] args) throws Exception {
		BenchMarkOptions options = BenchMarkOptions.parse(args.length > 0 ? args : DEFAULT_ARGS);
		if (null == options) {
			BenchMarkOptions.printUseage();
			return;
		}

		CabinetManagerImpl client = new CabinetManagerImpl(options.getEventThreads(), Executors.defaultThreadFactory());
		SocketAddress remote = new InetSocketAddress(options.getHost(), options.getPort());
		for (int i = 0; i < options.getConnections(); i++) {
			client.connect(remote, false, TBinaryProtocol.FACTORY);
		}

		if (options.getDelay() > 0) {
			Thread.sleep(options.getDelay());
		}

		CountDownLatch countDownLatch = new CountDownLatch(options.getThreads());
		PerformanceStats.startLoggingThread();
		final long start = System.currentTimeMillis();
		CabinetManagerTest[] tests = new CabinetManagerTest[options.getThreads()];
		for (int i = 0; i < options.getThreads(); i++) {
			tests[i] = new CabinetManagerTest(client, options.getRequests(), options.getSleep(), countDownLatch);
			new Thread(tests[i]).start();
		}
		countDownLatch.await();
		long total = 0, ok = 0, error = 0;
		for (int i = 0; i < options.getThreads(); i++) {
			total += tests[i].total.get();
			ok += tests[i].ok.get();
			error += tests[i].error.get();
		}
		System.out.format("send %d requests\n", total);
		long time = System.currentTimeMillis() - start;
		System.out.format("time: %d, ok: %d, error: %d\n", time, ok, error);
		System.out.format("%d qps\n", total * 1000 / time);
		System.out.println(PerformanceStats.statString());
		System.exit(0);
	}
}
