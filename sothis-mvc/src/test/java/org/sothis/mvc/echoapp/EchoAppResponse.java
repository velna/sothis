package org.sothis.mvc.echoapp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.sothis.mvc.Headers;
import org.sothis.mvc.Response;

public class EchoAppResponse implements Response {

	@Override
	public OutputStream getOutputStream() throws IOException {
		return System.out;
	}

	@Override
	public boolean isCommitted() {
		return false;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return null;
	}

	@Override
	public Headers headers() {
		return null;
	}

	@Override
	public String getProtocolVersion() {
		return null;
	}

	@Override
	public void setProtocolVersion(String protocolVersion) {

	}

	@Override
	public Charset getCharset() {
		return null;
	}

	@Override
	public void setCharset(Charset charset) throws UnsupportedEncodingException {

	}

	@Override
	public int getStatus() {
		return 0;
	}

	@Override
	public void setStatus(int status) {

	}

	@Override
	public void reset() {

	}

}
