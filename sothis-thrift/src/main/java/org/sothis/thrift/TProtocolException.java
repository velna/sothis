package org.sothis.thrift;

public class TProtocolException extends RuntimeException {

	public TProtocolException() {
		super();
	}

	public TProtocolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	public TProtocolException(String message) {
		super(message);
	}

	public TProtocolException(Throwable cause) {
		super(cause);
	}

}
