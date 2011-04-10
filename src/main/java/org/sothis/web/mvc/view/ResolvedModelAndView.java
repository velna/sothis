package org.sothis.web.mvc.view;

public class ResolvedModelAndView {
	private final Object model;
	private final View view;

	public ResolvedModelAndView(Object model, View view) {
		this.model = model;
		this.view = view;
	}

	public Object getModel() {
		return model;
	}

	public View getView() {
		return view;
	}

}
