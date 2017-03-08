package org.sothis.core.util.bwlist;

public class SourceLoadException extends RuntimeException {

	public SourceLoadException() {
		super();
	}

	public SourceLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SourceLoadException(String message, Throwable cause) {
		super(message, cause);
	}

	public SourceLoadException(String message) {
		super(message);
	}

	public SourceLoadException(Throwable cause) {
		super(cause);
	}

}
