package org.sothis.mvc;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Parameters implements Iterable<Map.Entry<String, String[]>> {

	public abstract String[] getStrings(String name);

	public abstract Iterator<String> names();

	@Override
	public abstract Iterator<Entry<String, String[]>> iterator();

	public String getString(String name) {
		String[] values = getStrings(name);
		return (null == values || values.length == 0) ? null : values[0];
	}

	public String getString(String name, String defaultValue) {
		String ret = getString(name);
		return null == ret ? defaultValue : ret;
	}

	public Boolean getBoolean(String name) {
		String value = getString(name);
		return null == value ? null : Boolean.parseBoolean(value);
	}

	public Boolean getBoolean(String name, Boolean defaultValue) {
		String value = getString(name);
		return null == value ? defaultValue : Boolean.parseBoolean(value);
	}

	public Boolean[] getBooleans(String name) {
		String[] values = getStrings(name);
		Boolean[] booleans;
		if (values.length > 0) {
			booleans = new Boolean[values.length];
			for (int i = 0; i < values.length; i++) {
				booleans[i] = Boolean.parseBoolean(values[i]);
			}
		} else {
			booleans = new Boolean[0];
		}
		return booleans;
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

	public Long getLong(String name) {
		String value = getString(name);
		return null == value ? null : Long.parseLong(value);
	}

	public Long getLong(String name, Long defaultValue) {
		String value = getString(name);
		return null == value ? defaultValue : Long.parseLong(value);
	}

	public Long[] getLongs(String name) {
		String[] values = getStrings(name);
		Long[] longs;
		if (values.length > 0) {
			longs = new Long[values.length];
			for (int i = 0; i < values.length; i++) {
				longs[i] = Long.parseLong(values[i]);
			}
		} else {
			longs = new Long[0];
		}
		return longs;
	}

	public Double getDouble(String name) {
		String value = getString(name);
		return null == value ? null : Double.parseDouble(value);
	}

	public Double getDouble(String name, Double defaultValue) {
		String value = getString(name);
		return null == value ? defaultValue : Double.parseDouble(value);
	}

	public Double[] getDoubles(String name) {
		String[] values = getStrings(name);
		Double[] doubles;
		if (values.length > 0) {
			doubles = new Double[values.length];
			for (int i = 0; i < values.length; i++) {
				doubles[i] = Double.parseDouble(values[i]);
			}
		} else {
			doubles = new Double[0];
		}
		return doubles;
	}

}
