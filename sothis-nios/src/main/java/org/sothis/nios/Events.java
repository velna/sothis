package org.sothis.nios;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface Events extends Runnable {

	public final static int OP_ACCEPT = SelectionKey.OP_ACCEPT;
	public final static int OP_CONNECT = SelectionKey.OP_CONNECT;
	public final static int OP_READ = SelectionKey.OP_READ;
	public final static int OP_WRITE = SelectionKey.OP_WRITE;

	void register(Channel channel, int ops) throws IOException;

	void submit(long timeout, Runnable r);

	boolean isRunning();

	void shutdown() throws IOException;
}