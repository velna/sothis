package org.sothis.mvc.http.netty;

import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.sothis.mvc.Headers;
import org.sothis.mvc.Response;

public class NettyHttpResponse implements Response {
	private final FullHttpResponse response;
	private Headers headers;
	private boolean committed;
	private ByteBufOutputStream ouputStream;
	private PrintWriter writer;

	public NettyHttpResponse(FullHttpResponse response) {
		super();
		this.response = response;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		if (null == ouputStream) {
			ouputStream = new ByteBufOutputStream(response.content());
		}
		return ouputStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (null == writer) {
			writer = new PrintWriter(getOutputStream());
		}
		return writer;
	}

	@Override
	public Headers headers() {
		if (null == headers) {
			headers = new NettyHttpHeaders(response.headers());
		}
		return headers;
	}

	@Override
	public int getStatus() {
		return response.getStatus().code();
	}

	@Override
	public void setStatus(int status) {
		response.setStatus(HttpResponseStatus.valueOf(status));
	}

	@Override
	public String getProtocolVersion() {
		return response.getProtocolVersion().text();
	}

	@Override
	public void setProtocolVersion(String protocolVersion) {
		response.setProtocolVersion(HttpVersion.valueOf(protocolVersion));
	}

	@Override
	public String getCharset() {
		return headers().getString(Names.CONTENT_ENCODING);
	}

	@Override
	public void setCharset(String charset) throws UnsupportedEncodingException {
		headers().setString(Names.CONTENT_ENCODING, charset);
	}

	public void commit() throws IOException {
		if (committed) {
			throw new IllegalStateException("response has already committed.");
		}
		committed = true;
		if (null != this.writer) {
			this.writer.flush();
		} else {
			this.getOutputStream().flush();
		}
		headers().setInteger(Names.CONTENT_LENGTH, response.content().readableBytes());
		headers().setString(Names.CONNECTION, Values.KEEP_ALIVE);
	}

	@Override
	public boolean isCommitted() {
		return committed;
	}

	@Override
	public void reset() {
		response.setStatus(HttpResponseStatus.OK);
		response.content().clear();
		response.headers().clear();
	}

}
