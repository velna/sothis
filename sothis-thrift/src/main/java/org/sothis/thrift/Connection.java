package org.sothis.thrift;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connection {
	private final static Logger LOGGER = LoggerFactory.getLogger(Connection.class);
	// private final static RequestStat requestStat =
	// RequestStat.newStat("connection");
	private final static int MAX_MESSAGE_SIZE = 65535;
	protected static final int VERSION_MASK = 0xffff0000;

	private final SocketChannel channel;
	private final ByteBuffer[] buffer = new ByteBuffer[] { ByteBuffer.allocateDirect(4),
			ByteBuffer.allocateDirect(MAX_MESSAGE_SIZE) };
	private TAsyncRequest currentRequest;
	// private long requestStart;
	private int currentMessageSize;
	private int emptyPoll;
	private TransitionState state = TransitionState.WRITE;

	private static enum TransitionState {
		WRITE, READ_SIZE, READ_BODY
	}

	public Connection(SocketChannel channel) {
		super();
		this.channel = channel;
	}

	public void transition(ThriftClient.Worker worker, SelectionKey key) {
		if (!key.isValid()) {
			key.cancel();
			return;
		}

		// long start = System.currentTimeMillis();
		// TransitionState s = state;
		try {
			switch (state) {
			case WRITE:
				if (key.isWritable()) {
					write(worker, key);
				}
				break;
			case READ_SIZE:
				if (key.isReadable()) {
					readSize();
				}
				break;
			case READ_BODY:
				if (key.isReadable()) {
					readBody();
				}
				break;
			default:
				throw new IllegalStateException("unknown transition stat: " + state);
			}
		} catch (Exception e) {
			key.cancel();
			operationFailed(e);
		}
		// LOGGER.info("transition from {} to {}, cost {} ms", s, state,
		// System.currentTimeMillis() - start);
	}

	private void write(ThriftClient.Worker worker, SelectionKey key) throws IOException {
		if (null == this.currentRequest) {
			if ((this.currentRequest = worker.getClient().pollRequest()) == null) {
				emptyPoll++;
				if (emptyPoll >= 3) {
					worker.deactive(key);
				}
				return;
			}
			emptyPoll = 0;
			// this.requestStart = System.currentTimeMillis();
			this.buffer[0].clear();
			this.buffer[1].clear();
			this.currentRequest.encode(this.buffer[1]);
			this.buffer[1].flip();
			this.buffer[0].putInt(this.buffer[1].limit());
			this.buffer[0].flip();
		}
		channel.write(this.buffer);
		if (this.buffer[1].remaining() == 0) {
			this.state = TransitionState.READ_SIZE;
			this.buffer[0].clear();
			this.buffer[1].clear();
		}
	}

	private void readSize() throws IOException {
		channel.read(buffer);
		if (this.buffer[0].remaining() == 0) {
			this.buffer[0].flip();
			currentMessageSize = this.buffer[0].getInt();
			if (currentMessageSize <= 0 || currentMessageSize > MAX_MESSAGE_SIZE) {
				throw new TProtocolException("invalid message size: " + currentMessageSize);
			}
			this.state = TransitionState.READ_BODY;
			checkBody();
		}
	}

	private void checkBody() throws IOException {
		if (this.buffer[1].position() == currentMessageSize) {
			this.buffer[1].flip();
			this.state = TransitionState.WRITE;
			handlerResponse(decode(this.buffer[1]));
			this.currentRequest = null;
		} else if (this.buffer[1].position() > currentMessageSize) {
			throw new TProtocolException("expect " + currentMessageSize + " bytes, but read " + this.buffer[1].position()
					+ " bytes");
		}
	}

	private void readBody() throws IOException {
		channel.read(this.buffer[1]);
		checkBody();
	}

	private void handlerResponse(TResponse response) {
		this.currentRequest.signal();
		// requestStat.stat(System.currentTimeMillis() - this.requestStart);
		if (response instanceof TError) {
			this.currentRequest.getAsyncHandler().operationFailed(new TException((TError) response));
		} else {
			this.currentRequest.getAsyncHandler().operationCompleted(response);
		}
	}

	private TResponse decode(ByteBuffer buf) throws IOException {
		TResponse response;
		int i = buf.getInt();
		byte type;
		if (i < 0) {
			int version = i & VERSION_MASK;
			type = (byte) (i & 0xff);
			buf.position(buf.getInt() + buf.position());
		} else {
			buf.position(buf.position() + i);
			type = buf.get();
		}

		int tid = buf.getInt();
		if (type == TMessageType.EXCEPTION) {
			response = new TError(tid);
		} else {
			response = this.currentRequest.newResponse(tid);
		}
		response.decode(buf);
		return response;
	}

	private void operationFailed(Exception e) {
		if (null != this.currentRequest) {
			this.currentRequest.getAsyncHandler().operationFailed(e);
		}
	}

	public SocketChannel getChannel() {
		return channel;
	}
}
