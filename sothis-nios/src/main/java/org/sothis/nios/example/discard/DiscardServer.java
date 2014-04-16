package org.sothis.nios.example.discard;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sothis.nios.ChannelInitializer;
import org.sothis.nios.DefaultEvents;
import org.sothis.nios.Events;
import org.sothis.nios.EventsGroup;
import org.sothis.nios.ServerSocketChannel;
import org.sothis.nios.SocketChannel;

public class DiscardServer {

	private final static Logger LOGGER = LoggerFactory.getLogger(DiscardServer.class);

	private final static AtomicLong TOTAL_READ = new AtomicLong(0);
	private final Events acceptor;
	private final Events worker;

	public DiscardServer() throws Exception {
		acceptor = new EventsGroup(DefaultEvents.class, 1, new BasicThreadFactory.Builder().daemon(false)
				.namingPattern("acceptor-%d").build());
		worker = new EventsGroup(DefaultEvents.class, 1, new BasicThreadFactory.Builder().daemon(false)
				.namingPattern("worker-%d").build());

		ServerSocketChannel serverChannel = new ServerSocketChannel(worker, new ChannelInitializer<SocketChannel>() {
			@Override
			public void initialize(SocketChannel channel) throws IOException {
				channel.underlying().setOption(StandardSocketOptions.TCP_NODELAY, true);
				channel.underlying().setOption(StandardSocketOptions.SO_RCVBUF, 8192);
				channel.underlying().setOption(StandardSocketOptions.SO_SNDBUF, 8192);
				channel.underlying().setOption(StandardSocketOptions.SO_KEEPALIVE, true);
				channel.handlers().addLast(new DiscardIoHandler(TOTAL_READ));
			}
		});
		serverChannel.underlying().setOption(StandardSocketOptions.SO_REUSEADDR, true);
		serverChannel.underlying().setOption(StandardSocketOptions.SO_RCVBUF, 8192);
		serverChannel.bind(new InetSocketAddress(12345));
		acceptor.register(serverChannel, Events.OP_ACCEPT);

		worker.run();
		acceptor.run();
		LOGGER.info("bind at 12345");
	}

	public static void main(String[] args) throws Exception {
		new DiscardServer();
	}

}
