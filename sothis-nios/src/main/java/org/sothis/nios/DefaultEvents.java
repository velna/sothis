package org.sothis.nios;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultEvents implements Events {

	private final static Logger LOGGER = LoggerFactory.getLogger(DefaultEvents.class);
	private final static int MAX_EMPTY_FLUSH = 3;

	private final Selector selector;
	private final TreeMap<Long, Set<Runnable>> tasks = new TreeMap<Long, Set<Runnable>>();
	private final Map<Runnable, Set<Runnable>> taskMap = new ConcurrentHashMap<Runnable, Set<Runnable>>();
	private final ConcurrentLinkedQueue<Object[]> pendingChannels = new ConcurrentLinkedQueue<Object[]>();
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
				long t = 0;
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
				registerPenndingChannels();
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
	public void register(Channel channel, int ops) throws IOException {
		pendingChannels.add(new Object[] { channel, ops });
		selector.wakeup();
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

	private void registerPenndingChannels() {
		Object[] pair;
		while ((pair = this.pendingChannels.poll()) != null) {
			Channel channel = (Channel) pair[0];
			int ops = (Integer) pair[1];
			ChannelContext context;
			try {
				SelectionKey key = channel.underlying().register(selector, ops);
				if (channel instanceof SocketChannel) {
					context = new SocketChannelContext((SocketChannel) channel, key, this);
				} else if (channel instanceof ServerSocketChannel) {
					context = new ServerSocketChannelContext((ServerSocketChannel) channel, key, this);
				} else {
					throw new IllegalArgumentException("unknown channel type: " + channel.getClass());
				}
				key.attach(context);
			} catch (ClosedChannelException e) {
				LOGGER.error("error register channel: ", e);
			}
		}
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
			i.remove();
			LOGGER.debug("readyOps: {}", key.readyOps());
			if (key.isValid()) {
				ChannelContext ctx = (ChannelContext) key.attachment();
				if (key.isAcceptable() || key.isReadable()) {
					try {
						Long n = ctx.channel().readBuffer().channelRead();
						if (n == null || n < 0) {
							ctx.close();
							continue;
						} else if (n > 0) {
							ctx.fireMessageReceived(ctx, ctx.channel().readBuffer(), true);
						}
					} catch (IOException e) {
						ctx.fireExceptionCaught(ctx, e, true);
					}
				}
				if (key.isWritable()) {
					try {
						if (ctx.channel().writeBuffer().channelWrite() >= MAX_EMPTY_FLUSH) {
							ctx.suspend(OP_WRITE);
						}
					} catch (IOException e) {
						ctx.fireExceptionCaught(ctx, e, true);
					}
				}
				if (key.isConnectable()) {
					SocketChannelContext context = (SocketChannelContext) ctx;
					try {
						if (context.channel().underlying().finishConnect()) {
							ctx.fireChannelOpened(ctx, true);
						} else {
							ctx.close();
						}
					} catch (IOException e) {
						ctx.close();
					}
				}
			}
		}
	}
}
