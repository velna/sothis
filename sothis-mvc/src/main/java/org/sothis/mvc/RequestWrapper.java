package org.sothis.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class RequestWrapper implements Request {
	private final Request request;

	public RequestWrapper(Request request) {
		super();
		this.request = request;
	}

	@Override
	public String getCharset() {
		return request.getCharset();
	}

	@Override
	public void setCharset(String charset) throws UnsupportedEncodingException {
		request.setCharset(charset);
	}

	@Override
	public String getQueryString() {
		return request.getQueryString();
	}

	@Override
	public Parameters parameters() {
		return request.parameters();
	}

	@Override
	public Attachments attachments() throws IOException {
		return request.attachments();
	}

	@Override
	public Attributes attributes() {
		return request.attributes();
	}

	@Override
	public String getUri() {
		return request.getUri();
	}

	@Override
	public String getUriPath() {
		return request.getUriPath();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

	@Override
	public Session getSession() {
		return request.getSession();
	}

	@Override
	public Session getSession(boolean create) {
		return request.getSession(create);
	}

}
