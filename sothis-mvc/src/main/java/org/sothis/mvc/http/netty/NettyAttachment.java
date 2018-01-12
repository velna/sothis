package org.sothis.mvc.http.netty;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.sothis.mvc.Attachment;
import org.sothis.mvc.Headers;
import org.sothis.mvc.ReadOnlyHeaders;

import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.http.multipart.HttpData;

public abstract class NettyAttachment implements Attachment {

	protected final HttpData httpData;
	private InputStream inputstream;

	public NettyAttachment(HttpData httpData) {
		super();
		this.httpData = httpData;
	}

	@Override
	public Charset getCharset() {
		return httpData.getCharset();
	}

	@Override
	public String getString(Charset charset) throws IOException {
		return httpData.getString(null == httpData.getCharset() ? charset : httpData.getCharset());
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if (null == inputstream) {
			inputstream = new ByteBufInputStream(httpData.getByteBuf());
		}
		return inputstream;
	}

	@Override
	public String getName() {
		return httpData.getName();
	}

	@Override
	public long getSize() {
		return httpData.length();
	}

	@Override
	public void delete() throws IOException {
		httpData.delete();
	}

	@Override
	public Headers headers() {
		return ReadOnlyHeaders.EMPTY;
	}

}
