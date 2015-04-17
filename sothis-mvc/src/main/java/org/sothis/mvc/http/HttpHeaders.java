package org.sothis.mvc.http;

import java.text.ParsePosition;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.sothis.core.util.ThreadSafeDateFormat;

public abstract class HttpHeaders implements Iterable<Map.Entry<String, String[]>> {

	protected static final ThreadSafeDateFormat FORMAT1 = new ThreadSafeDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH,
			TimeZone.getTimeZone("GMT"));
	protected static final ThreadSafeDateFormat FORMAT2 = new ThreadSafeDateFormat("E, dd-MMM-yy HH:mm:ss z", Locale.ENGLISH,
			TimeZone.getTimeZone("GMT"));
	protected static final ThreadSafeDateFormat FORMAT3 = new ThreadSafeDateFormat("E MMM d HH:mm:ss yyyy", Locale.ENGLISH,
			TimeZone.getTimeZone("GMT"));

	public abstract Iterator<String> names();

	public abstract String[] getStrings(String name);

	public abstract void addString(String name, String value);

	public abstract void setString(String name, String value);

	public abstract void remove(String name);

	public abstract boolean contains(String name);

	public Iterator<Entry<String, String[]>> iterator() {
		return new HeaderIterator(names());
	}

	public void setStrings(String name, String[] value) {
		remove(name);
		for (String v : value) {
			addString(name, v);
		}
	}

	public void addDate(String name, Date date) {
		addString(name, FORMAT1.format(date));
	}

	public void setDate(String name, Date date) {
		setString(name, FORMAT1.format(date));
	}

	public void addInteger(String name, Integer value) {
		addString(name, value.toString());
	}

	public void setInteger(String name, Integer value) {
		setString(name, value.toString());
	}

	public String getString(String name) {
		String[] values = getStrings(name);
		return (null == values || values.length == 0) ? null : values[0];
	}

	public String getString(String name, String defaultValue) {
		String ret = getString(name);
		return null == ret ? defaultValue : ret;
	}

	public Integer getInteger(String name) {
		String value = getString(name);
		return null == value ? null : Integer.parseInt(value);
	}

	public Integer getInteger(String name, Integer defaultValue) {
		String value = getString(name);
		return null == value ? defaultValue : Integer.parseInt(value);
	}

	public Integer[] getIntegers(String name) {
		String[] values = getStrings(name);
		Integer[] integers;
		if (values.length > 0) {
			integers = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				integers[i] = Integer.parseInt(values[i]);
			}
		} else {
			integers = new Integer[0];
		}
		return integers;
	}

	public Date getDate(String name) {
		Date[] values = getDates(name);
		return (null == values || values.length == 0) ? null : values[0];
	}

	public Date getDate(String name, Date defaultValue) {
		Date value = getDate(name);
		return null == value ? defaultValue : value;
	}

	public Date[] getDates(String name) {
		String[] values = getStrings(name);
		Date[] dates;
		if (values.length > 0) {
			dates = new Date[values.length];
			ParsePosition pos = new ParsePosition(0);
			for (int i = 0; i < values.length; i++) {
				dates[i] = FORMAT1.parse(values[i], pos);
				if (dates[i] == null) {
					dates[i] = FORMAT2.parse(values[i], pos);
					if (dates[i] == null) {
						dates[i] = FORMAT3.parse(values[i], pos);
					}
				}
			}
		} else {
			dates = new Date[0];
		}
		return dates;
	}

	private class HeaderIterator implements Iterator<Entry<String, String[]>> {
		private final Iterator<String> headerNames;

		public HeaderIterator(Iterator<String> headerNames) {
			super();
			this.headerNames = headerNames;
		}

		@Override
		public boolean hasNext() {
			return headerNames.hasNext();
		}

		@Override
		public Entry<String, String[]> next() {
			String name = headerNames.next();
			return new HeaderEntry(name);
		}

		@Override
		public void remove() {
			headerNames.remove();
		}

	}

	private class HeaderEntry implements Entry<String, String[]> {
		private final String name;
		private String[] value;

		public HeaderEntry(String name) {
			super();
			this.name = name;
		}

		@Override
		public String getKey() {
			return name;
		}

		@Override
		public String[] getValue() {
			if (null == value) {
				value = getStrings(name);
			}
			return value;
		}

		@Override
		public String[] setValue(String[] value) {
			String[] old = this.value;
			setStrings(name, value);
			this.value = value;
			return old;
		}

	}

}
