package org.sothis.nios.codec;

public class DecodeException extends RuntimeException {

	public DecodeException() {
		super();
	}

	public DecodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DecodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public DecodeException(String message) {
		super(message);
	}

	public DecodeException(Throwable cause) {
		super(cause);
	}

}
