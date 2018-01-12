package org.sothis.mvc;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class HashMapAttachments extends Attachments {

	private final Map<String, Collection<Attachment>> attachments;

	public HashMapAttachments(Map<String, Collection<Attachment>> attachments) {
		super();
		this.attachments = Collections.unmodifiableMap(attachments);
	}

	@Override
	protected Map<String, Collection<Attachment>> getAttachments() {
		return attachments;
	}

}
