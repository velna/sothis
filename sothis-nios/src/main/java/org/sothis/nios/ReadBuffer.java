package org.sothis.nios;

import java.io.IOException;
import java.util.Iterator;

public abstract class ReadBuffer implements Iterable<Object> {
	/**
	 * return <code>n</code> bytes read into this buffer.
	 * 
	 * @return return null indicates the end of stream and nothing read,
	 *         negative number indicates end of stream and abs(n) bytes read,
	 *         positive number indicates success read n bytes
	 * @throws IOException
	 */
	abstract Long channelRead() throws IOException;

	@Override
	public abstract Iterator<Object> iterator();

}
