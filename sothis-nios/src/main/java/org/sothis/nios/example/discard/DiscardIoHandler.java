package org.sothis.nios.example.discard;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sothis.nios.ChannelClosedHandler;
import org.sothis.nios.ChannelContext;
import org.sothis.nios.MessageReceivedHandler;
import org.sothis.nios.ReadBuffer;

public class DiscardIoHandler implements MessageReceivedHandler, ChannelClosedHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(DiscardIoHandler.class);

	private final AtomicLong totalRead;

	public DiscardIoHandler(AtomicLong totalRead) {
		super();
		this.totalRead = totalRead;
	}

	@Override
	public void messageReceived(ChannelContext ctx, Object message) {
		ReadBuffer readBuffer = (ReadBuffer) message;
		int n = 0;
		for (Object obj : readBuffer) {
			ByteBuffer buf = (ByteBuffer) obj;
			n += buf.remaining();
			while (buf.hasRemaining()) {
				buf.get();
			}
		}
		this.totalRead.addAndGet(n);
		LOGGER.info("read {}/{} bytes", n, this.totalRead.get());
	}

	@Override
	public void channelClosed(ChannelContext ctx) {
		LOGGER.info("channel closed");
	}
}
