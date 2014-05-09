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
import org.sothis.thrift.protocol.TAsyncCall;
import org.sothis.thrift.protocol.TEntity;
import org.sothis.thrift.protocol.TProtocolFactory;

public class ThriftClient {
	private final static Logger LOGGER = LoggerFactory.getLogger(ThriftClient.class);

	private final ConcurrentLinkedQueue<TAsyncCall<? extends TEntity, ? extends TEntity>> penddingCalls = new ConcurrentLinkedQueue<>();
	private final SelectWorker[] selectThreads;
	private int regIdx = 0;

	public ThriftClient(int nThreads, ThreadFactory threadFactory) throws IOException {
		super();
		this.selectThreads = new SelectWorker[nThreads];
		for (int i = 0; i < nThreads; i++) {
			this.selectThreads[i] = new SelectWorker();
			threadFactory.newThread(this.selectThreads[i]).start();
		}
	}

	public void call(TAsyncCall<? extends TEntity, ? extends TEntity> call) {
		this.penddingCalls.add(call);
		for (int i = 0; i < this.selectThreads.length; i++) {
			if (this.selectThreads[i].tryWakeup()) {
				break;
			}
		}
	}

	protected void initChannel(SocketChannel channel) {

	}

	public void connect(SocketAddress remote, boolean async, TProtocolFactory factory) throws IOException {
		SocketChannel channel = SocketChannel.open();
		channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
		initChannel(channel);
		channel.connect(remote);
		LOGGER.info("connection established: {}{}", channel.getLocalAddress(), channel.getRemoteAddress());
		channel.configureBlocking(false);

		int i = Math.abs(regIdx % this.selectThreads.length);
		this.selectThreads[i].addPenddingConnection(async ? new AsyncConnection(channel, factory) : new SyncConnection(channel,
				factory));
		regIdx++;
	}

	TAsyncCall<? extends TEntity, ? extends TEntity> pollCall() {
		return this.penddingCalls.poll();
	}

	protected interface Worker {
		ThriftClient getClient();

		void deactive(SelectionKey key);
	}

	private class SelectWorker implements Runnable, Worker {
		private final Selector selector;
		private final LinkedList<SelectionKey> idleKeys = new LinkedList<SelectionKey>();
		private final LinkedList<Connection> penddingConnections = new LinkedList<Connection>();

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
			if (!penddingCalls.isEmpty()) {
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
			key.interestOps(SelectionKey.OP_READ);
			idleKeys.add(key);
		}
	}
}
