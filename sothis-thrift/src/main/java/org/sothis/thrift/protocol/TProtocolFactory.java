package org.sothis.thrift.protocol;

import java.nio.ByteBuffer;

public interface TProtocolFactory {
	public TProtocol newProtocol(ByteBuffer buffer);
}
