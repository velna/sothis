package org.sothis.thrift;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class TRequest {

	protected final static int VERSION_1 = 0x80010000;
	private final static AtomicInteger TID = new AtomicInteger(0);

	private final int tid = TID.incrementAndGet();

	public TRequest() {
		super();
	}

//	public ByteBuffer encode() {
//		ByteBuffer buf = BufferPool.get().allocDirect(1024);
//		buf.putInt(0);
//		try {
//			encode(buf);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		buf.putInt(0, buf.position() - 4);
//		buf.flip();
//		return buf;
//	}

	abstract public TResponse newResponse(int tid);

	abstract public void encode(ByteBuffer buffer) throws IOException;

	public int getTid() {
		return tid;
	}

}
