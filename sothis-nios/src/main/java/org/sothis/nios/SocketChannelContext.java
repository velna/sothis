package org.sothis.nios;

import org.sothis.nios.Handlers.HandlerChain;

public class SocketChannelContext extends AbstractChannelContext {
	private final HandlerChain<MessageReceivedHandler> messageReceivedHandlerChain;
	private final HandlerChain<MessageSentHandler> messageSentHandlerChain;

	SocketChannelContext(SocketChannel channel, Events events) {
		super(channel, events);
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

	public void fireMessageReceived(ChannelContext ctx, Object message) {
		if (this.messageReceivedHandlerChain.hasNext()) {
			this.messageReceivedHandlerChain.next().messageReceived(ctx, message);
		}
	}

	public void fireMessageSent(ChannelContext ctx, Object message) {
		if (this.messageSentHandlerChain.hasNext()) {
			this.messageSentHandlerChain.next().messageSent(ctx, message);
		}
	}

	public void reset() {
		super.reset();
		this.messageReceivedHandlerChain.reset();
		this.messageSentHandlerChain.reset();
	}

	@Override
	public void write(Object message) {
		reset();
		this.fireMessageSent(this, message);
	}

	@Override
	public void flush() {
		if (this.channel().writeBuffer().flush()) {
			this.events().resume(this.channel(), Events.OP_WRITE);
		}
	}

}
