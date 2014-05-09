package com.zamplus.thrift.hugecabinet;

import org.sothis.thrift.AsyncHandler;
import org.sothis.thrift.Sync;
import org.sothis.thrift.protocol.TCall;

public interface CabinetManager {

	public abstract Sync QueryItem(QueryItemREQ req, String table, AsyncHandler<TCall<QueryItem_args, QueryItem_result>> handler);

}