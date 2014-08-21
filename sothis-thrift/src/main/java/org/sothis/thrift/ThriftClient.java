package org.sothis.thrift;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.CancelledKeyException;
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
import org.sothis.thrift.protocol.TFramedProtocol;
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

	public void connect(SocketAddress remote, boolean async, int maxMessageSize, TProtocolFactory factory) throws IOException {
		TFramedProtocol.Factory framedFactory = new TFramedProtocol.Factory(maxMessageSize, factory);
		Connection connection = async ? new AsyncConnection(remote, framedFactory) : new SyncConnection(remote, framedFactory);
		initChannel(connection.getChannel());
		int i = Math.abs(regIdx % this.selectThreads.length);
		this.selectThreads[i].addPenddingConnection(connection);
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
		private final LinkedList<SelectionKey> exceptionKeys = new LinkedList<SelectionKey>();
		private final LinkedList<Connection> penddingConnections = new LinkedList<Connection>();
		private long selectWaits = 0;
		private long exceptionCheckPoint = 0;

		public SelectWorker() throws IOException {
			this.selector = Selector.open();
		}

		@Override
		public void run() {
			while (true) {
				try {
					int n = this.selector.select(selectWaits);
					if (n > 0) {
						processEvents();
					}
					registerPenddingConnections();
					if (this.idleKeys.size() > 0) {
						wakeupConnections();
					}
					if (this.exceptionKeys.size() > 0 && System.currentTimeMillis() >= exceptionCheckPoint) {
						checkExceptionKeys();
					}
				} catch (IOException e) {
					LOGGER.error("", e);
				}
			}
		}

		private void addExceptionKey(SelectionKey key) {
			selectWaits = 1000;
			exceptionCheckPoint = System.currentTimeMillis() + selectWaits;
			this.exceptionKeys.add(key);
		}

		private void checkExceptionKeys() {
			for (Iterator<SelectionKey> i = this.exceptionKeys.iterator(); i.hasNext();) {
				SelectionKey key = i.next();
				if (key.isValid()) {
					i.remove();
					continue;
				}
				SocketChannel channel = (SocketChannel) key.channel();
				try {
					channel.close();
				} catch (IOException e) {
				}
				Connection oldConnection = (Connection) key.attachment();
				try {
					LOGGER.info("try reconnect to {}", oldConnection.getRemoteAddress());
					addPenddingConnection(oldConnection.duplicate(oldConnection.getRemoteAddress()));
					i.remove();
				} catch (IOException e) {
					selectWaits <<= 1;
					selectWaits = Math.max(selectWaits, 60000);
					exceptionCheckPoint = System.currentTimeMillis() + selectWaits;
					LOGGER.error("error connect to " + oldConnection.getRemoteAddress(), e);
				}
			}
			if (this.exceptionKeys.size() == 0) {
				selectWaits = 0;
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
					try {
						key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
					} catch (CancelledKeyException e) {
						addExceptionKey(key);
					}
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
					addExceptionKey(key);
					continue;
				}
				Connection connection = (Connection) key.attachment();
				connection.transition(this, key);
				if (!key.isValid()) {
					addExceptionKey(key);
				}
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
