package org.sothis.nios;

public interface MessageSentHandler extends Handler {
	void messageSent(ChannelContext ctx, Object message);
}
