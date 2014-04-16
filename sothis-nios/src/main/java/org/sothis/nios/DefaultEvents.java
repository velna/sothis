package org.sothis.nios;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultEvents implements Events {

	private final static Logger LOGGER = LoggerFactory.getLogger(DefaultEvents.class);
	private final static int MAX_EMPTY_FLUSH = 3;

	private final Selector selector;
	private final TreeMap<Long, Set<Runnable>> tasks = new TreeMap<Long, Set<Runnable>>();
	private final Map<Runnable, Set<Runnable>> taskMap = new ConcurrentHashMap<Runnable, Set<Runnable>>();
	private final AtomicBoolean running = new AtomicBoolean(false);

	public DefaultEvents() throws IOException {
		selector = Selector.open();
	}

	public boolean isRunning() {
		return this.running.get();
	}

	public void shutdown() throws IOException {
		if (this.running.compareAndSet(true, false)) {
			selector.close();
		}
	}

	@Override
	public void run() {
		if (!running.compareAndSet(false, true)) {
			throw new IllegalStateException("worker is already runinng.");
		}
		long lastProcessTime = System.currentTimeMillis();
		while (this.running.get()) {
			try {
				long t = 500;
				int n;
				Map.Entry<Long, Set<Runnable>> task = tasks.firstEntry();
				if (null != task) {
					t = task.getKey() - lastProcessTime;
					if (t <= 0) {
						n = selector.selectNow();
					} else {
						n = selector.select(t);
					}
				} else {
					n = selector.select(t);
				}
				LOGGER.debug("wait {} ms, found {} events", t, n);
				if (n > 0) {
					runEvents();
				}
				runTasks();
				lastProcessTime = System.currentTimeMillis();
			} catch (Exception e) {
				LOGGER.error("", e);
			}
		}
	}

	@Override
	public ChannelContext register(Channel channel, int ops) throws IOException {
		ChannelContext context;
		SelectionKey key = channel.underlying().keyFor(selector);
		if (null == key) {
			if (channel instanceof SocketChannel) {
				context = new SocketChannelContext((SocketChannel) channel, this);
			} else if (channel instanceof ServerSocketChannel) {
				context = new ServerSocketChannelContext((ServerSocketChannel) channel, this);
			} else {
				throw new IllegalArgumentException("unknown channel type: " + channel.getClass());
			}
			channel.underlying().register(selector, ops, context);
		} else {
			context = (ChannelContext) key.attachment();
		}
		return context;
	}

	@Override
	public void suspend(Channel channel, int op) {
		SelectionKey key = channel.underlying().keyFor(selector);
		if (null != key && key.isValid()) {
			key.interestOps(key.interestOps() ^ op);
		}
	}

	@Override
	public void resume(Channel channel, int op) {
		SelectionKey key = channel.underlying().keyFor(selector);
		if (null != key && key.isValid()) {
			key.interestOps(key.interestOps() | op);
		}
	}

	@Override
	public void cancel(Channel channel) {
		SelectionKey key = channel.underlying().keyFor(selector);
		if (null != key && key.isValid()) {
			key.cancel();
		}
	}

	@Override
	public ChannelContext context(Channel channel) {
		SelectionKey key = channel.underlying().keyFor(selector);
		if (null != key) {
			return (ChannelContext) key.attachment();
		} else {
			return null;
		}
	}

	@Override
	public void submit(long timeout, Runnable r) {
		if (timeout < 0) {
			throw new IllegalArgumentException("timeout can not be negative.");
		}
		long expire = timeout == 0 ? 0 : System.currentTimeMillis() + timeout;
		Set<Runnable> rs = this.tasks.get(expire);
		if (null == rs) {
			rs = new HashSet<Runnable>(2);
			this.tasks.put(expire, rs);
			this.taskMap.put(r, rs);
		}
		rs.add(r);
	}

	private void runTasks() {
		long now = System.currentTimeMillis();
		while (!tasks.isEmpty() && tasks.firstKey() <= now) {
			Map.Entry<Long, Set<Runnable>> task = tasks.pollFirstEntry();
			Set<Runnable> rs = task.getValue();
			for (Runnable r : rs) {
				r.run();
			}
		}
	}

	private void runEvents() {
		Iterator<SelectionKey> i = selector.selectedKeys().iterator();
		while (i.hasNext()) {
			SelectionKey key = i.next();
			LOGGER.debug("readyOps: {}", key.readyOps());
			if (key.isValid()) {
				ChannelContext ctx = (ChannelContext) key.attachment();
				if (key.isAcceptable() || key.isReadable()) {
					try {
						Long n = ctx.channel().readBuffer().channelRead();
						if (n == null || n < 0) {
							// TODO: end of stream
						} else if (n > 0) {
							ctx.reset();
							ctx.fireMessageReceived(ctx, ctx.channel().readBuffer());
						}
					} catch (IOException e) {
						ctx.reset();
						ctx.fireExceptionCaught(ctx, e);
					}
				}
				if (key.isWritable()) {
					try {
						if (ctx.channel().writeBuffer().channelWrite() >= MAX_EMPTY_FLUSH) {
							this.suspend(ctx.channel(), OP_WRITE);
						}
					} catch (IOException e) {
						ctx.reset();
						ctx.fireExceptionCaught(ctx, e);
					}
				}
				if (key.isConnectable()) {
					// TODO:
				}
			}
			i.remove();
		}
	}
}
