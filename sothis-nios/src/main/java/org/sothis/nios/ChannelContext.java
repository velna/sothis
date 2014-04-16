package org.sothis.nios;

import java.io.IOException;

public interface ChannelContext {
	Channel channel();

	Events events();

	ChannelContext fork();

	void write(Object message);

	void flush();

	void close() throws IOException;

	void reset();

	void fireChannelClosed(ChannelContext ctx);

	void fireMessageReceived(ChannelContext ctx, Object message);

	void fireMessageSent(ChannelContext ctx, Object message);

	void fireExceptionCaught(ChannelContext ctx, Throwable e);
}
