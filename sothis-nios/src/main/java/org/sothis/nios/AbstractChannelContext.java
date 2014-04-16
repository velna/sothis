package org.sothis.nios;

import java.io.IOException;

import org.sothis.nios.Handlers.HandlerChain;

public abstract class AbstractChannelContext implements ChannelContext {

	private final Events events;
	private final Channel channel;
	private final HandlerChain<ExceptionHandler> exceptionHandlerChain;
	private final HandlerChain<ChannelClosedHandler> channelClosedHandlerChain;

	public AbstractChannelContext(Channel channel, Events events) {
		this.channel = channel;
		this.events = events;
		this.exceptionHandlerChain = channel.handlers().chain(ExceptionHandler.class);
		this.channelClosedHandlerChain = channel.handlers().chain(ChannelClosedHandler.class);
	}

	public AbstractChannelContext(AbstractChannelContext ctx) {
		this.channel = ctx.channel;
		this.events = ctx.events;
		this.exceptionHandlerChain = ctx.exceptionHandlerChain.fork();
		this.channelClosedHandlerChain = ctx.channelClosedHandlerChain.fork();
	}

	@Override
	public Events events() {
		return events;
	}

	@Override
	public void fireChannelClosed(ChannelContext ctx) {
		if (this.channelClosedHandlerChain.hasNext()) {
			this.channelClosedHandlerChain.next().channelClosed(ctx);
		}
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
	public void close() throws IOException {
		events.cancel(channel);
		channel.underlying().close();
		reset();
		this.fireChannelClosed(this);
	}

	@Override
	public void reset() {
		this.exceptionHandlerChain.reset();
		this.channelClosedHandlerChain.reset();
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
