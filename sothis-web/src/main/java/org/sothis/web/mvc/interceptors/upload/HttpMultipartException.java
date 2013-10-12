package org.sothis.web.mvc.interceptors.upload;

public class HttpMultipartException extends Exception {

	private static final long serialVersionUID = 6227465230053675968L;

	public HttpMultipartException() {
	}

	public HttpMultipartException(String message) {
		super(message);
	}

	public HttpMultipartException(Throwable cause) {
		super(cause);
	}

	public HttpMultipartException(String message, Throwable cause) {
		super(message, cause);
	}

}
