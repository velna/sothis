package org.sothis.thrift;

import org.sothis.thrift.protocol.TCall;
import org.sothis.thrift.protocol.TEntity;

public interface AsyncHandler<C extends TCall<? extends TEntity, ? extends TEntity>> {
	void operationCompleted(C call);

	void operationFailed(C call, TException e);
}
