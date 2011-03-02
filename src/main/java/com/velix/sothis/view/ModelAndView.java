package com.velix.sothis.view;

public class ModelAndView {

	private final Object model;
	private final Object[] viewParams;

	public ModelAndView(Object model, Object... viewParams) {
		this.model = model;
		this.viewParams = viewParams;
	}

	public Object getModel() {
		return model;
	}

	public Object[] getViewParams() {
		return viewParams;
	}
}
