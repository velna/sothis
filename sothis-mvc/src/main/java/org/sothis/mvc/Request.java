package org.sothis.mvc;

import java.io.IOException;
import java.io.InputStream;

public interface Request extends Attributed, Parameterized, Attachable {

	public static final String DEFAULT_CHARSET = "UTF-8";

	String getUri();

	String getUriPath();

	InputStream getInputStream() throws IOException;

	Session getSession();

	Session getSession(boolean create);
}
