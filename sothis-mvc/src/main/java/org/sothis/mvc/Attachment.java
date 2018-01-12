package org.sothis.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public interface Attachment {

	Charset getCharset();

	String getString(Charset charset) throws IOException;

	InputStream getInputStream() throws IOException;

	String getContentType();

	String getName();

	String getFilename();

	long getSize();

	void delete() throws IOException;

	Headers headers();
}
