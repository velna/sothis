package org.sothis.nios.thrift;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import org.sothis.nios.ClientSocketChannel;

public class TNiosTransport extends TNonblockingTransport {
	private final ClientSocketChannel channel;
	private final SocketAddress remote;

	public TNiosTransport(SocketAddress remote) throws IOException {
		super();
		this.remote = remote;
		this.channel = new ClientSocketChannel();
	}

	@Override
	public boolean startConnect() throws IOException {
		return channel.connect(remote);
	}

	@Override
	public boolean finishConnect() throws IOException {
		return channel.finishConnect();
	}

	@Override
	public SelectionKey registerSelector(Selector selector, int interests) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int read(ByteBuffer buffer) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int write(ByteBuffer buffer) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void open() throws TTransportException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public int read(byte[] buf, int off, int len) throws TTransportException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void write(byte[] buf, int off, int len) throws TTransportException {
		// TODO Auto-generated method stub

	}

}
