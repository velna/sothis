package org.sothis.nios;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class SocketChannel extends AbstractChannel<java.nio.channels.SocketChannel> {

	private final ReadBuffer readBuffer;
	private final WriteBuffer writeBuffer;

	protected SocketChannel(java.nio.channels.SocketChannel channel) throws IOException {
		super(channel);
		this.readBuffer = new SocketChannelReadBuffer(4096, 3);
		this.writeBuffer = new SocketChannelWriteBuffer();
	}

	public ReadBuffer readBuffer() {
		return this.readBuffer;
	}

	public WriteBuffer writeBuffer() {
		return this.writeBuffer;
	}

	@Override
	public SocketAddress localAddress() throws IOException {
		return channel.getLocalAddress();
	}

	public SocketAddress remoteAddress() throws IOException {
		return channel.getRemoteAddress();
	}

	@Override
	public SocketChannel bind(SocketAddress local) throws IOException {
		channel.bind(local);
		return this;
	}

	@Override
	public <T> SocketChannel setOption(SocketOption<T> name, T value) throws IOException {
		channel.setOption(name, value);
		return this;
	}

	@Override
	public <T> T getOption(SocketOption<T> name) throws IOException {
		return channel.getOption(name);
	}

	private class SocketChannelWriteBuffer extends WriteBuffer {
		private ByteBuffer[] bufs = new ByteBuffer[0];
		private int bufsLength = 0;
		private int bufsOffset = 0;
		private final List<ByteBuffer> buffers = new LinkedList<ByteBuffer>();
		private int flushEmpty;

		boolean flush() {
			bufsLength = this.buffers.size();
			bufsOffset = 0;
			if (bufsLength > 0) {
				bufs = this.buffers.toArray(bufs);
				return true;
			} else {
				return false;
			}
		}

		int channelWrite() throws IOException {
			if (bufsLength == 0) {
				return ++flushEmpty;
			}
			flushEmpty = 0;
			long n = channel.write(bufs, bufsOffset, bufsLength);
			if (n > 0) {
				for (Iterator<ByteBuffer> i = buffers.iterator(); i.hasNext();) {
					ByteBuffer buf = i.next();
					if (buf.hasRemaining()) {
						break;
					} else {
						i.remove();
						bufsOffset++;
						bufsLength--;
					}
				}
			}
			return 0;
		}

		public void write(ByteBuffer buf) {
			buf.flip();
			this.buffers.add(buf);
		}

	}

	private class SocketChannelReadBuffer extends ReadBuffer {
		private final ByteBuffer[] ring;
		private final boolean[] ringFlags;
		private int currentIndex;
		private int readIndex;
		private ByteBuffer next;
		private final Iterator<Object> iterator = new Iterator<Object>() {

			@Override
			public boolean hasNext() {
				check();
				return next != null;
			}

			@Override
			public Object next() {
				if (null == next) {
					throw new NoSuchElementException();
				}
				return next;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};

		public SocketChannelReadBuffer(int capacity, int n) {
			this.ring = new ByteBuffer[n];
			this.ringFlags = new boolean[n];
			for (int i = 0; i < n; i++) {
				this.ring[i] = ByteBuffer.allocateDirect(capacity);
			}
			currentIndex = -1;
			readIndex = 0;
		}

		/**
		 * return <code>n</code> bytes read into this buffer.
		 * 
		 * @return return null indicates the end of stream and nothing read,
		 *         negative number indicates end of stream and abs(n) bytes
		 *         read, positive number indicates success read n bytes
		 * @throws IOException
		 */
		Long channelRead() throws IOException {
			long n, read = 0;
			ByteBuffer buf;
			do {
				if (this.readIndex == this.currentIndex) {
					// ring is full
					return 0L;
				}
				if (ringFlags[this.readIndex]) {
					// ring is full
					return 0L;
				}
				buf = this.ring[this.readIndex];
				buf.clear();
				n = channel.read(buf);
				if (n > 0) {
					buf.flip();
					ringFlags[this.readIndex] = true;
					this.readIndex++;
					if (this.readIndex == ring.length) {
						this.readIndex = 0;
					}
					read += n;
				}
			} while (n == buf.capacity());

			if (n < 0) {
				if (read == 0) {
					return null;
				} else {
					return -read;
				}
			} else {
				return read;
			}
		}

		@Override
		public Iterator<Object> iterator() {
			check();
			return iterator;
		}

		private void check() {
			if (next != null) {
				if (next.hasRemaining()) {
					return;
				} else {
					ringFlags[this.currentIndex] = false;
				}
			}

			int nextIndex = currentIndex + 1;
			if (nextIndex == ring.length) {
				nextIndex = 0;
			}
			if (ringFlags[nextIndex]) {
				next = ring[nextIndex];
				currentIndex = nextIndex;
			} else {
				next = null;
			}
		}
	}

}
