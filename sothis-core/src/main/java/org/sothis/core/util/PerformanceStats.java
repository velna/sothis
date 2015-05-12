package org.sothis.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.sothis.core.util.LoggerFactory;

public class PerformanceStats {
	private final static Logger PERFORMANCE_LOGGER = LoggerFactory.getPerformanceLogger(PerformanceStats.class);
	private final static List<PerformanceStats> STATS = Collections.synchronizedList(new ArrayList<PerformanceStats>());
	private static Thread STAT_THREAD;
	private static long STEP = 1;

	private final AtomicLong[] counters = new AtomicLong[21];
	private final AtomicLong totalTime = new AtomicLong(0);
	private final AtomicLong totalCount = new AtomicLong(0);
	private final String name;

	private PerformanceStats(String name) {
		this.name = name;
		for (int i = 0; i < counters.length; i++) {
			counters[i] = new AtomicLong(0);
		}
	}

	public static void clear() {
		synchronized (STATS) {
			for (PerformanceStats stats : STATS) {
				stats.totalCount.set(0);
				stats.totalTime.set(0);
				for (AtomicLong c : stats.counters) {
					c.set(0);
				}
			}
		}
	}

	public static String statString() {
		long[] n = new long[STATS.size()];
		long[] c = new long[STATS.size()];
		long[] tt = new long[STATS.size()];
		long[] ttt = new long[STATS.size()];

		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (int j = 0; j < STATS.size(); j++) {
			sb.append(String.format("\t%24s", STATS.get(j).name));
			tt[j] = STATS.get(j).totalCount.get();
			ttt[j] = STATS.get(j).totalTime.get();
		}
		sb.append("\n");
		for (int i = 0; i < 21; i++) {
			if (i == 20) {
				sb.append(String.format(">%dms\t", i * STEP));
			} else {
				sb.append(String.format("%dms\t", i * STEP));
			}
			for (int j = 0; j < STATS.size(); j++) {
				c[j] = STATS.get(j).counters[i].get();
				n[j] += c[j];
				sb.append(String.format("%12d%12.2f/%-12.2f", c[j], c[j] * 100.0 / tt[j], n[j] * 100.0 / tt[j]));
			}
			sb.append("\n");
		}
		sb.append(String.format("%s\t", "Total:"));
		for (int j = 0; j < STATS.size(); j++) {
			sb.append(String.format("%24d", tt[j]));
		}
		sb.append("\n");
		sb.append(String.format("%s\t", "AVG:"));
		for (int j = 0; j < STATS.size(); j++) {
			sb.append(String.format("%25d", tt[j]));
		}
		sb.append("\n");
		return sb.toString();
	}

	synchronized public static void startLoggingThread(long step) {
		if (null == STAT_THREAD) {
			STEP = step;
			STAT_THREAD = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							break;
						}
						PERFORMANCE_LOGGER.info(statString());
					}
				}
			}, "PerformanceStats");
			STAT_THREAD.setDaemon(true);
			STAT_THREAD.start();
		}
	}

	synchronized public static PerformanceStats newStat(String name) {
		PerformanceStats stat = new PerformanceStats(name);
		STATS.add(stat);
		return stat;
	}

	public static void setStep(long step) {
		STEP = step;
		clear();
	}

	public void stat(long cost) {
		totalTime.addAndGet(cost);
		totalCount.incrementAndGet();
		int i = (int) (cost / STEP);
		if (i > 20) {
			i = 20;
		}
		counters[i].incrementAndGet();
	}

}
