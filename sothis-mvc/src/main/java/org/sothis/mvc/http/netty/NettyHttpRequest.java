package org.sothis.mvc.http.netty;

import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.http.FullHttpRequest;

import java.io.IOException;
import java.io.InputStream;

import org.sothis.mvc.AbstractRequest;
import org.sothis.mvc.Session;
import org.sothis.mvc.http.HttpHeaders;
import org.sothis.mvc.http.HttpRequest;

public class NettyHttpRequest extends AbstractRequest implements HttpRequest {

	private final FullHttpRequest request;
	private InputStream inputStream;
	private HttpHeaders headers;

	public NettyHttpRequest(FullHttpRequest request) {
		super();
		this.request = request;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if (null == inputStream) {
			inputStream = new ByteBufInputStream(request.content());
		}
		return inputStream;
	}

	@Override
	public Session getSession() {
		return getSession(true);
	}

	@Override
	public Session getSession(boolean create) {
		if (create) {
			throw new UnsupportedOperationException();
		} else {
			return null;
		}
	}

	@Override
	public String getMethod() {
		return request.getMethod().name();
	}

	@Override
	public String getProtocolVersion() {
		return request.getProtocolVersion().text();
	}

	@Override
	public String getUri() {
		return request.getUri();
	}

	@Override
	public HttpHeaders headers() {
		if (null == headers) {
			headers = new NettyHttpHeaders(request.headers());
		}
		return headers;
	}

}
