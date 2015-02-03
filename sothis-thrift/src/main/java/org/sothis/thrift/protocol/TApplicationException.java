package org.sothis.thrift.protocol;

import org.sothis.thrift.TException;

public class TApplicationException extends TException implements TEntity {

	private static final TStruct STRUCT_DESC = new TStruct("TApplicationException");
	private final static short FID_MESSAGE = 1;
	private final static short FID_TYPE = 2;
	private static final TField MESSAGE_FIELD = new TField("message", TType.STRING, FID_MESSAGE);
	private static final TField TYPE_FIELD = new TField("type", TType.I32, FID_TYPE);

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

	public TApplicationException() {
	}

	@Override
	public void read(TProtocol protocol) throws TException {
		TField field;
		protocol.readStructBegin();

		while (true) {
			field = protocol.readFieldBegin();
			if (field.type == TType.STOP) {
				break;
			}
			switch (field.id) {
			case FID_MESSAGE:
				if (field.type == TType.STRING) {
					message = protocol.readString();
				} else {
					protocol.skip(field.type);
				}
				break;
			case FID_TYPE:
				if (field.type == TType.I32) {
					type = protocol.readI32();
				} else {
					protocol.skip(field.type);
				}
				break;
			default:
				protocol.skip(field.type);
				break;
			}
			protocol.readFieldEnd();
		}
		protocol.readStructEnd();

	}

	@Override
	public void write(TProtocol protocol) throws TException {
		protocol.writeStructBegin(STRUCT_DESC);
		if (message != null) {
			protocol.writeFieldBegin(MESSAGE_FIELD);
			protocol.writeString(message);
			protocol.writeFieldEnd();
		}
		protocol.writeFieldBegin(TYPE_FIELD);
		protocol.writeI32(type);
		protocol.writeFieldEnd();
		protocol.writeFieldStop();
		protocol.writeStructEnd();
	}

	public String getMessage() {
		return message;
	}

	public int getType() {
		return type;
	}

}
