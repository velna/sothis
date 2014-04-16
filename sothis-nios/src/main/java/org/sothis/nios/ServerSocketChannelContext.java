package org.sothis.nios;

import org.sothis.nios.Handlers.HandlerChain;

public class ServerSocketChannelContext extends AbstractChannelContext {

	private final HandlerChain<MessageReceivedHandler> messageReceivedHandlerChain;

	private ServerSocketChannelContext(ServerSocketChannelContext ctx) {
		super(ctx);
		this.messageReceivedHandlerChain = ctx.messageReceivedHandlerChain.fork();
	}

	ServerSocketChannelContext(ServerSocketChannel channel, Events events) {
		super(channel, events);
		this.messageReceivedHandlerChain = channel.handlers().chain(MessageReceivedHandler.class);
	}

	@Override
	public ChannelContext fork() {
		return new ServerSocketChannelContext(this);
	}

	public void fireMessageReceived(ChannelContext ctx, Object message) {
		if (this.messageReceivedHandlerChain.hasNext()) {
			this.messageReceivedHandlerChain.next().messageReceived(ctx, message);
		}
	}

	public void reset() {
		super.reset();
		this.messageReceivedHandlerChain.reset();
	}

}
