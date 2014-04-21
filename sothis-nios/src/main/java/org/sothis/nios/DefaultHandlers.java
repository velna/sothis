package org.sothis.nios;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class DefaultHandlers implements Handlers {

	private final List<Handler> handlers = new ArrayList<Handler>();

	@Override
	public void addFirst(Handler handler) {
		this.handlers.add(0, handler);
	}

	@Override
	public void addLast(Handler handler) {
		handlers.add(handler);
	}

	@Override
	public void remove(Handler handler) {
		handlers.remove(handler);
	}

	@Override
	public Handler replace(Handler original, Handler newone) {
		int i = handlers.indexOf(original);
		if (i < 0) {
			return null;
		} else {
			return handlers.set(i, newone);
		}
	}

	private class TypedHandlerChain<H extends Handler> implements HandlerChain<H> {
		private int i;
		private Handler next = null;
		private final Class<H> c;
		private final int start;

		public TypedHandlerChain(Class<H> c, int start) {
			super();
			this.c = c;
			this.start = start;
			reset();
		}

		private Handler findNext() {
			while (i < handlers.size()) {
				Handler h = handlers.get(i);
				if (c.isAssignableFrom(h.getClass())) {
					return h;
				}
				i++;
			}
			return null;
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public H next() {
			if (null == next) {
				throw new NoSuchElementException();
			}
			H ret = (H) next;
			next = findNext();
			return ret;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void reset() {
			i = start;
			next = findNext();
		}

		@Override
		public HandlerChain<H> fork() {
			return new TypedHandlerChain<H>(c, i);
		}

	}

	@Override
	public <H extends Handler> HandlerChain<H> chain(final Class<H> c) {
		return new TypedHandlerChain<H>(c, 0);
	}

}
