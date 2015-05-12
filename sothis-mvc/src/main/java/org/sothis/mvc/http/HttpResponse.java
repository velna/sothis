package org.sothis.mvc.http;

import java.io.IOException;
import java.io.PrintWriter;

import org.sothis.mvc.Response;

public interface HttpResponse extends Response, HttpMessage {

	PrintWriter getWriter() throws IOException;

	int getStatus();

	void setStatus(int status);

	void setProtocolVersion(String protocolVersion);
}
