package org.sothis.nios;

import java.util.Iterator;

public interface Handlers {

	void addFirst(Handler handler);

	void addLast(Handler handler);

	void remove(Handler handler);

	Handler replace(Handler original, Handler newone);

	<H extends Handler> HandlerChain<H> chain(Class<H> c);

	public interface HandlerChain<H extends Handler> extends Iterator<H> {
		void reset();

		HandlerChain<H> fork();
	}
}
