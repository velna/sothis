package org.sothis.nios;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ServerSocketChannel extends AbstractChannel<java.nio.channels.ServerSocketChannel> {

	private final Events workerEvents;
	private final ChannelInitializer<SocketChannel> channelInitializer;
	private final ReadBuffer readBuffer;

	public ServerSocketChannel(Events workerEvents, ChannelInitializer<SocketChannel> channelInitializer) throws IOException {
		super(java.nio.channels.ServerSocketChannel.open());
		this.workerEvents = workerEvents;
		this.channelInitializer = channelInitializer;
		this.readBuffer = new ServerSocketChannelReadBuffer();
	}

	public void bind(SocketAddress local) throws IOException {
		this.channel.bind(local);
	}

	@Override
	public ReadBuffer readBuffer() {
		return readBuffer;
	}

	@Override
	public WriteBuffer writeBuffer() {
		throw new UnsupportedOperationException("write buffer is not supported for server sockets.");
	}

	private class ServerSocketChannelReadBuffer extends ReadBuffer {
		private final List<Object> socketChannels = new LinkedList<Object>();

		@Override
		Long channelRead() throws IOException {
			java.nio.channels.SocketChannel ch = channel.accept();
			if (null != ch) {
				SocketChannel ret = new SocketChannel(ch);
				channelInitializer.initialize(ret);
				workerEvents.register(ret, Events.OP_READ | Events.OP_WRITE);
				socketChannels.add(ret);
				return 1L;
			} else {
				return 0L;
			}
		}

		@Override
		public Iterator<Object> iterator() {
			return socketChannels.iterator();
		}

	}
}
