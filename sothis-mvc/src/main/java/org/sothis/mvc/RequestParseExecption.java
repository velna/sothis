package org.sothis.mvc;

public class RequestParseExecption extends RuntimeException {

	public RequestParseExecption() {
	}

	public RequestParseExecption(String message) {
		super(message);
	}

	public RequestParseExecption(Throwable cause) {
		super(cause);
	}

	public RequestParseExecption(String message, Throwable cause) {
		super(message, cause);
	}

	public RequestParseExecption(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
