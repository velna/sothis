package org.sothis.nios;

public interface ChannelContext {
	Channel channel();

	Events events();

	ChannelContext fork();

	void write(Object message);

	void flush();

	void reset();

	void fireMessageReceived(ChannelContext ctx, Object message);

	void fireMessageSent(ChannelContext ctx, Object message);

	void fireExceptionCaught(ChannelContext ctx, Throwable e);
}
