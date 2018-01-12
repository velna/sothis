package org.sothis.mvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HashMapParameters extends Parameters {

	protected final Map<String, Object[]> params;

	public HashMapParameters(Map<String, Object[]> params) {
		super();
		this.params = Collections.unmodifiableMap(params);
	}

	@Override
	public Iterator<String> names() {
		return params.keySet().iterator();
	}

	@Override
	public Iterator<Entry<String, Object[]>> iterator() {
		return params.entrySet().iterator();
	}

	@Override
	public Map<String, Object[]> toMap() {
		return params;
	}

	@Override
	public String toString() {
		return params.toString();
	}

	@Override
	public Object[] getValues(String name) {
		Object[] values = params.get(name);
		return null == values ? new Object[0] : Arrays.copyOf(values, values.length);
	}

}
