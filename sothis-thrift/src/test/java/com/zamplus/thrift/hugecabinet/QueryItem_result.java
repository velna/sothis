package com.zamplus.thrift.hugecabinet;

import org.sothis.thrift.TException;
import org.sothis.thrift.protocol.TEntity;
import org.sothis.thrift.protocol.TField;
import org.sothis.thrift.protocol.TProtocol;
import org.sothis.thrift.protocol.TType;

public class QueryItem_result implements TEntity {
	private final static short FID_RESULT = 0;
	private final static TField FIELD_RESULT = new TField("result", TType.STRUCT, FID_RESULT);

	private QueryItemANS result;

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
			case FID_RESULT:
				if (field.type == TType.STRUCT) {
					result = new QueryItemANS();
					result.read(protocol);
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
	}

	@Override
	public void write(TProtocol protocol) throws TException {
		// TODO Auto-generated method stub

	}

}
