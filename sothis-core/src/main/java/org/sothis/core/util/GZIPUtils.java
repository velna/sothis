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
	public static byte[] deflate(byte[] data) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
		GZIPOutputStream zipOut = new GZIPOutputStream(out, 8192);
		zipOut.write(data);
		zipOut.close();
		return out.toByteArray();
	}

	/**
	 * 将data以gzip方式解压缩并返回
	 * 
	 * @param data
	 * @return 返回解压缩后的数据
	 * @throws IOException
	 */
	public static byte[] inflate(byte[] data) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
		ByteArrayInputStream input = new ByteArrayInputStream(data);
		GZIPInputStream zipIn = new GZIPInputStream(input, 8192);
		byte[] buf = new byte[8192];
		int n;
		while ((n = zipIn.read(buf)) != -1) {
			out.write(buf, 0, n);
		}
		zipIn.close();
		out.close();
		return out.toByteArray();
	}

	/**
	 * 将gzip格式的输入in转换成解压后的输入
	 * 
	 * @param in
	 * @return 返回解压后的输入
	 * @throws IOException
	 */
	public static InputStream deflate(InputStream in) throws IOException {
		return new GZIPInputStream(in);
	}

	/**
	 * 将输出转换成gzip压缩后的输出
	 * 
	 * @param out
	 * @return 返回gzip压缩后的输出
	 * @throws IOException
	 */
	public static OutputStream inflate(OutputStream out) throws IOException {
		return new GZIPOutputStream(out);
	}

}
