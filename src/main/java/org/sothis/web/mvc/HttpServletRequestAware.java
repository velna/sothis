package org.sothis.web.mvc;

import javax.servlet.http.HttpServletRequest;

public interface HttpServletRequestAware {
	void setRequest(HttpServletRequest request);
}
