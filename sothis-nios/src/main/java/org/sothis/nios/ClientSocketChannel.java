package org.sothis.nios;

import java.io.IOException;
import java.net.SocketAddress;

public class ClientSocketChannel extends SocketChannel {

	public ClientSocketChannel() throws IOException {
		super(java.nio.channels.SocketChannel.open());
	}

	public void connect(SocketAddress remote) throws IOException {
		this.underlying().connect(remote);
	}
}
