package org.sothis.web.mvc;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.sothis.mvc.Response;

public class WebResponse extends HttpServletResponseWrapper implements Response {

	public WebResponse(HttpServletResponse response) {
		super(response);
	}

}
