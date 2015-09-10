package org.sothis.mvc;

import java.util.Iterator;
import java.util.Map;

public interface Attachments extends Iterable<Map.Entry<String, Object>> {

	Object get(String name);

	Iterator<String> names();

	boolean isEmpty();
}
