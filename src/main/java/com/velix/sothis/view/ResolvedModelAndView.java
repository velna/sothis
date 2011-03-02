package com.velix.sothis.view;

import java.util.Map;

public class ResolvedModelAndView {
	private final Map<String, Object> model;
	private final View view;

	public ResolvedModelAndView(Map<String, Object> model, View view) {
		this.model = model;
		this.view = view;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public View getView() {
		return view;
	}

}
