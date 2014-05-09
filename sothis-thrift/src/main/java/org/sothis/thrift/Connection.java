package org.sothis.thrift;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public abstract class Connection {

	protected final SocketChannel channel;

	public Connection(SocketChannel channel) {
		this.channel = channel;
	}

	public abstract void transition(ThriftClient.Worker worker, SelectionKey key);

	public SocketChannel getChannel() {
		return channel;
	}

}