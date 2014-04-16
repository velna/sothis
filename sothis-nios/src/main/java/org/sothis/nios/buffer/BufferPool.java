package org.sothis.nios.buffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BufferPool {

	private final static ThreadLocal<BufferPool> POOL = new ThreadLocal<BufferPool>() {

		@Override
		protected BufferPool initialValue() {
			return new BufferPool();
		}

	};

	private final static int MAX_BUF_SIZE = 16;
	private final static int MIN_BUF_SIZE = 8;

	private List<LinkedList<ByteBuffer>> buffers = new ArrayList<LinkedList<ByteBuffer>>(MAX_BUF_SIZE - MIN_BUF_SIZE);
	private List<LinkedList<ByteBuffer>> directBuffers = new ArrayList<LinkedList<ByteBuffer>>(MAX_BUF_SIZE - MIN_BUF_SIZE);

	private BufferPool() {
		for (int i = 0; i < MAX_BUF_SIZE - MIN_BUF_SIZE; i++) {
			buffers.add(new LinkedList<ByteBuffer>());
			directBuffers.add(new LinkedList<ByteBuffer>());
		}
	}

	public static BufferPool get() {
		BufferPool pool = POOL.get();
		return pool;
	}

	private ByteBuffer alloc(int size, boolean direct) {
		int i = 0;
		while ((1 << (i++)) < size)
			;
		if (i > MAX_BUF_SIZE) {
			throw new IllegalArgumentException("size out of range");
		}
		if (i < MIN_BUF_SIZE) {
			i = MIN_BUF_SIZE;
		}
		LinkedList<ByteBuffer> pool = direct ? directBuffers.get(i - MIN_BUF_SIZE) : buffers.get(i - MIN_BUF_SIZE);
		if (pool.isEmpty()) {
			return direct ? ByteBuffer.allocateDirect(1 << i) : ByteBuffer.allocate(1 << i);
		} else {
			return pool.getFirst();
		}
	}

	public ByteBuffer alloc(int size) {
		return alloc(size, false);
	}

	public ByteBuffer allocDirect(int size) {
		return alloc(size, true);
	}

	public void free(ByteBuffer buf) {
		int i = 1;
		int size = buf.capacity();
		while ((size >>= 1) > 0) {
			i++;
		}
		if (i > MAX_BUF_SIZE) {
			throw new IllegalArgumentException("size out of range");
		}
		if (i < MIN_BUF_SIZE) {
			i = MIN_BUF_SIZE;
		}
		LinkedList<ByteBuffer> pool = buf.isDirect() ? directBuffers.get(i - MIN_BUF_SIZE) : buffers.get(i - MIN_BUF_SIZE);
		buf.clear();
		pool.addLast(buf);
	}
}
