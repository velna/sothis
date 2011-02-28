package com.velix.sothis;

import javax.servlet.http.HttpServletResponse;

public interface HttpServletResponseAware {
	void setResponse(HttpServletResponse response);
}
