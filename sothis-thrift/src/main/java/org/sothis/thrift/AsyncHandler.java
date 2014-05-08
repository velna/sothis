package org.sothis.thrift;

public interface AsyncHandler<R> {
	void operationCompleted(R result);

	void operationFailed(Exception e);
}
