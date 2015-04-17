package org.sothis.mvc;

import java.io.UnsupportedEncodingException;

public interface Parameterized {

	String getCharset();

	void setCharset(String charset) throws UnsupportedEncodingException;

	String getQueryString();

	Parameters parameters();
}
