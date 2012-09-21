package org.sothis.core.util;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalData {
	private final static ThreadLocalData instance = new ThreadLocalData();
	private final static ThreadLocal<Map<Object, Object>> tl = new ThreadLocal<Map<Object, Object>>();

	private ThreadLocalData() {

	}

	public static ThreadLocalData getInstance() {
		return instance;
	}

	public Object get(Object key) {
		Map<Object, Object> data = tl.get();
		if (data == null) {
			return null;
		}
		return data.get(key);
	}

	public void put(Object key, Object value) {
		Map<Object, Object> data = tl.get();
		if (data == null) {
			data = new HashMap<Object, Object>();
			tl.set(data);
		}
		data.put(key, value);
	}

	public void clear() {
		tl.remove();
	}

	public void remove(Object key) {
		tl.get().remove(key);
	}
}
