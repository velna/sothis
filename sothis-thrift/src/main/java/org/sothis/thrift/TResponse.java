package org.sothis.thrift;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class TResponse {
	private final int tid;

	public TResponse(int tid) {
		super();
		this.tid = tid;
	}

	protected void skip(ByteBuffer buf, byte type) {
		int size;
		switch (type) {
		case TType.BOOL:
			buf.get();
			break;

		case TType.BYTE:
			buf.get();
			break;

		case TType.I16:
			buf.getShort();
			break;

		case TType.I32:
			buf.getInt();
			break;

		case TType.I64:
			buf.getLong();
			break;

		case TType.DOUBLE:
			buf.getDouble();
			break;

		case TType.STRING:
			size = buf.getInt();
			while ((size--) > 0) {
				buf.get();
			}
			break;

		case TType.STRUCT:
			while (true) {
				byte ftype = buf.get();
				buf.getShort();
				if (ftype == TType.STOP) {
					break;
				}
				skip(buf, ftype);
			}
			break;

		case TType.MAP:
			byte keyType = buf.get();
			byte valueType = buf.get();
			size = buf.getInt();
			for (int i = 0; i < size; i++) {
				skip(buf, keyType);
				skip(buf, valueType);
			}
			break;

		case TType.SET:
		case TType.LIST:
			byte eType = buf.get();
			size = buf.getInt();
			for (int i = 0; i < size; i++) {
				skip(buf, eType);
			}
			break;

		default:
			break;
		}
	}

	abstract public void decode(ByteBuffer buf) throws IOException;

	public int getTid() {
		return tid;
	}

}
