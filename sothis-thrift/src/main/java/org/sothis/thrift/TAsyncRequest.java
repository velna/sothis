package org.sothis.thrift;

public abstract class TAsyncRequest<R extends TResponse> extends TRequest implements Sync {

	private final AsyncHandler<R> asyncHandler;
	private boolean done;

	public TAsyncRequest(AsyncHandler<R> asyncHandler) {
		super();
		this.asyncHandler = asyncHandler;
	}

	public AsyncHandler<R> getAsyncHandler() {
		return asyncHandler;
	}

	@Override
	public synchronized void sync() throws InterruptedException {
		while (!done) {
			wait();
		}
	}

	synchronized void signal() {
		done = true;
		this.notifyAll();
	}
}
