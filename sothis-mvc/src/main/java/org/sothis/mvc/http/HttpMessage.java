package org.sothis.mvc.http;

import java.io.UnsupportedEncodingException;

public interface HttpMessage {

	HttpHeaders headers();

	String getProtocolVersion();

	String getCharset();

	void setCharset(String charset) throws UnsupportedEncodingException;

}
