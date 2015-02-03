package org.sothis.thrift.protocol;

import org.sothis.thrift.AsyncHandler;
import org.sothis.thrift.Sync;
import org.sothis.thrift.TException;

public class TAsyncCall<REQ extends TEntity, RESP extends TEntity> extends TCall<REQ, RESP> implements Sync {

	private final AsyncHandler<TCall<REQ, RESP>> asyncHandler;
	private boolean done;

	public TAsyncCall(TMessage message, AsyncHandler<TCall<REQ, RESP>> asyncHandler) {
		super(message);
		this.asyncHandler = asyncHandler;
	}

	public AsyncHandler<TCall<REQ, RESP>> getAsyncHandler() {
		return asyncHandler;
	}

	@Override
	public synchronized void sync() throws InterruptedException {
		while (!done) {
			wait();
		}
	}

	public synchronized void signal(TException e) {
		done = true;
		this.notifyAll();
		if (null != e) {
			asyncHandler.operationFailed(this, e);
		} else {
			asyncHandler.operationCompleted(this);
		}
	}
}
