package org.sothis.nios;

import org.sothis.nios.Handlers.HandlerChain;

public abstract class AbstractChannelContext implements ChannelContext {

	private final Events events;
	private final Channel channel;
	private final HandlerChain<ExceptionHandler> exceptionHandlerChain;

	public AbstractChannelContext(Channel channel, Events events) {
		this.channel = channel;
		this.events = events;
		this.exceptionHandlerChain = channel.handlers().chain(ExceptionHandler.class);
	}

	public AbstractChannelContext(AbstractChannelContext ctx) {
		this.channel = ctx.channel;
		this.events = ctx.events;
		this.exceptionHandlerChain = ctx.exceptionHandlerChain.fork();
	}

	@Override
	public Events events() {
		return events;
	}

	@Override
	public void fireMessageReceived(ChannelContext ctx, Object message) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void fireMessageSent(ChannelContext ctx, Object message) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void fireExceptionCaught(ChannelContext ctx, Throwable e) {
		if (this.exceptionHandlerChain.hasNext()) {
			this.exceptionHandlerChain.next().exceptionCaught(ctx, e);
		}
	}

	@Override
	public Channel channel() {
		return channel;
	}

	@Override
	public void reset() {
		this.exceptionHandlerChain.reset();
	}

	@Override
	public void write(Object message) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void flush() {
		throw new UnsupportedOperationException();
	}

}
