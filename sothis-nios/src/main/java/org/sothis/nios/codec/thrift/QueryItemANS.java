package org.sothis.nios.codec.thrift;

import java.io.IOException;

public class QueryItemANS extends TResponse {

	// private DecodeStatus status = DecodeStatus.FIELD_BEGIN;
	// private ANSDecodeStatus ansStatus = ANSDecodeStatus.FIELD_BEGIN;

	private int code;
	private byte[] key;
	private byte[] value;

	public QueryItemANS(int tid) {
		super(tid);
	}

	// private static enum ANSDecodeStatus {
	// FIELD_BEGIN, FIELD_CODE, FIELD_KEY, FIELD_VALUE, DONE, SKIP
	// }
	//
	// private static enum DecodeStatus {
	// FIELD_BEGIN, STRUCT_ANS, SKIP, DONE
	// }

	private boolean decodeANS(ByteBuf buf) throws IOException {
		while (true) {
			byte fieldType = buf.readByte();
			if (fieldType == TType.STOP) {
				break;
			}
			short fieldId = buf.readShort();
			switch (fieldId) {
			case 1:
				if (fieldType == TType.I32) {
					code = buf.readInt();
				} else {
					skip(buf, fieldType);
				}
				break;
			case 2:
				if (fieldType == TType.STRING) {
					buf.markReaderIndex();
					int size = buf.readInt();
					key = new byte[size];
					buf.readBytes(key);
				} else {
					skip(buf, fieldType);
				}
				break;
			case 3:
				if (fieldType == TType.STRING) {
					buf.markReaderIndex();
					int size = buf.readInt();
					value = new byte[size];
					buf.readBytes(value);
				} else {
					skip(buf, fieldType);
				}
				break;
			default:
				skip(buf, fieldType);
				break;
			}
		}
		return true;
	}

	@Override
	public boolean decode(ByteBuf buf) throws IOException {
		while (true) {
			byte fieldType = buf.readByte();
			if (fieldType == TType.STOP) {
				break;
			}
			short fieldId = buf.readShort();
			switch (fieldId) {
			case 0:
				if (fieldType == TType.STRUCT) {
					decodeANS(buf);
				} else {
					skip(buf, fieldType);
				}
				break;
			default:
				skip(buf, fieldType);
				break;
			}
		}
		return true;
	}

	// private boolean _decode(ByteBuf buf) throws IOException {
	// if (status == DecodeStatus.DONE) {
	// throw new IllegalStateException();
	// }
	// while (true) {
	// if (status == DecodeStatus.FIELD_BEGIN) {
	// if (buf.readableBytes() >= 3) {
	// byte fieldType = buf.readByte();
	// if (fieldType == TType.STOP) {
	// status = DecodeStatus.DONE;
	// break;
	// }
	// short fieldId = buf.readShort();
	// switch (fieldId) {
	// case 0:
	// if (fieldType == TType.STRUCT) {
	// status = DecodeStatus.STRUCT_ANS;
	// } else {
	// skipContext.init(fieldType);
	// status = DecodeStatus.SKIP;
	// }
	// break;
	// default:
	// skipContext.init(fieldType);
	// status = DecodeStatus.SKIP;
	// break;
	// }
	// } else {
	// break;
	// }
	// }
	// if (status == DecodeStatus.STRUCT_ANS) {
	// if (decodeANS(buf)) {
	// status = DecodeStatus.FIELD_BEGIN;
	// } else {
	// break;
	// }
	// }
	// if (status == DecodeStatus.SKIP) {
	// if (skipContext.skip(buf)) {
	// status = DecodeStatus.FIELD_BEGIN;
	// } else {
	// break;
	// }
	// }
	// }
	// return status == DecodeStatus.DONE;
	// }
	//
	// private boolean _decodeANS(ByteBuf buf) throws IOException {
	// if (ansStatus == ANSDecodeStatus.DONE) {
	// throw new IllegalStateException();
	// }
	// while (true) {
	// if (ansStatus == ANSDecodeStatus.FIELD_BEGIN) {
	// if (buf.readableBytes() >= 3) {
	// byte fieldType = buf.readByte();
	// if (fieldType == TType.STOP) {
	// ansStatus = ANSDecodeStatus.DONE;
	// break;
	// }
	// short fieldId = buf.readShort();
	// switch (fieldId) {
	// case 1:
	// if (fieldType == TType.I32) {
	// ansStatus = ANSDecodeStatus.FIELD_CODE;
	// } else {
	// skipContext.init(fieldType);
	// ansStatus = ANSDecodeStatus.SKIP;
	// }
	// break;
	// case 2:
	// if (fieldType == TType.STRING) {
	// ansStatus = ANSDecodeStatus.FIELD_KEY;
	// } else {
	// skipContext.init(fieldType);
	// ansStatus = ANSDecodeStatus.SKIP;
	// }
	// break;
	// case 3:
	// if (fieldType == TType.STRING) {
	// ansStatus = ANSDecodeStatus.FIELD_VALUE;
	// } else {
	// skipContext.init(fieldType);
	// ansStatus = ANSDecodeStatus.SKIP;
	// }
	// break;
	// default:
	// skipContext.init(fieldType);
	// ansStatus = ANSDecodeStatus.SKIP;
	// break;
	// }
	// } else {
	// break;
	// }
	// }
	// if (ansStatus == ANSDecodeStatus.FIELD_CODE) {
	// if (buf.readableBytes() >= 4) {
	// code = buf.readInt();
	// status = DecodeStatus.FIELD_BEGIN;
	// } else {
	// break;
	// }
	// }
	// if (ansStatus == ANSDecodeStatus.FIELD_KEY) {
	// if (buf.readableBytes() >= 4) {
	// buf.markReaderIndex();
	// int size = buf.readInt();
	// if (buf.readableBytes() >= size) {
	// key = new byte[size];
	// buf.readBytes(key);
	// status = DecodeStatus.FIELD_BEGIN;
	// } else {
	// buf.resetReaderIndex();
	// }
	// } else {
	// break;
	// }
	// }
	// if (ansStatus == ANSDecodeStatus.FIELD_VALUE) {
	// if (buf.readableBytes() >= 4) {
	// buf.markReaderIndex();
	// int size = buf.readInt();
	// if (buf.readableBytes() >= size) {
	// value = new byte[size];
	// buf.readBytes(value);
	// status = DecodeStatus.FIELD_BEGIN;
	// } else {
	// buf.resetReaderIndex();
	// }
	// } else {
	// break;
	// }
	// }
	// if (ansStatus == ANSDecodeStatus.SKIP) {
	// if (skipContext.skip(buf)) {
	// ansStatus = ANSDecodeStatus.FIELD_BEGIN;
	// } else {
	// break;
	// }
	// }
	// }
	// return ansStatus == ANSDecodeStatus.DONE;
	// }

	public int getCode() {
		return code;
	}

	public byte[] getKey() {
		return key;
	}

	public byte[] getValue() {
		return value;
	}

}
