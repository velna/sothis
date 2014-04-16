package org.sothis.nios;

import java.io.IOException;
import java.nio.channels.SelectableChannel;

public abstract class AbstractChannel<C extends SelectableChannel> implements Channel {

	protected final C channel;
	private final Handlers handlers = new DefaultHandlers();

	public AbstractChannel(C channel) throws IOException {
		this.channel = channel;
		this.channel.configureBlocking(false);
	}

	public C underlying() {
		return this.channel;
	}

	@Override
	public Handlers handlers() {
		return handlers;
	}

}
