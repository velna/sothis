package org.sothis.thrift.protocol;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.sothis.thrift.TException;

public class TBinaryProtocol extends TProtocol {

	public final static TProtocolFactory FACTORY = new TProtocolFactory() {
		@Override
		public TProtocol newProtocol(ByteBuffer buffer) {
			return new TBinaryProtocol(buffer);
		}
	};

	private static final int VERSION_MASK = 0xffff0000;
	private static final int VERSION_1 = 0x80010000;
	private static final TStruct ANONYMOUS_STRUCT = new TStruct();

	public TBinaryProtocol(ByteBuffer buffer) {
		super(buffer);
	}

	@Override
	public void writeMessageBegin(TMessage message) throws TException {
		int version = VERSION_1 | message.type;
		writeI32(version);
		writeString(message.name);
		writeI32(message.seqid);
	}

	@Override
	public void writeMessageEnd() throws TException {
	}

	@Override
	public void writeStructBegin(TStruct struct) throws TException {
	}

	@Override
	public void writeStructEnd() throws TException {
	}

	@Override
	public void writeFieldBegin(TField field) throws TException {
		writeByte(field.type);
		writeI16(field.id);
	}

	@Override
	public void writeFieldEnd() throws TException {
	}

	@Override
	public void writeFieldStop() throws TException {
		writeByte(TType.STOP);
	}

	@Override
	public void writeMapBegin(TMap map) throws TException {
		writeByte(map.keyType);
		writeByte(map.valueType);
		writeI32(map.size);
	}

	@Override
	public void writeMapEnd() throws TException {
	}

	@Override
	public void writeListBegin(TList list) throws TException {
		writeByte(list.elemType);
		writeI32(list.size);
	}

	@Override
	public void writeListEnd() throws TException {
	}

	@Override
	public void writeSetBegin(TSet set) throws TException {
		writeByte(set.elemType);
		writeI32(set.size);
	}

	@Override
	public void writeSetEnd() throws TException {
	}

	@Override
	public void writeBool(boolean b) throws TException {
		writeByte(b ? (byte) 1 : (byte) 0);
	}

	@Override
	public void writeByte(byte b) throws TException {
		buffer.put(b);
	}

	@Override
	public void writeI16(short i16) throws TException {
		buffer.putShort(i16);
	}

	@Override
	public void writeI32(int i32) throws TException {
		buffer.putInt(i32);
	}

	@Override
	public void writeI64(long i64) throws TException {
		buffer.putLong(i64);
	}

	@Override
	public void writeDouble(double dub) throws TException {
		buffer.putDouble(dub);
	}

	@Override
	public void writeString(String str) throws TException {
		try {
			byte[] bs = str.getBytes("UTF-8");
			writeBinary(bs, 0, bs.length);
		} catch (UnsupportedEncodingException e) {
			throw new TException(e);
		}
	}

	@Override
	public void writeBinary(byte[] bs, int off, int length) throws TException {
		writeI32(length);
		buffer.put(bs, off, length);
	}

	@Override
	public TMessage readMessageBegin() throws TException {
		int size = readI32();
		if (size < 0) {
			int version = size & VERSION_MASK;
			if (version != VERSION_1) {
				throw new TProtocolException(TProtocolException.BAD_VERSION, "Bad version in readMessageBegin");
			}
			return new TMessage(readString(), (byte) (size & 0x000000ff), readI32());
		} else {
			throw new TProtocolException(TProtocolException.BAD_VERSION, "Missing version in readMessageBegin, old client?");
		}
	}

	@Override
	public void readMessageEnd() throws TException {
	}

	@Override
	public TStruct readStructBegin() throws TException {
		return ANONYMOUS_STRUCT;
	}

	@Override
	public void readStructEnd() throws TException {
	}

	@Override
	public TField readFieldBegin() throws TException {
		byte type = readByte();
		short id = type == TType.STOP ? 0 : readI16();
		return new TField("", type, id);
	}

	@Override
	public void readFieldEnd() throws TException {
	}

	@Override
	public TMap readMapBegin() throws TException {
		return new TMap(readByte(), readByte(), readI32());
	}

	@Override
	public void readMapEnd() throws TException {
	}

	@Override
	public TList readListBegin() throws TException {
		return new TList(readByte(), readI32());
	}

	@Override
	public void readListEnd() throws TException {
	}

	@Override
	public TSet readSetBegin() throws TException {
		return new TSet(readByte(), readI32());
	}

	@Override
	public void readSetEnd() throws TException {
	}

	@Override
	public boolean readBool() throws TException {
		return (readByte() == 1);
	}

	@Override
	public byte readByte() throws TException {
		return buffer.get();
	}

	@Override
	public short readI16() throws TException {
		return buffer.getShort();
	}

	@Override
	public int readI32() throws TException {
		return buffer.getInt();
	}

	@Override
	public long readI64() throws TException {
		return buffer.getLong();
	}

	@Override
	public double readDouble() throws TException {
		return buffer.getDouble();
	}

	@Override
	public String readString() throws TException {
		try {
			return new String(readBinary(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new TException(e);
		}
	}

	@Override
	public byte[] readBinary() throws TException {
		int length = readI32();
		byte[] bs = new byte[length];
		buffer.get(bs);
		return bs;
	}

	@Override
	public void reset() {

	}

}
