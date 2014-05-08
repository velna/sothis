package org.sothis.nios.codec;

import java.io.IOException;

import org.sothis.nios.ChannelContext;
import org.sothis.nios.MessageSentHandler;
import org.sothis.nios.WriteBuffer;

public abstract class Encoder implements MessageSentHandler {

	protected abstract void encode(ChannelContext ctx, Object message, WriteBuffer out) throws IOException;

	@Override
	public void messageSent(ChannelContext ctx, Object message) {
		try {
			encode(ctx, message, ctx.channel().writeBuffer());
			ctx.fireMessageSent(ctx, message, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
