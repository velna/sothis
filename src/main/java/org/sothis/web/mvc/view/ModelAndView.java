package org.sothis.web.mvc.view;

public interface ModelAndView {

	public Object getModel();

	public String getViewType();

	public Object[] getViewParams();
}
