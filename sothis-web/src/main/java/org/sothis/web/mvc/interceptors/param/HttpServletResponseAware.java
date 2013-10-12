package org.sothis.web.mvc.interceptors.param;

import javax.servlet.http.HttpServletResponse;

public interface HttpServletResponseAware {
	void setResponse(HttpServletResponse response);
}
