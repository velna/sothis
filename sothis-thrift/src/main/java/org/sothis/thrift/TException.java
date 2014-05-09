package org.sothis.thrift;

public class TException extends Exception {

	public TException() {
		super();
	}

	public TException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TException(String message, Throwable cause) {
		super(message, cause);
	}

	public TException(String message) {
		super(message);
	}

	public TException(Throwable cause) {
		super(cause);
	}

}
