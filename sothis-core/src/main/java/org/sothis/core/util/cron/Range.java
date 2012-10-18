package org.sothis.core.util.cron;

import java.util.Calendar;

public class Range implements Matcher {
	private final int start;
	private final int end;
	private final int max;
	private final int min;

	public Range(int start, int end, int min, int max) {
		this.start = start;
		this.end = end;
		this.max = max;
		this.min = min;
		if (min > max) {
			throw new IllegalArgumentException("min must less than max");
		}
		if (start > max || start < min) {
			throw new IllegalArgumentException("start must between " + min + " and " + max + ": " + start);
		}
		if (end > max || end < min) {
			throw new IllegalArgumentException("end must between " + min + " and " + max + ": " + end);
		}
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	@Override
	public boolean matches(Calendar calendar, int field) {
		int value = calendar.get(field);
		if (value > max || value < min) {
			return false;
		}
		if (start < end) {
			return start <= value && value <= end;
		} else if (start > end) {
			return (start <= value && value <= max) || (min <= value && value <= end);
		} else {
			return start == value;
		}
	}
}
