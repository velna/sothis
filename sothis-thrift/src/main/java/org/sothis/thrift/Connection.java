package org.sothis.thrift;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sothis.thrift.protocol.TFramedProtocol;

public abstract class Connection {
	private final static Logger LOGGER = LoggerFactory.getLogger(Connection.class);

	protected final SocketAddress remoteAddress;
	protected final SocketChannel channel;
	protected final TFramedProtocol.Factory factory;

	public Connection(SocketAddress remoteAddress, TFramedProtocol.Factory factory) throws IOException {
		this.remoteAddress = remoteAddress;
		this.factory = factory;
		this.channel = SocketChannel.open();
		channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
		channel.connect(remoteAddress);
		channel.configureBlocking(false);
		LOGGER.info("connection established: {}{}", channel.getLocalAddress(), channel.getRemoteAddress());
	}

	public abstract void transition(ThriftClient.Worker worker, SelectionKey key);

	public abstract Connection duplicate(SocketAddress remoteAddress) throws IOException;

	public TFramedProtocol.Factory getProtocolFactory() {
		return factory;
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public SocketAddress getRemoteAddress() {
		return remoteAddress;
	}
}