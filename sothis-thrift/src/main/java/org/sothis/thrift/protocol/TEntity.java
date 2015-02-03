package org.sothis.thrift.protocol;

import org.sothis.thrift.TException;

public interface TEntity {
	public void read(TProtocol protocol) throws TException;

	public void write(TProtocol protocol) throws TException;
}
