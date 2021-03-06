package org.sothis.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public interface Request extends Attributed, Parameterized, Attachable {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	String getMethod();

	String getUri();

	String getUriPath();

	InputStream getInputStream() throws IOException;

	Session getSession();

	Session getSession(boolean create);

	String getLocalAddr();

	int getLocalPort();

	String getRemoteAddr();

	int getRemotePort();

	Headers headers();

	String getProtocol();

	String getScheme();

	Charset getCharset();

	void setCharset(Charset charset) throws UnsupportedEncodingException;

}
