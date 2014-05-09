package com.zamplus.thrift.hugecabinet;

import java.io.IOException;
import java.util.concurrent.ThreadFactory;

import org.sothis.thrift.AsyncHandler;
import org.sothis.thrift.Sync;
import org.sothis.thrift.ThriftClient;
import org.sothis.thrift.protocol.TAsyncCall;
import org.sothis.thrift.protocol.TCall;
import org.sothis.thrift.protocol.TMessage;

public class CabinetManagerImpl extends ThriftClient implements CabinetManager {

	public CabinetManagerImpl(int nThreads, ThreadFactory threadFactory) throws IOException {
		super(nThreads, threadFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sothi.thrift.zamplus.ZamplusClient#QueryItem(org.sothi.thrift.zamplus
	 * .QueryItemREQ, java.lang.String, org.sothi.thrift.AsyncHandler)
	 */
	@Override
	public Sync QueryItem(final QueryItemREQ req, final String table,
			final AsyncHandler<TCall<QueryItem_args, QueryItem_result>> handler) {
		TAsyncCall<QueryItem_args, QueryItem_result> call = new TAsyncCall<>(TMessage.newRequestMessage("QueryItem"), handler);
		call.setRequest(new QueryItem_args(req, table));
		call.setResponse(new QueryItem_result());
		this.call(call);
		return call;
	}

}
