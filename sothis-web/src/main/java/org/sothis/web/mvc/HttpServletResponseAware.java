package org.sothis.web.mvc;

import javax.servlet.http.HttpServletResponse;

public interface HttpServletResponseAware {
	void setResponse(HttpServletResponse response);
}
