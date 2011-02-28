package com.velix.sothis;

import javax.servlet.http.HttpServletRequest;

public interface HttpServletRequestAware {
	void setRequest(HttpServletRequest request);
}
