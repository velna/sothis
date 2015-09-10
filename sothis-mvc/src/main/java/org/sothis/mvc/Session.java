package org.sothis.mvc;

import java.util.Date;

public interface Session {
	String getId();

	Date getDateCreated();

	Date getLastAccessed();

	Attributes attributes();

	void invalidate();
}
