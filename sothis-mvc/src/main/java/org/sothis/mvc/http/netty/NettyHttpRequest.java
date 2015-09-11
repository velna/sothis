package org.sothis.mvc.http.netty;

import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

import org.sothis.mvc.AbstractRequest;
import org.sothis.mvc.Attachments;
import org.sothis.mvc.Headers;
import org.sothis.mvc.Session;

public class NettyHttpRequest extends AbstractRequest {

	private final FullHttpRequest request;
	private final Channel channel;
	private InputStream inputStream;
	private Headers headers;

	public NettyHttpRequest(FullHttpRequest request, Channel channel) {
		super();
		this.request = request;
		this.channel = channel;
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
	public Headers headers() {
		if (null == headers) {
			headers = new NettyHttpHeaders(request.headers());
		}
		return headers;
	}

	@Override
	public Attachments attachments() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getLocalAddr() {
		return ((InetSocketAddress) channel.localAddress()).getHostString();
	}

	@Override
	public int getLocalPort() {
		return ((InetSocketAddress) channel.localAddress()).getPort();
	}

	@Override
	public String getRemoteAddr() {
		return ((InetSocketAddress) channel.remoteAddress()).getHostString();
	}

	@Override
	public int getRemotePort() {
		return ((InetSocketAddress) channel.remoteAddress()).getPort();
	}

}
