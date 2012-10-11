package org.sothis.core.cache;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 缓存值。实际放入缓存的为这个类。
 * 
 * @author velna
 * 
 */
public class CacheValue implements Externalizable {

	protected long timeToLive;
	protected Object value;

	/**
	 * 创建一个值为{@code value}，存活时间为{@code timeToLive}秒的缓存
	 * 
	 * @param value
	 * @param timeToLive
	 *            单位：秒
	 */
	public CacheValue(Object value, long timeToLive) {
		this.value = value;
		this.timeToLive = timeToLive;
	}

	/**
	 * 实际缓存的值
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <V> V getValue() {
		return (V) value;
	}

	/**
	 * 缓存存活时间
	 * 
	 * @return 单位：秒
	 */
	public long getTimeToLive() {
		return timeToLive;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(timeToLive);
		out.writeObject(value);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.timeToLive = in.readLong();
		this.value = in.readObject();
	}

}
