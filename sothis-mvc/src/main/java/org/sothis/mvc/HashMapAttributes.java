package org.sothis.mvc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HashMapAttributes implements Attributes {
	protected Map<String, Object> attributes = new HashMap<>();

	public Object get(String name) {
		return attributes.get(name);
	}

	public void set(String name, Object value) {
		attributes.put(name, value);
	}

	public void remove(String name) {
		attributes.remove(name);
	}

	public Iterator<String> names() {
		return attributes.keySet().iterator();
	}

	@Override
	public Iterator<Entry<String, Object>> iterator() {
		return attributes.entrySet().iterator();
	}

}
