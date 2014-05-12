package com.zamplus.thrift.hugecabinet;

import java.io.IOException;
import java.util.concurrent.ThreadFactory;

import org.sothis.thrift.AsyncHandler;
import org.sothis.thrift.Sync;
import org.sothis.thrift.ThriftClient;
import org.sothis.thrift.protocol.TAsyncCall;
import org.sothis.thrift.protocol.TCall;
import org.sothis.thrift.protocol.TMessage;

public class CabinetManager {

	public interface Service {
		public abstract Sync QueryItem(QueryItemREQ req, String table,
				AsyncHandler<TCall<QueryItem_args, QueryItem_result>> handler);
	}

	public static class Client extends ThriftClient implements Service {

		public Client(int nThreads, ThreadFactory threadFactory) throws IOException {
			super(nThreads, threadFactory);
		}

		@Override
		public Sync QueryItem(QueryItemREQ req, String table, AsyncHandler<TCall<QueryItem_args, QueryItem_result>> handler) {
			TAsyncCall<QueryItem_args, QueryItem_result> call = new TAsyncCall<>(TMessage.newRequestMessage("QueryItem"), handler);
			call.setRequest(new QueryItem_args(req, table));
			call.setResponse(new QueryItem_result());
			this.call(call);
			return call;
		}

	}
}