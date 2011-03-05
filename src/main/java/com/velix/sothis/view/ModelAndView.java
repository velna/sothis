package com.velix.sothis.view;

public interface ModelAndView {

	public Object getModel();

	public String getViewType();

	public Object[] getViewParams();
}
