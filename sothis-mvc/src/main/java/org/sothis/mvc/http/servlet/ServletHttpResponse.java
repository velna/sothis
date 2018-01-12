package org.sothis.mvc.http.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import org.sothis.mvc.Headers;
import org.sothis.mvc.Response;

public class ServletHttpResponse implements Response {

	private final HttpServletResponse response;
	private Headers headers;
	private String protocolVersion;
	private Charset charset;

	public ServletHttpResponse(HttpServletResponse response, String protocolVersion) {
		super();
		this.response = response;
		this.protocolVersion = protocolVersion;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return response.getOutputStream();
	}

	@Override
	public Headers headers() {
		if (null == headers) {
			headers = new ServletResponseHttpHeaders(response);
		}
		return headers;
	}

	@Override
	public int getStatus() {
		return response.getStatus();
	}

	@Override
	public void setStatus(int status) {
		response.setStatus(status);
	}

	@Override
	public String getProtocolVersion() {
		return protocolVersion;
	}

	@Override
	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	@Override
	public Charset getCharset() {
		if (null == charset) {
			String encoding = response.getCharacterEncoding();
			charset = null == encoding ? null : Charset.forName(encoding);
		}
		return charset;
	}

	@Override
	public void setCharset(Charset charset) throws UnsupportedEncodingException {
		this.charset = charset;
		response.setCharacterEncoding(charset.name());
	}

	@Override
	public boolean isCommitted() {
		return response.isCommitted();
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return response.getWriter();
	}

	@Override
	public void reset() {
		response.reset();
	}

}
