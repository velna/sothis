package org.sothis.mvc.http;

import org.sothis.mvc.Request;

public interface HttpRequest extends Request, HttpMessage {
	String getMethod();

	String getQueryString();

	String getLocalAddr();

	int getLocalPort();

	String getRemoteAddr();

	int getRemotePort();

	public static final class Methods {
		public static final String GET = "GET";
		public static final String POST = "POST";
		public static final String PUT = "PUT";
		public static final String DELETE = "DELETE";
		public static final String HEAD = "HEAD";
		public static final String CONNECT = "CONNECT";
		public static final String OPTIONS = "OPTIONS";
		public static final String TRACE = "TRACE";
	}
}
