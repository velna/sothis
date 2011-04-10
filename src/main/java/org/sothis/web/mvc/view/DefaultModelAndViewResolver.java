package org.sothis.web.mvc.view;

import java.util.HashMap;
import java.util.Map;

import org.sothis.web.mvc.ActionInvocation;

public class DefaultModelAndViewResolver implements ModelAndViewResolver {

	private final Map<String, Class<? extends View>> viewMap = new HashMap<String, Class<? extends View>>();
	private static final String DEFAULT_VIEW_KEY = "org.sothis.web.mvc.view.DEFAULT_VIEW_KEY";
	private Class<? extends View> defaultView;

	@Override
	public ResolvedModelAndView resolve(Object actionResult,
			ActionInvocation invocation) {
		Object model = null;
		View view = null;
		if (actionResult == null) {
			view = createDefaultView(invocation.getAction().getName());
		} else if (actionResult instanceof ModelAndView) {
			ModelAndView mav = (ModelAndView) actionResult;
			view = createView(mav.getViewType(), mav.getViewParams());
			model = mav.getModel();
		} else {
			view = createDefaultView(invocation.getAction().getName());
			model = actionResult;
		}

		return new ResolvedModelAndView(model, view);
	}

	private View createDefaultView(Object... params) {
		return createView(DEFAULT_VIEW_KEY, params);
	}

	private View createView(String typeName, Object... params) {
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
					+ defaultView, e);
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
