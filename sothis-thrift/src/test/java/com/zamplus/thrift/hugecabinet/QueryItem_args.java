package com.zamplus.thrift.hugecabinet;

import org.sothis.thrift.TException;
import org.sothis.thrift.protocol.TEntity;
import org.sothis.thrift.protocol.TField;
import org.sothis.thrift.protocol.TProtocol;
import org.sothis.thrift.protocol.TStruct;
import org.sothis.thrift.protocol.TType;

public class QueryItem_args implements TEntity {
	private final static TStruct STRUCT_ARGS = new TStruct("QueryItem_args");
	private final static short FID_REQ = 1;
	private final static short FID_TABLE = 2;
	private final static TField FIELD_REQ = new TField("req", TType.STRUCT, FID_REQ);
	private final static TField FIELD_TABLE = new TField("table", TType.STRING, FID_TABLE);

	private final QueryItemREQ req;
	private final String table;

	public QueryItem_args(QueryItemREQ req, String table) {
		this.req = req;
		this.table = table;
	}

	@Override
	public void write(TProtocol protocol) throws TException {
		protocol.writeStructBegin(STRUCT_ARGS);
		protocol.writeFieldBegin(FIELD_REQ);
		req.write(protocol);
		protocol.writeFieldEnd();
		protocol.writeFieldBegin(FIELD_TABLE);
		protocol.writeString(table);
		protocol.writeFieldEnd();
		protocol.writeFieldStop();
		protocol.writeStructEnd();
	}

	@Override
	public void read(TProtocol protocol) throws TException {
		// TODO Auto-generated method stub

	}

}