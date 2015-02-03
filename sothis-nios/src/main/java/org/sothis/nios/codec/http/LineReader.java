package org.sothis.nios.codec.http;

import java.nio.ByteBuffer;
import java.util.Iterator;

import org.sothis.nios.ReadBuffer;

public class LineReader {
	public final static String EMPTY_LINE = new String("");

	private final StringBuilder line = new StringBuilder();
	private int matchCount = 0;

	public int size() {
		return line.length();
	}

	public String readLine(ReadBuffer in) {
		for (Iterator<Object> i = in.iterator(); i.hasNext();) {
			ByteBuffer buf = (ByteBuffer) i.next();
			while (buf.hasRemaining()) {
				byte ch = buf.get();
				line.append((char) (ch & 0xff));
				switch (ch) {
				case '\r':
					matchCount++;
					break;
				case '\n':
					matchCount++;
					if (matchCount == 2) {
						String ret = line.toString().trim();
						line.delete(0, line.length());
						return ret;
					} else if (matchCount == 4) {
						matchCount = 0;
						return EMPTY_LINE;
					}
				default:
					matchCount = 0;
					break;
				}
			}
		}
		return null;
	}
}
