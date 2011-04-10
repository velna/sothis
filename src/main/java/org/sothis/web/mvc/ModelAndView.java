package org.sothis.web.mvc;

import java.util.Map;

public interface ModelAndView {

	public Object getModel();

	public String getViewType();

	public Map<String, Object> getViewParams();
}
