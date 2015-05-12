package org.sothis.mvc.http.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.sothis.mvc.http.HttpHeaders;
import org.sothis.mvc.http.HttpResponse;

public class ServletHttpResponse implements HttpResponse {

	private final HttpServletResponse response;
	private HttpHeaders headers;
	private String protocolVersion;

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
	public HttpHeaders headers() {
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
	public String getCharset() {
		return response.getCharacterEncoding();
	}

	@Override
	public void setCharset(String charset) throws UnsupportedEncodingException {
		response.setCharacterEncoding(charset);
	}

	@Override
	public boolean isCommitted() {
		return response.isCommitted();
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return response.getWriter();
	}

}
