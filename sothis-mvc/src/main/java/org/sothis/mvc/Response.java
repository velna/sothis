package org.sothis.mvc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public interface Response {

	OutputStream getOutputStream() throws IOException;

	PrintWriter getWriter() throws IOException;

	Headers headers();

	String getProtocolVersion();

	void setProtocolVersion(String protocolVersion);

	String getCharset();

	void setCharset(String charset) throws UnsupportedEncodingException;

	int getStatus();

	void setStatus(int status);

	boolean isCommitted();

	void reset();

}
