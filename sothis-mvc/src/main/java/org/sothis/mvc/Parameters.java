package org.sothis.mvc;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Parameters implements Iterable<Map.Entry<String, Object[]>> {

	public abstract Object[] getValues(String name);

	public abstract Iterator<String> names();

	public abstract Map<String, Object[]> toMap();

	@Override
	public abstract Iterator<Entry<String, Object[]>> iterator();

	public Object get(String name) {
		return get(name, null);
	}

	public Object get(String name, Object defaultValue) {
		Object[] values = getValues(name);
		return (null == values || values.length == 0 || null == values[0]) ? defaultValue : values[0];
	}

	public String getString(String name) {
		return getString(name, null);
	}

	public String getString(String name, String defaultValue) {
		Object value = get(name);
		return null == value ? defaultValue : (value instanceof String ? (String) value : value.toString());
	}

	public String[] getStrings(String name) {
		Object[] values = getValues(name);
		if (values instanceof String[]) {
			return (String[]) values;
		}
		String[] ret = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			ret[i] = null == values[i] ? null : values[i].toString();
		}
		return ret;
	}

	public Boolean getBoolean(String name) {
		return getBoolean(name, null);
	}

	public Boolean getBoolean(String name, Boolean defaultValue) {
		Object value = get(name);
		return null == value ? defaultValue : (value instanceof Boolean ? (Boolean) value : Boolean.valueOf(value.toString()));
	}

	public Boolean[] getBooleans(String name) {
		Object[] values = getValues(name);
		if (values instanceof Boolean[]) {
			return (Boolean[]) values;
		}
		Boolean[] ret = new Boolean[values.length];
		for (int i = 0; i < values.length; i++) {
			ret[i] = null == values[i] ? null : Boolean.valueOf(values[i].toString());
		}
		return ret;
	}

	public Integer getInteger(String name) {
		return getInteger(name, null);
	}

	public Integer getInteger(String name, Integer defaultValue) {
		Object value = get(name);
		return null == value ? defaultValue : (value instanceof Integer ? (Integer) value : Integer.valueOf(value.toString()));
	}

	public Integer[] getIntegers(String name) {
		Object[] values = getValues(name);
		if (values instanceof Integer[]) {
			return (Integer[]) values;
		}
		Integer[] ret = new Integer[values.length];
		for (int i = 0; i < values.length; i++) {
			ret[i] = null == values[i] ? null : Integer.valueOf(values[i].toString());
		}
		return ret;
	}

	public Long getLong(String name) {
		return getLong(name, null);
	}

	public Long getLong(String name, Long defaultValue) {
		Object value = get(name);
		return null == value ? defaultValue : (value instanceof Long ? (Long) value : Long.valueOf(value.toString()));
	}

	public Long[] getLongs(String name) {
		Object[] values = getValues(name);
		if (values instanceof Long[]) {
			return (Long[]) values;
		}
		Long[] ret = new Long[values.length];
		for (int i = 0; i < values.length; i++) {
			ret[i] = null == values[i] ? null : Long.valueOf(values[i].toString());
		}
		return ret;
	}

	public Double getDouble(String name) {
		return getDouble(name, null);
	}

	public Double getDouble(String name, Double defaultValue) {
		Object value = get(name);
		return null == value ? defaultValue : (value instanceof Double ? (Double) value : Double.valueOf(value.toString()));
	}

	public Double[] getDoubles(String name) {
		Object[] values = getValues(name);
		if (values instanceof Double[]) {
			return (Double[]) values;
		}
		Double[] ret = new Double[values.length];
		for (int i = 0; i < values.length; i++) {
			ret[i] = null == values[i] ? null : Double.valueOf(values[i].toString());
		}
		return ret;
	}

}
