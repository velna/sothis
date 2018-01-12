package org.sothis.mvc;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Attachments implements Iterable<Map.Entry<String, Collection<Attachment>>> {

	public static final Attachments EMPTY = new Attachments() {

		@Override
		protected Map<String, Collection<Attachment>> getAttachments() {
			return Collections.emptyMap();
		}

	};

	protected abstract Map<String, Collection<Attachment>> getAttachments();

	public Collection<Attachment> getAttachments(String name) {
		return getAttachments().get(name);
	}

	public Attachment get(String name) {
		Collection<Attachment> atts = getAttachments().get(name);
		return atts.isEmpty() ? null : atts.iterator().next();
	}

	@Override
	public Iterator<Entry<String, Collection<Attachment>>> iterator() {
		return this.getAttachments().entrySet().iterator();
	}

	public Iterator<String> names() {
		return this.getAttachments().keySet().iterator();
	}

	public boolean isEmpty() {
		return this.getAttachments().isEmpty();
	}

}
