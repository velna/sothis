package org.sothis.nios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventsGroup implements Events {

	private final ThreadFactory threadFactory;
	private final Map<Channel, Events> channelEventsNexus = new HashMap<Channel, Events>();
	private final Map<Thread, Events> threadEventsNexus = new HashMap<Thread, Events>();
	private int nextIdx = 0;
	private final AtomicBoolean running = new AtomicBoolean(false);
	private final List<Events> eventsList;

	public EventsGroup(Class<? extends Events> eventsClass, int numThreads, ThreadFactory threadFactory)
			throws ReflectiveOperationException {
		this.threadFactory = threadFactory;
		this.eventsList = new ArrayList<Events>(numThreads);
		for (int i = 0; i < numThreads; i++) {
			Events worker = eventsClass.newInstance();
			this.eventsList.add(worker);
		}
	}

	@Override
	public void shutdown() throws IOException {
		if (running.compareAndSet(true, false)) {
			for (int i = 0; i < this.eventsList.size(); i++) {
				this.eventsList.get(i).shutdown();
			}
			for (Thread thread : this.threadEventsNexus.keySet()) {
				try {
					thread.join();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	@Override
	public void run() {
		if (!running.compareAndSet(false, true)) {
			throw new IllegalStateException("worker group is already runinng.");
		}
		this.threadEventsNexus.clear();
		for (Events events : this.eventsList) {
			Thread thread = this.threadFactory.newThread(events);
			this.threadEventsNexus.put(thread, events);
			thread.start();
		}
	}

	@Override
	public boolean isRunning() {
		return running.get();
	}

	private synchronized Events findEvents(Channel channel) {
		Events events = this.channelEventsNexus.get(channel);
		if (null == events) {
			events = this.eventsList.get(this.nextIdx++ % this.eventsList.size());
			this.channelEventsNexus.put(channel, events);
		}
		return events;
	}

	@Override
	public ChannelContext register(Channel channel, int ops) throws IOException {
		return findEvents(channel).register(channel, ops);
	}

	@Override
	public void suspend(Channel channel, int op) {
		findEvents(channel).suspend(channel, op);
	}

	@Override
	public void resume(Channel channel, int op) {
		findEvents(channel).resume(channel, op);
	}

	@Override
	public void cancel(Channel channel) {
		findEvents(channel).cancel(channel);
	}

	@Override
	public ChannelContext context(Channel channel) {
		return findEvents(channel).context(channel);
	}

	@Override
	public void submit(long timeout, Runnable r) {
		Events events = this.threadEventsNexus.get(Thread.currentThread());
		if (null == events) {
			throw new IllegalStateException("can not find events for current thread.");
		}
		events.submit(timeout, r);
	}

}
