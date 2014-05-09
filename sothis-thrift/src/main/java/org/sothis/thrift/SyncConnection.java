package org.sothis.thrift;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.sothis.thrift.protocol.TAsyncCall;
import org.sothis.thrift.protocol.TFramedProtocol;
import org.sothis.thrift.protocol.TMessage;
import org.sothis.thrift.protocol.TProtocolFactory;

public class SyncConnection extends Connection {
	private final TFramedProtocol protocol;

	private TAsyncCall<?, ?> currentCall;
	private int emptyPoll;
	private TransitionState state = TransitionState.WRITE;

	private static enum TransitionState {
		WRITE, READ
	}

	public SyncConnection(SocketChannel channel, TProtocolFactory factory) {
		super(channel);
		this.protocol = new TFramedProtocol(factory);
	}

	@Override
	public void transition(ThriftClient.Worker worker, SelectionKey key) {
		if (!key.isValid()) {
			key.cancel();
			return;
		}

		try {
			switch (state) {
			case WRITE:
				if (key.isWritable()) {
					write(worker, key);
				}
				break;
			case READ:
				if (key.isReadable()) {
					read(worker, key);
				}
				break;
			default:
				throw new IllegalStateException("unknown transition stat: " + state);
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

	private void write(ThriftClient.Worker worker, SelectionKey key) throws TException, IOException {
		if (null == this.currentCall) {
			if ((this.currentCall = worker.getClient().pollCall()) == null) {
				emptyPoll++;
				if (emptyPoll >= 3) {
					worker.deactive(key);
				}
				return;
			}
			emptyPoll = 0;
			protocol.reset();
			protocol.writeMessageBegin(this.currentCall.getMessage());
			this.currentCall.getRequest().write(protocol);
			protocol.writeMessageEnd();
		}
		if (protocol.write(channel) == -1) {
			this.state = TransitionState.READ;
			this.protocol.reset();
		}
	}

	private void read(ThriftClient.Worker worker, SelectionKey key) throws TException, IOException {
		if (protocol.read(channel) == -1) {
			key.cancel();
			channel.close();
			return;
		}
		TMessage message = this.protocol.readMessageBegin();
		if (null != message) {
			this.currentCall.getResponse().read(protocol);
			this.protocol.readMessageEnd();
			this.currentCall.signal(null);
			this.state = TransitionState.WRITE;
			currentCall = null;
		}
	}

	private void operationFailed(TException e) {
		if (null != this.currentCall) {
			this.currentCall.signal(e);
		}
	}

}
