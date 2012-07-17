package org.sothis.core.util;

import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformaceDebugger {
	private final static Logger LOGGER = LoggerFactory.getLogger(PerformaceDebugger.class);

	private final String name;
	private long startTime;
	private long stepTime;
	private final Map<String, Long> steps;
	private final Map<String, Long> coreSteps;
	private int maxStepNameLength;
	private int maxCoreStepNameLength;

	private final static ThreadLocal<PerformaceDebugger> THREAD_LOCAL = new ThreadLocal<PerformaceDebugger>() {

		@Override
		protected PerformaceDebugger initialValue() {
			return new PerformaceDebugger(Thread.currentThread().getName());
		}

	};

	@SuppressWarnings("unchecked")
	public PerformaceDebugger(String name) {
		this.name = name;
		this.startTime = System.currentTimeMillis();
		this.stepTime = this.startTime;
		this.steps = new ListOrderedMap();
		this.coreSteps = new ListOrderedMap();
	}

	public static PerformaceDebugger getThreadLocalInstance() {
		return THREAD_LOCAL.get();
	}

	public void step(String stepName) {
		long t = System.currentTimeMillis();
		steps.put(stepName, t - this.stepTime);
		this.stepTime = t;
		maxStepNameLength = Math.max(maxStepNameLength, stepName.length());
	}

	public void step(String stepName, long maxTime) {
		long t = System.currentTimeMillis();
		long st = t - this.stepTime;
		steps.put(stepName, st);
		if (st > maxTime) {
			coreSteps.put(stepName, st);
		}
		this.stepTime = t;
		maxStepNameLength = Math.max(maxStepNameLength, stepName.length());
		maxCoreStepNameLength = Math.max(maxCoreStepNameLength, stepName.length());
	}

	public void end(long maxTime) {
		long t = System.currentTimeMillis() - this.startTime;
		if (LOGGER.isErrorEnabled()) {
			if (t > maxTime) {
				LOGGER.error(toString(this.steps, t, this.maxStepNameLength));
			} else {
				LOGGER.error(toString(this.coreSteps, t, this.maxCoreStepNameLength));
			}
		}
	}

	public void reset() {
		this.startTime = System.currentTimeMillis();
		this.stepTime = this.startTime;
		this.steps.clear();
		this.coreSteps.clear();
		this.maxStepNameLength = 0;
		this.maxCoreStepNameLength = 0;
	}

	public void remove() {
		THREAD_LOCAL.remove();
	}

	private String toString(Map<String, Long> steps, long t, int maxStepNameLength) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.name).append(" total time: ").append(t).append(" ms\n");
		for (Map.Entry<String, Long> entry : steps.entrySet()) {
			sb.append(pad(entry.getKey(), maxStepNameLength)).append(" : ").append(entry.getValue()).append(" ms\n");
		}
		return sb.toString();
	}

	private String pad(String s, int length) {
		if (null == s) {
			return s;
		}
		int sLength = s.length();
		if (sLength >= length) {
			return s;
		}
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < length - sLength; i++) {
			ret.append(' ');
		}
		ret.append(s);
		return ret.toString();
	}

}
