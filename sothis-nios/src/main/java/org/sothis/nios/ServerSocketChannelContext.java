package org.sothis.nios;

import java.nio.channels.SelectionKey;

import org.sothis.nios.Handlers.HandlerChain;

public class ServerSocketChannelContext extends AbstractChannelContext {

	private final HandlerChain<MessageReceivedHandler> messageReceivedHandlerChain;

	private ServerSocketChannelContext(ServerSocketChannelContext ctx) {
		super(ctx);
		this.messageReceivedHandlerChain = ctx.messageReceivedHandlerChain.fork();
	}

	ServerSocketChannelContext(ServerSocketChannel channel, SelectionKey key, Events events) {
		super(channel, key, events);
		this.messageReceivedHandlerChain = channel.handlers().chain(MessageReceivedHandler.class);
	}

	@Override
	public ChannelContext fork() {
		return new ServerSocketChannelContext(this);
	}

	public void fireMessageReceived(ChannelContext ctx, Object message, boolean reset) {
		if (reset) {
			this.messageReceivedHandlerChain.reset();
		}
		if (this.messageReceivedHandlerChain.hasNext()) {
			this.messageReceivedHandlerChain.next().messageReceived(ctx, message);
		}
	}

	@Override
	public ServerSocketChannel channel() {
		return (ServerSocketChannel) super.channel();
	}

}
