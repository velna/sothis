package org.sothis.nios;

import java.nio.channels.SelectionKey;

import org.sothis.nios.Handlers.HandlerChain;

public class SocketChannelContext extends AbstractChannelContext {
	private final HandlerChain<MessageReceivedHandler> messageReceivedHandlerChain;
	private final HandlerChain<MessageSentHandler> messageSentHandlerChain;

	SocketChannelContext(SocketChannel channel, SelectionKey key, Events events) {
		super(channel, key, events);
		this.messageReceivedHandlerChain = channel.handlers().chain(MessageReceivedHandler.class);
		this.messageSentHandlerChain = channel.handlers().chain(MessageSentHandler.class);
	}

	private SocketChannelContext(SocketChannelContext ctx) {
		super(ctx);
		this.messageReceivedHandlerChain = ctx.messageReceivedHandlerChain.fork();
		this.messageSentHandlerChain = ctx.messageSentHandlerChain.fork();
	}

	public SocketChannelContext fork() {
		return new SocketChannelContext(this);
	}

	public void fireMessageReceived(ChannelContext ctx, Object message, boolean reset) {
		if (reset) {
			this.messageReceivedHandlerChain.reset();
		}
		if (this.messageReceivedHandlerChain.hasNext()) {
			this.messageReceivedHandlerChain.next().messageReceived(ctx, message);
		}
	}

	public void fireMessageSent(ChannelContext ctx, Object message, boolean reset) {
		if (reset) {
			this.messageSentHandlerChain.reset();
		}
		if (this.messageSentHandlerChain.hasNext()) {
			this.messageSentHandlerChain.next().messageSent(ctx, message);
		}
	}

	@Override
	public void write(Object message) {
		this.fireMessageSent(this, message, true);
	}

	@Override
	public void flush() {
		if (this.channel().writeBuffer().flush()) {
			resume(Events.OP_WRITE);
		}
	}

	@Override
	public SocketChannel channel() {
		return (SocketChannel) super.channel();
	}

}
