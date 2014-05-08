package org.sothis.nios.codec.thrift;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public abstract class TResponse {
	private final int tid;

	public TResponse(int tid) {
		super();
		this.tid = tid;
	}

	protected void skip(ByteBuf buf, byte type) {
		int size;
		switch (type) {
		case TType.BOOL:
			buf.readBoolean();
			break;

		case TType.BYTE:
			buf.readByte();
			break;

		case TType.I16:
			buf.readShort();
			break;

		case TType.I32:
			buf.readInt();
			break;

		case TType.I64:
			buf.readLong();
			break;

		case TType.DOUBLE:
			buf.readDouble();
			break;

		case TType.STRING:
			size = buf.readInt();
			while ((size--) > 0) {
				buf.readByte();
			}
			break;

		case TType.STRUCT:
			while (true) {
				byte ftype = buf.readByte();
				buf.readShort();
				if (ftype == TType.STOP) {
					break;
				}
				skip(buf, ftype);
			}
			break;

		case TType.MAP:
			byte keyType = buf.readByte();
			byte valueType = buf.readByte();
			size = buf.readInt();
			for (int i = 0; i < size; i++) {
				skip(buf, keyType);
				skip(buf, valueType);
			}
			break;

		case TType.SET:
		case TType.LIST:
			byte eType = buf.readByte();
			size = buf.readInt();
			for (int i = 0; i < size; i++) {
				skip(buf, eType);
			}
			break;

		default:
			break;
		}
	}

	abstract public boolean decode(ByteBuf buf) throws IOException;

	public int getTid() {
		return tid;
	}

}
