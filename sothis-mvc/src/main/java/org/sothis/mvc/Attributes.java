package org.sothis.mvc;

import java.util.Iterator;
import java.util.Map;

public interface Attributes extends Iterable<Map.Entry<String, Object>> {

	public Object get(String name);

	public void set(String name, Object value);

	public void remove(String name);

	public Iterator<String> names();

}
