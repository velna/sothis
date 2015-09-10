package org.sothis.mvc.http.servlet;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.sothis.mvc.Attachments;

public abstract class ServletAttachments implements Attachments {

	static final ServletAttachments EMPTY = new ServletAttachments() {

		@Override
		protected Map<String, Object> getAttachments() {
			return Collections.emptyMap();
		}

	};

	protected abstract Map<String, Object> getAttachments();

	@Override
	public Iterator<Entry<String, Object>> iterator() {
		return getAttachments().entrySet().iterator();
	}

	@Override
	public Object get(String name) {
		return getAttachments().get(name);
	}

	@Override
	public Iterator<String> names() {
		return getAttachments().keySet().iterator();
	}

	@Override
	public boolean isEmpty() {
		return getAttachments().isEmpty();
	}

}
