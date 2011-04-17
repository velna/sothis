package org.sothis.web.mvc;

import java.util.Map;

public interface ModelAndView {

	public Object model();

	public String viewType();

	public Map<String, Object> viewParams();
}
