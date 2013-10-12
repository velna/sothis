package org.sothis.web.mvc.interceptors.param;

import javax.servlet.http.HttpServletRequest;

public interface HttpServletRequestAware {
	void setRequest(HttpServletRequest request);
}
