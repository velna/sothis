package com.zamplus.thrift.hugecabinet;

import org.sothis.thrift.TException;
import org.sothis.thrift.protocol.TEntity;
import org.sothis.thrift.protocol.TField;
import org.sothis.thrift.protocol.TProtocol;
import org.sothis.thrift.protocol.TStruct;
import org.sothis.thrift.protocol.TType;

public class QueryItemREQ implements TEntity {
	private static final TStruct STRUCT_DESC = new TStruct("QueryItemREQ");
	private final static short FID_KEY = 1;
	private final static TField FIELD_KEY = new TField("key", TType.STRING, FID_KEY);

	private final byte[] key;

	public QueryItemREQ(byte[] key) {
		this.key = key;
	}

	@Override
	public void write(TProtocol protocol) throws TException {
		protocol.writeStructBegin(STRUCT_DESC);

		if (key != null) {
			protocol.writeFieldBegin(FIELD_KEY);
			protocol.writeBinary(key, 0, key.length);
			protocol.writeFieldEnd();
		}

		protocol.writeFieldStop();
		protocol.writeStructEnd();
	}

	@Override
	public void read(TProtocol protocol) throws TException {
		// TODO Auto-generated method stub

	}

}
