package org.sothis.nios.codec.thrift;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.sothis.nios.WriteBuffer;

public abstract class TRequest {

	private final static AtomicInteger TID = new AtomicInteger(0);

	private final int tid = TID.incrementAndGet();

	abstract public void encode(WriteBuffer out) throws IOException;

	public int getTid() {
		return tid;
	}
}
