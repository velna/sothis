package org.sothis.mvc.echoapp;

import java.io.IOException;
import java.io.OutputStream;

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

}
