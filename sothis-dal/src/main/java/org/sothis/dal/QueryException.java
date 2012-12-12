package org.sothis.dal;

public class QueryException extends RuntimeException {

	public QueryException() {
	}

	public QueryException(String message) {
		super(message);
	}

	public QueryException(Throwable cause) {
		super(cause);
	}

	public QueryException(String message, Throwable cause) {
		super(message, cause);
	}

}
