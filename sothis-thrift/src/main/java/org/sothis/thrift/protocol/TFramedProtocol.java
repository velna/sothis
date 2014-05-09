package org.sothis.thrift.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.sothis.thrift.TException;

public class TFramedProtocol extends TProtocol {
	public final static int MAX_MESSAGE_SIZE = 65535;

	private final TProtocol protocol;
	private int frameIndex;

	public TFramedProtocol(TProtocolFactory factory) {
		super(ByteBuffer.allocateDirect(MAX_MESSAGE_SIZE + 4));
		this.protocol = factory.newProtocol(buffer);
	}

	public int read(SocketChannel channel) throws IOException {
		int n = channel.read(buffer);
		buffer.flip();
		return n;
	}

	public int write(SocketChannel channel) throws IOException {
		buffer.flip();
		if (buffer.remaining() == 0) {
			return -1;
		}
		int n = channel.write(buffer);
		buffer.compact();
		return buffer.position() == 0 ? -1 : n;
	}

	@Override
	public void reset() {
		protocol.reset();
		this.buffer.clear();
		frameIndex = 0;
	}

	@Override
	public void writeMessageBegin(TMessage message) throws TException {
		frameIndex = buffer.position();
		buffer.putInt(0);
		protocol.writeMessageBegin(message);
	}

	@Override
	public void writeMessageEnd() throws TException {
		protocol.writeMessageEnd();
		buffer.putInt(frameIndex, buffer.position() - frameIndex - 4);
	}

	@Override
	public void writeStructBegin(TStruct struct) throws TException {
		protocol.writeStructBegin(struct);
	}

	@Override
	public void writeStructEnd() throws TException {
		protocol.writeStructEnd();
	}

	@Override
	public void writeFieldBegin(TField field) throws TException {
		protocol.writeFieldBegin(field);
	}

	@Override
	public void writeFieldEnd() throws TException {
		protocol.writeFieldEnd();
	}

	@Override
	public void writeFieldStop() throws TException {
		protocol.writeFieldStop();
	}

	@Override
	public void writeMapBegin(TMap map) throws TException {
		protocol.writeMapBegin(map);
	}

	@Override
	public void writeMapEnd() throws TException {
		protocol.writeMapEnd();
	}

	@Override
	public void writeListBegin(TList list) throws TException {
		protocol.writeListBegin(list);
	}

	@Override
	public void writeListEnd() throws TException {
		protocol.writeListEnd();
	}

	@Override
	public void writeSetBegin(TSet set) throws TException {
		protocol.writeSetBegin(set);
	}

	@Override
	public void writeSetEnd() throws TException {
		protocol.writeSetEnd();
	}

	@Override
	public void writeBool(boolean b) throws TException {
		protocol.writeBool(b);
	}

	@Override
	public void writeByte(byte b) throws TException {
		protocol.writeByte(b);
	}

	@Override
	public void writeI16(short i16) throws TException {
		protocol.writeI16(i16);
	}

	@Override
	public void writeI32(int i32) throws TException {
		protocol.writeI32(i32);
	}

	@Override
	public void writeI64(long i64) throws TException {
		protocol.writeI64(i64);
	}

	@Override
	public void writeDouble(double dub) throws TException {
		protocol.writeDouble(dub);
	}

	@Override
	public void writeString(String str) throws TException {
		protocol.writeString(str);
	}

	@Override
	public void writeBinary(byte[] bs, int off, int length) throws TException {
		protocol.writeBinary(bs, off, length);
	}

	@Override
	public TMessage readMessageBegin() throws TException {
		if (buffer.remaining() >= 4) {
			buffer.mark();
			int size = buffer.getInt();
			if (size > MAX_MESSAGE_SIZE) {
				throw new TProtocolException(TProtocolException.SIZE_LIMIT, "invalid message size: " + size);
			}
			if (buffer.remaining() < size) {
				buffer.reset();
				buffer.compact();
				return null;
			}
			return protocol.readMessageBegin();
		} else {
			buffer.compact();
			return null;
		}
	}

	@Override
	public void readMessageEnd() throws TException {
		protocol.readMessageEnd();
	}

	@Override
	public TStruct readStructBegin() throws TException {
		return protocol.readStructBegin();
	}

	@Override
	public void readStructEnd() throws TException {
		protocol.readStructEnd();
	}

	@Override
	public TField readFieldBegin() throws TException {
		return protocol.readFieldBegin();
	}

	@Override
	public void readFieldEnd() throws TException {
		protocol.readFieldEnd();
	}

	@Override
	public TMap readMapBegin() throws TException {
		return protocol.readMapBegin();
	}

	@Override
	public void readMapEnd() throws TException {
		protocol.readMapEnd();
	}

	@Override
	public TList readListBegin() throws TException {
		return protocol.readListBegin();
	}

	@Override
	public void readListEnd() throws TException {
		protocol.readListEnd();
	}

	@Override
	public TSet readSetBegin() throws TException {
		return protocol.readSetBegin();
	}

	@Override
	public void readSetEnd() throws TException {
		protocol.readSetEnd();
	}

	@Override
	public boolean readBool() throws TException {
		return protocol.readBool();
	}

	@Override
	public byte readByte() throws TException {
		return protocol.readByte();
	}

	@Override
	public short readI16() throws TException {
		return protocol.readI16();
	}

	@Override
	public int readI32() throws TException {
		return protocol.readI32();
	}

	@Override
	public long readI64() throws TException {
		return protocol.readI64();
	}

	@Override
	public double readDouble() throws TException {
		return protocol.readDouble();
	}

	@Override
	public String readString() throws TException {
		return protocol.readString();
	}

	@Override
	public byte[] readBinary() throws TException {
		return protocol.readBinary();
	}

}
