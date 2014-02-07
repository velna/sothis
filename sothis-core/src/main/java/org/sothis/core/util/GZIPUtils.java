package org.sothis.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPUtils {

	/**
	 * 将data以gzip方式压缩并返回
	 * 
	 * @param data
	 * @return 返回压缩后的数据
	 * @throws IOException
	 */
	public static byte[] makeAsByteArray(byte[] data) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
		GZIPOutputStream zipOut = new GZIPOutputStream(out, Math.max(data.length / 10, 512));
		zipOut.write(data);
		zipOut.close();
		return out.toByteArray();
	}

	/**
	 * 将data以gzip方式压缩，并返回一个可读取的InputStream
	 * 
	 * @param data
	 * @return 返回可读的InputStream
	 * @throws IOException
	 */
	public static InputStream makeAsInputStream(byte[] data) throws IOException {
		return new ByteArrayInputStream(makeAsByteArray(data));
	}

	/**
	 * 将gzip格式的输入in转换成解压后的输入
	 * 
	 * @param in
	 * @return 返回解压后的输入
	 * @throws IOException
	 */
	public static InputStream wrap(InputStream in) throws IOException {
		return new GZIPInputStream(in);
	}

	/**
	 * 将输出转换成gzip压缩后的输出
	 * 
	 * @param out
	 * @return 返回gzip压缩后的输出
	 * @throws IOException
	 */
	public static OutputStream wrap(OutputStream out) throws IOException {
		return new GZIPOutputStream(out);
	}
}
