package org.sothis.web.mvc.view;

public class DefaultModelAndView implements ModelAndView {

	private final Object model;
	private final Object[] viewParams;
	private final String viewType;

	public DefaultModelAndView(Object model, String viewType,
			Object... viewParams) {
		this.model = model;
		this.viewType = viewType;
		this.viewParams = viewParams;
	}

	@Override
	public Object getModel() {
		return model;
	}

	@Override
	public Object[] getViewParams() {
		return viewParams;
	}

	@Override
	public String getViewType() {
		return viewType;
	}
}
