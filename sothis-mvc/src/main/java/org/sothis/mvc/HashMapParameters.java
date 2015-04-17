package org.sothis.mvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HashMapParameters extends Parameters {

	protected final Map<String, String[]> params;

	public HashMapParameters(Map<String, String[]> params) {
		super();
		this.params = params;
	}

	@Override
	public String[] getStrings(String name) {
		String[] strings = params.get(name);
		return null == strings ? null : Arrays.copyOf(strings, strings.length);
	}

	@Override
	public Iterator<String> names() {
		return Collections.unmodifiableSet(params.keySet()).iterator();
	}

	@Override
	public Iterator<Entry<String, String[]>> iterator() {
		return Collections.unmodifiableMap(params).entrySet().iterator();
	}

}
