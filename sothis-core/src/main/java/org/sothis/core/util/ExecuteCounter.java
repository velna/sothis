package org.sothis.core.util;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 执行计数器，可以用来对当前线程下的某些方法进行计数统计
 * 
 * @author velna
 * 
 */
public class ExecuteCounter {
	public final CounterAndTimer totalCAndT = new CounterAndTimer();
	private final ConcurrentHashMap<Object, SubCounter> entityCounterMap = new ConcurrentHashMap<Object, SubCounter>();

	private class CounterAndTimer {
		public final AtomicLong counter = new AtomicLong(0);
		public final AtomicLong timer = new AtomicLong(0);

		@Override
		public String toString() {
			return new StringBuilder().append('{').append(this.counter.get()).append(", ").append(timer.get())
					.append("ms}").toString();
		}
	}

	private class SubCounter {
		private final ConcurrentHashMap<Object, CounterAndTimer> cAndTMap = new ConcurrentHashMap<Object, CounterAndTimer>();
		private final CounterAndTimer totalCAndT = new CounterAndTimer();

		public void increase(Object subKey, long time) {
			this.totalCAndT.counter.incrementAndGet();
			this.totalCAndT.timer.addAndGet(time);
			CounterAndTimer cAndT = this.cAndTMap.get(subKey);
			if (null == cAndT) {
				cAndT = new CounterAndTimer();
				this.cAndTMap.put(subKey, cAndT);
			}
			cAndT.counter.incrementAndGet();
			cAndT.timer.addAndGet(time);
		}

		@Override
		public String toString() {
			StringBuilder ret = new StringBuilder();
			ret.append("{total: ").append(this.totalCAndT);
			ret.append(", detail: {");
			for (Iterator<Object> i = this.cAndTMap.keySet().iterator(); i.hasNext();) {
				Object key = i.next();
				CounterAndTimer cAndT = this.cAndTMap.get(key);
				ret.append(key).append('=').append(cAndT);
				if (i.hasNext()) {
					ret.append(',');
				}
			}
			ret.append("}}");
			return ret.toString();
		}

	}

	/**
	 * 对指定的key进行1次计数
	 * 
	 * @param key
	 * @return
	 */
	public final void increase(Object key, Object subKey, long time) {
		totalCAndT.counter.incrementAndGet();
		totalCAndT.timer.addAndGet(time);
		SubCounter subCounter = this.entityCounterMap.get(key);
		if (null == subCounter) {
			subCounter = new SubCounter();
			this.entityCounterMap.put(key, subCounter);
		}
		subCounter.increase(subKey, time);
	}

	/**
	 * 得到一个基于thread local的实例
	 * 
	 * @return
	 */
	public static final ExecuteCounter getThreadLocalInstance(Object key) {
		ThreadLocalData threadLocalData = ThreadLocalData.getInstance();
		ExecuteCounter executeCounter = (ExecuteCounter) threadLocalData.get(key);
		if (null == executeCounter) {
			executeCounter = new ExecuteCounter();
			threadLocalData.put(key, executeCounter);
		}
		return executeCounter;
	}

	public long getTotalCount() {
		return this.totalCAndT.counter.get();
	}

	public long getTotalTime() {
		return this.totalCAndT.timer.get();
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("{total: ").append(this.totalCAndT);
		ret.append(", detail: {");
		for (Iterator<Object> i = this.entityCounterMap.keySet().iterator(); i.hasNext();) {
			Object key = i.next();
			SubCounter subCounter = this.entityCounterMap.get(key);
			ret.append(key).append('=').append(subCounter);
			if (i.hasNext()) {
				ret.append(',');
			}
		}
		ret.append("}}");
		return ret.toString();
	}

}
