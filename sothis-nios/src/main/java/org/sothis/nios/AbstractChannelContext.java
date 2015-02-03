package org.sothis.nios;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sothis.nios.Handlers.HandlerChain;

public abstract class AbstractChannelContext implements ChannelContext {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractChannelContext.class);

	private final Events events;
	private final Channel channel;
	private final SelectionKey key;
	private final HandlerChain<ExceptionHandler> exceptionHandlerChain;
	private final HandlerChain<ChannelOpenedHandler> channelOpenedHandlerChain;
	private final HandlerChain<ChannelClosedHandler> channelClosedHandlerChain;

	public AbstractChannelContext(Channel channel, SelectionKey key, Events events) {
		this.channel = channel;
		this.key = key;
		this.events = events;
		this.exceptionHandlerChain = channel.handlers().chain(ExceptionHandler.class);
		this.channelOpenedHandlerChain = channel.handlers().chain(ChannelOpenedHandler.class);
		this.channelClosedHandlerChain = channel.handlers().chain(ChannelClosedHandler.class);
	}

	public AbstractChannelContext(AbstractChannelContext ctx) {
		this.channel = ctx.channel;
		this.key = ctx.key;
		this.events = ctx.events;
		this.exceptionHandlerChain = ctx.exceptionHandlerChain.fork();
		this.channelOpenedHandlerChain = ctx.channelOpenedHandlerChain.fork();
		this.channelClosedHandlerChain = ctx.channelClosedHandlerChain.fork();
	}

	@Override
	public Events events() {
		return events;
	}

	@Override
	public void fireChannelOpened(ChannelContext ctx, boolean reset) {
		if (reset) {
			this.channelOpenedHandlerChain.reset();
		}
		if (this.channelOpenedHandlerChain.hasNext()) {
			this.channelOpenedHandlerChain.next().channelOpened(ctx);
		}
	}

	@Override
	public void fireChannelClosed(ChannelContext ctx, boolean reset) {
		if (reset) {
			this.channelClosedHandlerChain.reset();
		}
		if (this.channelClosedHandlerChain.hasNext()) {
			this.channelClosedHandlerChain.next().channelClosed(ctx);
		}
	}

	@Override
	public void fireMessageReceived(ChannelContext ctx, Object message, boolean reset) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void fireMessageSent(ChannelContext ctx, Object message, boolean reset) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void fireExceptionCaught(ChannelContext ctx, Throwable e, boolean reset) {
		if (reset) {
			this.exceptionHandlerChain.reset();
		}
		if (this.exceptionHandlerChain.hasNext()) {
			this.exceptionHandlerChain.next().exceptionCaught(ctx, e);
		}
	}

	@Override
	public Channel channel() {
		return channel;
	}

	@Override
	public void suspend(int op) {
		key.interestOps(key.interestOps() ^ op);
	}

	@Override
	public void resume(int op) {
		key.interestOps(key.interestOps() | op);
	}

	@Override
	public void close() {
		if (key.isValid()) {
			key.cancel();
		}
		if (channel.underlying().isOpen()) {
			try {
				channel.underlying().close();
			} catch (IOException e) {
				LOGGER.error("error close underlying socket: ", e);
			}
			this.fireChannelClosed(this, true);
		}
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
