package org.sothis.nios;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class WriteBuffer {
	abstract boolean flush();

	abstract int channelWrite() throws IOException;

	abstract public void write(ByteBuffer buf);
}
