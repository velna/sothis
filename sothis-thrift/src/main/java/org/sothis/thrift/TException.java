package org.sothis.thrift;

public class TException extends Exception {
	private final TError error;

	public TException(TError error) {
		super();
		this.error = error;
	}

	public TError getError() {
		return error;
	}

}
