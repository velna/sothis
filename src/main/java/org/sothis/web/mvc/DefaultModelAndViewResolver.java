package org.sothis.web.mvc;

import java.util.HashMap;
import java.util.Map;

public class DefaultModelAndViewResolver implements ModelAndViewResolver {

	private final Map<String, Class<? extends View>> viewMap = new HashMap<String, Class<? extends View>>();

	@Override
	public ResolvedModelAndView resolve(Object actionResult,
			ActionInvocation invocation) {
		Object model = null;
		View view = null;
		if (actionResult == null) {
			view = createDefaultView();
		} else if (actionResult instanceof ModelAndView) {
			ModelAndView mav = (ModelAndView) actionResult;
			view = createView(mav.viewType(), mav.viewParams());
			model = mav.model();
		} else {
			view = createDefaultView();
			model = actionResult;
		}

		return new ResolvedModelAndView(model, view);
	}

	private View createDefaultView() {
		return createView(DEFAULT_VIEW_KEY, (Map<String, Object>) null);
	}

	private View createView(String typeName, Map<String, Object> params) {
		Class<? extends View> viewClass = this.viewMap.get(typeName);
		if (null == viewClass) {
			throw new ViewCreationException("no view type of:" + typeName);
		}
		try {
			View view = viewClass.newInstance();
			view.setParams(params);
			return view;
		} catch (Exception e) {
			throw new ViewCreationException("error creation view of class "
					+ viewClass, e);
		}
	}

	@Override
	public void register(String typeName, Class<? extends View> viewClass) {
		viewMap.put(typeName, viewClass);
	}

	@Override
	public void setDefaultView(Class<? extends View> viewClass) {
		viewMap.put(DEFAULT_VIEW_KEY, viewClass);
	}
}
