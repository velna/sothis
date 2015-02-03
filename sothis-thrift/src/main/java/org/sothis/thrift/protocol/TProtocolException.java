package org.sothis.thrift.protocol;

import org.sothis.thrift.TException;

public class TProtocolException extends TException {

	public static final int UNKNOWN = 0;
	public static final int INVALID_DATA = 1;
	public static final int NEGATIVE_SIZE = 2;
	public static final int SIZE_LIMIT = 3;
	public static final int BAD_VERSION = 4;
	public static final int NOT_IMPLEMENTED = 5;

	protected int type_ = UNKNOWN;

	public TProtocolException() {
		super();
	}

	public TProtocolException(int type) {
		super();
		type_ = type;
	}

	public TProtocolException(int type, String message) {
		super(message);
		type_ = type;
	}

	public TProtocolException(String message) {
		super(message);
	}

	public TProtocolException(int type, Throwable cause) {
		super(cause);
		type_ = type;
	}

	public TProtocolException(Throwable cause) {
		super(cause);
	}

	public TProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	public TProtocolException(int type, String message, Throwable cause) {
		super(message, cause);
		type_ = type;
	}

	public int getType() {
		return type_;
	}

}
