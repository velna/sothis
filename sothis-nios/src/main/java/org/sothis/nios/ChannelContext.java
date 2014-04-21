package org.sothis.nios;

public interface ChannelContext {
	Channel channel();

	Events events();

	ChannelContext fork();

	void write(Object message);

	void flush();

	void suspend(int op);

	void resume(int op);

	void close();

	void fireChannelOpened(ChannelContext ctx, boolean reset);

	void fireChannelClosed(ChannelContext ctx, boolean reset);

	void fireMessageReceived(ChannelContext ctx, Object message, boolean reset);

	void fireMessageSent(ChannelContext ctx, Object message, boolean reset);

	void fireExceptionCaught(ChannelContext ctx, Throwable e, boolean reset);
}
