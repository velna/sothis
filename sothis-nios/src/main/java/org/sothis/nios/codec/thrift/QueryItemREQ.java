package org.sothis.nios.codec.thrift;

import java.io.IOException;
import java.nio.ByteBuffer;

public class QueryItemREQ implements TMessage {
	private final static TField KEY_FIELD_DESC = new TField("key", TType.STRING, (short) 1);

	private final byte[] key;

	public QueryItemREQ(byte[] key) {
		this.key = key;
	}

	@Override
	public void encode(ByteBuffer buf) throws IOException {
		buf.put(KEY_FIELD_DESC.type);
		buf.putShort(KEY_FIELD_DESC.id);
		buf.putInt(key.length);
		buf.put(key);

		buf.put(TType.STOP);
	}

}
