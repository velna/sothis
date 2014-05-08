package org.sothis.nios.thrift;

import org.sothis.nios.ChannelContext;
import org.sothis.nios.WriteBuffer;
import org.sothis.nios.buffer.BufferPool;
import org.sothis.nios.codec.Encoder;

public class ThriftEncoder extends Encoder {

	@Override
	protected void encode(ChannelContext ctx, Object message, WriteBuffer out) {
		BufferPool.get().allocDirect(4096);
		
	}

}
