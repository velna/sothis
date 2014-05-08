package org.sothis.thrift;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface TMessage {
	void encode(ByteBuffer buf) throws IOException;
}
