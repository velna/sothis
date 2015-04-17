package org.sothis.mvc.http;

import org.sothis.mvc.Request;

public interface HttpRequest extends Request, HttpMessage {
	String getMethod();

	String getQueryString();
}
