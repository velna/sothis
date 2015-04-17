package org.sothis.mvc.http;

import org.sothis.mvc.Response;

public interface HttpResponse extends Response, HttpMessage {
	int getStatus();

	void setStatus(int status);

	void setProtocolVersion(String protocolVersion);
}
