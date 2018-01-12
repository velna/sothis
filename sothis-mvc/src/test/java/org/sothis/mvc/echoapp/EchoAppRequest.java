package org.sothis.mvc.echoapp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.sothis.mvc.AbstractRequest;
import org.sothis.mvc.Attachments;
import org.sothis.mvc.DefaultSession;
import org.sothis.mvc.Headers;
import org.sothis.mvc.RequestParseExecption;
import org.sothis.mvc.Session;

public class EchoAppRequest extends AbstractRequest {

	private static final Session session = new DefaultSession("echo");

	private final String uri;
	private final InputStream inputStream;

	public EchoAppRequest(String uri, String message) throws UnsupportedEncodingException {
		this.uri = uri + "?message=" + message;
		this.inputStream = new ByteArrayInputStream(message.getBytes("UTF-8"));
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return inputStream;
	}

	@Override
	public Session getSession(boolean create) {
		return session;
	}

	@Override
	public Session getSession() {
		return session;
	}

	@Override
	public Attachments attachments() throws RequestParseExecption {
		return null;
	}

	@Override
	public String getMethod() {
		return null;
	}

	@Override
	public String getLocalAddr() {
		return null;
	}

	@Override
	public int getLocalPort() {
		return 0;
	}

	@Override
	public String getRemoteAddr() {
		return null;
	}

	@Override
	public int getRemotePort() {
		return 0;
	}

	@Override
	public Headers headers() {
		return null;
	}

	@Override
	public String getProtocol() {
		return null;
	}

	@Override
	public String getScheme() {
		return null;
	}

}
