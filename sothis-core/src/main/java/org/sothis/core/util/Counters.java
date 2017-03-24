package org.sothis.core.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.sothis.core.util.Counters.Counter;

public class Counters implements Iterable<Counter> {

	public static final int OPT_TOTAL_ONLY = 0x01;
	public static final int OPT_NO_PRINT = 0x02;
	public static final int OPT_NO_RESET = 0x04;

	private Map<String, Counter> counterMap = new LinkedHashMap<>();

	private static String fullName(String group, String name) {
		String nm;
		if (null != group) {
			nm = group + "." + name;
		} else {
			nm = name;
		}
		return nm;
	}

	public Counter create(String group, String name, int opt) {
		String nm = fullName(group, name);
		if (counterMap.containsKey(nm)) {
			throw new IllegalArgumentException("counter with name " + nm + " already exists.");
		}
		Counter counter = new Counter(group, name, opt);
		counterMap.put(counter.getFullName(), counter);
		return counter;
	}

	public Counter remove(String group, String name) {
		String nm = fullName(group, name);
		return this.counterMap.remove(nm);
	}

	@Override
	public Iterator<Counter> iterator() {
		return counterMap.values().iterator();
	}

	public Counter get(String group, String name) {
		String nm = fullName(group, name);
		return counterMap.get(nm);
	}

	public void reset() {
		for (Counter counter : counterMap.values()) {
			counter.reset();
		}
	}

	public void snapshot() {
		for (Counter counter : counterMap.values()) {
			counter.snapshot();
		}
	}

	public void enable(String group, int opt) {
		for (Counter counter : counterMap.values()) {
			if (StringUtils.equals(counter.getGroup(), group)) {
				counter.enable(opt);
			}
		}
	}

	public void disable(String group, int opt) {
		for (Counter counter : counterMap.values()) {
			if (StringUtils.equals(counter.getGroup(), group)) {
				counter.disable(opt);
			}
		}
	}

	public void print(PrintStream out) {
		for (Counter counter : counterMap.values()) {
			counter.print(out);
		}
	}

	public int size() {
		return this.counterMap.size();
	}

	@Override
	public String toString() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(out);
		for (Counter counter : counterMap.values()) {
			counter.print(stream);
		}
		return out.toString();
	}

	public static class Counter {
		private long snapshotTimestamp;
		private long lastTimestamp;
		private final long initTimestamp;
		private final String group;
		private final String name;
		private final String fullName;
		private int opt;
		private final AtomicLong value = new AtomicLong(0);
		private long snapshot;
		private long last;

		private Counter(String group, String name, int opt) {
			super();
			this.group = group;
			this.name = name;
			fullName = fullName(group, name);
			this.opt = opt;
			this.initTimestamp = this.lastTimestamp = this.snapshotTimestamp = System.currentTimeMillis();
		}

		public long getSnapshot() {
			return snapshot;
		}

		public long getLast() {
			return last;
		}

		public String getName() {
			return name;
		}

		public long getValue() {
			return value.get();
		}

		public long add(long delta) {
			return value.getAndAdd(delta);
		}

		public long incr() {
			return value.getAndIncrement();
		}

		public long decr() {
			return value.getAndDecrement();
		}

		public Counter set(long newValue) {
			value.set(newValue);
			return this;
		}

		public Counter reset() {
			if ((opt & OPT_NO_RESET) == 0) {
				set(0);
				last = snapshot = 0;
			}
			return this;
		}

		public Counter snapshot() {
			last = snapshot;
			snapshot = value.get();
			this.lastTimestamp = this.snapshotTimestamp;
			this.snapshotTimestamp = System.currentTimeMillis();
			return this;
		}

		public long avg() {
			long diffTotal = (this.snapshotTimestamp - this.initTimestamp) / 1000;
			if (diffTotal == 0) {
				diffTotal = 1;
			}
			return this.snapshot / diffTotal;
		}

		public long inst() {
			long diff = (this.snapshotTimestamp - this.lastTimestamp) / 1000;
			if (diff == 0) {
				diff = 1;
			}
			return (snapshot - last) / diff;
		}

		public Counter print(PrintStream out) {
			if ((opt & OPT_NO_PRINT) == 0 && snapshot > 0) {
				if ((opt & OPT_TOTAL_ONLY) != 0) {
					out.format("%s:\t%s\n", fullName, snapshot);
				} else {
					out.format("%s:\t%s\t%s\t%s\n", fullName, snapshot, avg(), inst());
				}
			}
			return this;
		}

		@Override
		public String toString() {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			print(new PrintStream(out));
			return out.toString();
		}

		public int getOpt() {
			return opt;
		}

		public Counter enable(int opt) {
			this.opt |= opt;
			return this;
		}

		public Counter disable(int opt) {
			this.opt &= ~opt;
			return this;
		}

		public String getGroup() {
			return group;
		}

		public String getFullName() {
			return fullName;
		}
	}

}
