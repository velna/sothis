package org.sothis.nios.codec.thrift;

import java.io.IOException;

import org.sothis.nios.ChannelContext;
import org.sothis.nios.WriteBuffer;
import org.sothis.nios.codec.Encoder;

public class ThriftBinaryEncoder extends Encoder {
	@Override
	protected void encode(ChannelContext ctx, Object message, WriteBuffer out) throws IOException {
		TRequest req = (TRequest) message;
		req.encode(out);
	}

}
