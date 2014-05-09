package com.zamplus.thrift.hugecabinet;

import org.sothis.thrift.TException;
import org.sothis.thrift.protocol.TEntity;
import org.sothis.thrift.protocol.TField;
import org.sothis.thrift.protocol.TProtocol;
import org.sothis.thrift.protocol.TProtocolException;
import org.sothis.thrift.protocol.TStruct;
import org.sothis.thrift.protocol.TType;

public class QueryItemANS implements TEntity {

	private final static TStruct STRUCT_DESC = new TStruct("QueryItemANS");
	private final static short FID_CODE = 1;
	private final static short FID_KEY = 2;
	private final static short FID_VALUE = 3;
	private final static TField FIELD_CODE = new TField("code", TType.I32, FID_CODE);
	private final static TField FIELD_KEY = new TField("key", TType.STRING, FID_KEY);
	private final static TField FIELD_VALUE = new TField("value", TType.STRING, FID_VALUE);

	private int code;
	private byte[] key;
	private byte[] value;

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
			case FID_CODE:
				if (field.type == TType.I32) {
					code = protocol.readI32();
				} else {
					protocol.skip(field.type);
				}
				break;
			case FID_KEY:
				if (field.type == TType.STRING) {
					key = protocol.readBinary();
				} else {
					protocol.skip(field.type);
				}
				break;
			case FID_VALUE:
				if (field.type == TType.STRING) {
					value = protocol.readBinary();
				} else {
					protocol.skip(field.type);
				}
				break;
			default:
				protocol.skip(field.type);
			}
			protocol.readFieldEnd();
		}
		protocol.readStructEnd();
		if (null == key) {
			throw new TProtocolException("key not found");
		}
		if (null == value) {
			throw new TProtocolException("value not found");
		}
	}

	@Override
	public void write(TProtocol protocol) throws TException {
		protocol.writeStructBegin(STRUCT_DESC);

		protocol.writeFieldBegin(FIELD_CODE);
		protocol.writeI32(code);
		protocol.writeFieldEnd();

		if (key != null) {
			protocol.writeFieldBegin(FIELD_KEY);
			protocol.writeBinary(key, 0, key.length);
			protocol.writeFieldEnd();
		}

		if (value != null) {
			protocol.writeFieldBegin(FIELD_VALUE);
			protocol.writeBinary(value, 0, value.length);
			protocol.writeFieldEnd();
		}

		protocol.writeFieldStop();
		protocol.writeStructEnd();
	}

	public int getCode() {
		return code;
	}

	public byte[] getKey() {
		return key;
	}

	public byte[] getValue() {
		return value;
	}

}
