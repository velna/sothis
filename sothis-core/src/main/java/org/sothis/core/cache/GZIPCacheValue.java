package org.sothis.core.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 经过gzip压缩的缓存值类
 * 
 * @author velna
 * 
 */
public class GZIPCacheValue extends CacheValue {

	private static final long serialVersionUID = -8276718494211480303L;

	public GZIPCacheValue(Object value, long timeToLive) {
		super(value, timeToLive);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.timeToLive = in.readLong();
		byte[] data = (byte[]) in.readObject();
		ObjectInputStream zipin = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(data)));
		value = zipin.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(timeToLive);
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		ObjectOutputStream zipout = new ObjectOutputStream(new GZIPOutputStream(byteArrayOut));
		zipout.writeObject(value);
		zipout.close();
		out.writeObject(byteArrayOut.toByteArray());
	}
}
