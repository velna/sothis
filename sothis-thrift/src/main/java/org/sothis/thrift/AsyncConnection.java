package org.sothis.thrift;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import org.sothis.thrift.ThriftClient.Worker;
import org.sothis.thrift.protocol.TAsyncCall;
import org.sothis.thrift.protocol.TFramedProtocol;
import org.sothis.thrift.protocol.TMessage;
import org.sothis.thrift.protocol.TProtocolFactory;

public class AsyncConnection extends Connection {

	private final Map<Integer, TAsyncCall<?, ?>> requests = new HashMap<>();
	private final TFramedProtocol readProtocol;
	private final TFramedProtocol writeProtocol;
	private int emptyPoll;

	public AsyncConnection(SocketChannel channel, TProtocolFactory factory) {
		super(channel);
		this.readProtocol = new TFramedProtocol(factory);
		this.writeProtocol = new TFramedProtocol(factory);
	}

	@Override
	public void transition(Worker worker, SelectionKey key) {
		if (!key.isValid()) {
			key.cancel();
			return;
		}
		try {
			if (key.isWritable()) {
				write(worker, key);
			}
			if (key.isReadable()) {
				read(worker, key);
			}
		} catch (Exception e) {
			key.cancel();
			if (e instanceof TException) {
				operationFailed((TException) e);
			} else {
				operationFailed(new TException(e));
			}
		}
	}

	private void write(Worker worker, SelectionKey key) throws IOException, TException {
		while (this.writeProtocol.write(channel) == -1) {
			TAsyncCall<?, ?> call = worker.getClient().pollCall();
			if (null == call) {
				emptyPoll++;
				if (emptyPoll >= 3) {
					worker.deactive(key);
				}
				return;
			}
			emptyPoll = 0;
			this.writeProtocol.reset();
			writeProtocol.writeMessageBegin(call.getMessage());
			call.getRequest().write(writeProtocol);
			writeProtocol.writeMessageEnd();
		}
	}

	private void read(Worker worker, SelectionKey key) throws IOException, TException {
		if (this.readProtocol.read(channel) == -1) {
			key.cancel();
			channel.close();
		}
		TMessage message;
		while ((message = this.readProtocol.readMessageBegin()) != null) {
			TAsyncCall<?, ?> call = this.requests.get(message.seqid);
			call.getResponse().read(readProtocol);
			this.readProtocol.readMessageEnd();
			call.signal(null);
		}
	}

	private void operationFailed(TException e) {
		for (Map.Entry<Integer, TAsyncCall<?, ?>> entry : requests.entrySet()) {
			entry.getValue().signal(e);
		}
	}
}
