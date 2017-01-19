package org.sothis.core.util.bloomfilter;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;

import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;

public final class BloomFilter<T> {
	private static final int MAGIC_CODE = 0x01464C42;
	private static final int HEADER_LENGTH = Integer.SIZE / 8 * 3;

	private final int numHashFunctions;
	private final BitArray bits;
	private final Funnel<T> funnel;

	private BloomFilter(BitArray bits, Funnel<T> funnel, int numHashFunctions) throws IOException {
		this.numHashFunctions = numHashFunctions;
		this.bits = bits;
		this.funnel = funnel;
	}

	public boolean put(T object) {
		long bitSize = bits.bitSize();
		byte[] bytes = Hashing.murmur3_128().hashObject(object, funnel).asBytes();
		long hash1 = lowerEight(bytes);
		long hash2 = upperEight(bytes);

		boolean bitsChanged = false;
		long combinedHash = hash1;
		for (int i = 0; i < numHashFunctions; i++) {
			// Make the combined hash positive and indexable
			bitsChanged |= bits.set((combinedHash & Long.MAX_VALUE) % bitSize);
			combinedHash += hash2;
		}
		return bitsChanged;
	}

	public boolean mightContain(T object) {
		long bitSize = bits.bitSize();
		byte[] bytes = Hashing.murmur3_128().hashObject(object, funnel).asBytes();
		long hash1 = lowerEight(bytes);
		long hash2 = upperEight(bytes);

		long combinedHash = hash1;
		for (int i = 0; i < numHashFunctions; i++) {
			// Make the combined hash positive and indexable
			if (!bits.get((combinedHash & Long.MAX_VALUE) % bitSize)) {
				return false;
			}
			combinedHash += hash2;
		}
		return true;
	}

	public long bitCount() {
		return bits.bitCount();
	}

	private long lowerEight(byte[] bytes) {
		return Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
	}

	private long upperEight(byte[] bytes) {
		return Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
	}

	public void writeTo(OutputStream out) throws IOException {
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(out));
		dout.writeInt(MAGIC_CODE);
		dout.writeInt(numHashFunctions);
		dout.writeInt((int) (bits.bitSize() / 8));
		bits.writeTo(dout);
		dout.flush();
	}

	public static <T> BloomFilter<T> readFrom(InputStream in, Funnel<T> funnel) throws IOException {
		DataInputStream din = new DataInputStream(in);
		if (din.readInt() != MAGIC_CODE) {
			return null;
		}
		int numHashFunctions = din.readInt();
		int numBytes = din.readInt();
		byte[] data = new byte[numBytes];
		din.read(data);
		return new BloomFilter<T>(new ByteBufferBitArray(ByteBuffer.wrap(data)), funnel, numHashFunctions);
	}

	public static <T> BloomFilter<T> create(Funnel<T> funnel, int expectedInsertions, double fpp) throws IOException {
		long numBits = optimalNumOfBits(expectedInsertions, fpp);
		long numBytes = numBits >> 3;
		if (numBytes > Integer.MAX_VALUE) {
			return null;
		}
		int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
		return new BloomFilter<T>(new ByteBufferBitArray(ByteBuffer.allocate((int) numBytes)), funnel, numHashFunctions);
	}

	public static <T> BloomFilter<T> createMapped(Funnel<T> funnel, int expectedInsertions, double fpp, String filename)
			throws IOException {
		long numBits = optimalNumOfBits(expectedInsertions, fpp);
		long numBytes = numBits >> 3;
		if (numBytes > Integer.MAX_VALUE) {
			return null;
		}
		if (null == filename) {
			return null;
		}
		int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
		ByteBuffer data;

		File file = new File(filename);
		RandomAccessFile raf = null;
		try {
			if (file.isFile()) {
				raf = new RandomAccessFile(file, "rw");
				if (raf.readInt() != MAGIC_CODE) {
					return null;
				}
				if (raf.readInt() != numHashFunctions) {
					return null;
				}
				if (raf.readInt() != numBytes) {
					return null;
				}
				if (file.length() != (numBytes) + HEADER_LENGTH) {
					return null;
				}
				data = raf.getChannel().map(MapMode.READ_WRITE, HEADER_LENGTH, numBytes);
			} else {
				raf = new RandomAccessFile(file, "rw");
				raf.setLength(HEADER_LENGTH + numBytes);
				raf.writeInt(MAGIC_CODE);
				raf.writeInt(numHashFunctions);
				raf.writeInt((int) numBytes);
				FileChannel channel = raf.getChannel();
				data = channel.map(MapMode.READ_WRITE, HEADER_LENGTH, numBytes);
				channel.close();
			}
		} finally {
			raf.close();
		}
		return new BloomFilter<T>(new ByteBufferBitArray(data), funnel, numHashFunctions);
	}

	static int optimalNumOfHashFunctions(long n, long m) {
		// (m / n) * log(2), but avoid truncation due to division!
		return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
	}

	static long optimalNumOfBits(long n, double p) {
		if (p == 0) {
			p = Double.MIN_VALUE;
		}
		return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
	}

	static interface BitArray {

		boolean set(long index);

		boolean get(long index);

		long bitSize();

		long bitCount();

		void writeTo(DataOutputStream out) throws IOException;
	}

	static final class ByteBufferBitArray implements BitArray {
		long bitCount;
		ByteBuffer data;

		// Used by serialization
		ByteBufferBitArray(ByteBuffer data) {
			this.data = data;
			long bitCount = 0;
			for (int i = 0; i < data.limit(); i++) {
				bitCount += Integer.bitCount(data.get(i));
			}
			this.bitCount = bitCount;
		}

		/** Returns true if the bit changed value. */
		public boolean set(long index) {
			if (!get(index)) {
				int i = (int) (index >> 3);
				data.put(i, (byte) (data.get(i) | (1 << (index & 0x7))));
				bitCount++;
				return true;
			}
			return false;
		}

		public boolean get(long index) {
			return (data.get((int) (index >> 3)) & (1 << (index & 0x7))) != 0;
		}

		/** Number of bits */
		public long bitSize() {
			return ((long) data.limit()) << 3;
		}

		/** Number of set bits (1s) */
		public long bitCount() {
			return bitCount;
		}

		@Override
		protected void finalize() throws Throwable {
			if (this.data instanceof MappedByteBuffer) {
				((MappedByteBuffer) this.data).force();
			}
			super.finalize();
		}

		@Override
		public void writeTo(DataOutputStream out) throws IOException {
			for (int i = 0; i < data.limit(); i++) {
				out.write(data.get(i));
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BloomFilter<CharSequence> bf = BloomFilter.createMapped(Funnels.stringFunnel(Charset.forName("UTF-8")), 10000000, 0.0001,
				"/Users/velna/test1.bf");
		System.out.println(bf.bitCount());
		bf.put("abc");
		System.out.println(bf.mightContain("abc"));
		System.out.println(bf.mightContain("def"));
		OutputStream out = new FileOutputStream("/Users/velna/test2.bf");
		bf.writeTo(out);
		out.close();
		bf = BloomFilter.readFrom(new FileInputStream("/Users/velna/test2.bf"), Funnels.stringFunnel(Charset.forName("UTF-8")));
		System.out.println(bf.bitCount());
		System.out.println(bf.mightContain("abc"));
		System.out.println(bf.mightContain("def"));
	}
}
