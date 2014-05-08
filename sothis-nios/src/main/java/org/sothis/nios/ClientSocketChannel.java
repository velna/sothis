package org.sothis.nios;

import java.io.IOException;
import java.net.SocketAddress;

public class ClientSocketChannel extends SocketChannel {

	public ClientSocketChannel() throws IOException {
		super(java.nio.channels.SocketChannel.open());
	}

	public boolean connect(SocketAddress remote) throws IOException {
		return this.underlying().connect(remote);
	}

	public boolean finishConnect() throws IOException {
		return this.underlying().finishConnect();
	}
}
