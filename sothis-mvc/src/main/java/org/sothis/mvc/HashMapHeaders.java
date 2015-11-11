package org.sothis.mvc;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.ArrayUtils;

public class HashMapHeaders extends Headers {
	private HashMap<String, String[]> headers = new HashMap<>();

	@Override
	public Iterator<String> names() {
		return headers.keySet().iterator();
	}

	@Override
	public String[] getStrings(String name) {
		return headers.get(name);
	}

	@Override
	public void addString(String name, String value) {
		String[] values = headers.get(name);
		if (null == values) {
			values = new String[] { value };
		} else {
			values = ArrayUtils.add(values, value);
		}
		headers.put(name, values);
	}

	@Override
	public void setString(String name, String value) {
		headers.put(name, new String[] { value });
	}

	@Override
	public void remove(String name) {
		headers.remove(name);
	}

	@Override
	public boolean contains(String name) {
		return headers.containsKey(name);
	}

}
