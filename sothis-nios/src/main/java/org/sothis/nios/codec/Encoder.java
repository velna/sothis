package org.sothis.nios.codec;

import org.sothis.nios.ChannelContext;
import org.sothis.nios.MessageSentHandler;
import org.sothis.nios.WriteBuffer;

public abstract class Encoder implements MessageSentHandler {

	protected abstract void encode(ChannelContext ctx, Object message, WriteBuffer out);

	@Override
	public void messageSent(ChannelContext ctx, Object message) {
		encode(ctx, message, ctx.channel().writeBuffer());
		ctx.fireMessageSent(ctx, message);
	}

}
