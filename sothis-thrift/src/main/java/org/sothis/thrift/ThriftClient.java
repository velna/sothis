package org.sothis.thrift;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThriftClient {
	private final static Logger LOGGER = LoggerFactory.getLogger(ThriftClient.class);

	private final ConcurrentLinkedQueue<TAsyncRequest<? extends TResponse>> penddingRequests = new ConcurrentLinkedQueue<TAsyncRequest<? extends TResponse>>();
	private final SelectWorker[] selectThreads;
	private int callIdx = 0;
	private int regIdx = 0;

	public ThriftClient(int nThreads, ThreadFactory threadFactory) throws IOException {
		super();
		this.selectThreads = new SelectWorker[nThreads];
		for (int i = 0; i < nThreads; i++) {
			this.selectThreads[i] = new SelectWorker();
			threadFactory.newThread(this.selectThreads[i]).start();
		}
	}

	public void call(TAsyncRequest<? extends TResponse> request) {
		this.penddingRequests.add(request);
		for (int i = 0; i < this.selectThreads.length; i++) {
			if (this.selectThreads[i].tryWakeup()) {
				break;
			}
			// int next = Math.abs(callIdx % this.selectThreads.length);
			// this.selectThreads[next].selector.wakeup();
			// callIdx++;
		}
	}

	protected void initChannel(SocketChannel channel) {

	}

	public void connect(SocketAddress remote) throws IOException {
		SocketChannel channel = SocketChannel.open();
		channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
		initChannel(channel);
		// channel.setOption(StandardSocketOptions.SO_RCVBUF, 65535);
		// channel.setOption(StandardSocketOptions.SO_SNDBUF, 512);
		channel.connect(remote);
		LOGGER.info("connection established: {}{}", channel.getLocalAddress(), channel.getRemoteAddress());
		channel.configureBlocking(false);

		int i = Math.abs(regIdx % this.selectThreads.length);
		this.selectThreads[i].addPenddingConnection(new Connection(channel));
		regIdx++;
	}

	TAsyncRequest<? extends TResponse> pollRequest() {
		return this.penddingRequests.poll();
	}

	protected interface Worker {
		ThriftClient getClient();

		void deactive(SelectionKey key);
	}

	private class SelectWorker implements Runnable, Worker {
		private final Selector selector;
		private final LinkedList<SelectionKey> idleKeys = new LinkedList<SelectionKey>();
		private final LinkedList<Connection> penddingConnections = new LinkedList<Connection>();
		private int keyCount;

		public SelectWorker() throws IOException {
			this.selector = Selector.open();
		}

		@Override
		public void run() {
			while (true) {
				try {
					int n = this.selector.select();
					if (n > 0) {
						processEvents();
					}
					registerPenddingConnections();
					if (this.idleKeys.size() > 0) {
						wakeupConnections();
					}
				} catch (IOException e) {
					LOGGER.error("", e);
				}
			}
		}

		public void addPenddingConnection(Connection c) {
			synchronized (this.penddingConnections) {
				this.penddingConnections.add(c);
			}
			this.selector.wakeup();
		}

		private void wakeupConnections() {
			if (!penddingRequests.isEmpty()) {
				SelectionKey key = this.idleKeys.pollFirst();
				if (null != key) {
					key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
				}
			}
		}

		public boolean tryWakeup() {
			if (this.idleKeys.size() > 0) {
				this.selector.wakeup();
				return true;
			}
			return false;
		}

		public void registerPenddingConnections() {
			synchronized (this.penddingConnections) {
				Connection connection = null;
				while ((connection = penddingConnections.poll()) != null) {
					try {
						connection.getChannel().register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, connection);
						keyCount++;
					} catch (ClosedChannelException e) {
						LOGGER.error("", e);
					}
				}
			}
		}

		private void processEvents() {
			Iterator<SelectionKey> i = this.selector.selectedKeys().iterator();
			while (i.hasNext()) {
				SelectionKey key = i.next();
				i.remove();
				if (!key.isValid()) {
					continue;
				}
				Connection connection = (Connection) key.attachment();
				connection.transition(this, key);
			}
		}

		@Override
		public ThriftClient getClient() {
			return ThriftClient.this;
		}

		@Override
		public void deactive(SelectionKey key) {
			key.interestOps(0);
			idleKeys.add(key);
		}
	}
}
