package org.sothis.thrift;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TError extends TResponse {

	public static final int UNKNOWN = 0;
	public static final int UNKNOWN_METHOD = 1;
	public static final int INVALID_MESSAGE_TYPE = 2;
	public static final int WRONG_METHOD_NAME = 3;
	public static final int BAD_SEQUENCE_ID = 4;
	public static final int MISSING_RESULT = 5;
	public static final int INTERNAL_ERROR = 6;
	public static final int PROTOCOL_ERROR = 7;
	public static final int INVALID_TRANSFORM = 8;
	public static final int INVALID_PROTOCOL = 9;
	public static final int UNSUPPORTED_CLIENT_TYPE = 10;

	private String message;
	private int type = UNKNOWN;

	public TError(int tid) {
		super(tid);
	}

	@Override
	public void decode(ByteBuffer buf) throws IOException {
		while (true) {
			byte fieldType = buf.get();
			if (fieldType == TType.STOP) {
				break;
			}
			short fieldId = buf.getShort();
			switch (fieldId) {
			case 1:
				if (fieldType == TType.STRING) {
					int size = buf.getInt();
					byte[] array = new byte[size];
					buf.get(array);
					message = new String(array, "UTF-8");
				} else {
					skip(buf, fieldType);
				}
				break;
			case 2:
				if (fieldType == TType.I32) {
					type = buf.getInt();
				} else {
					skip(buf, fieldType);
				}
				break;
			default:
				skip(buf, fieldType);
				break;
			}
		}
	}

	public String getMessage() {
		return message;
	}

	public int getType() {
		return type;
	}

}
