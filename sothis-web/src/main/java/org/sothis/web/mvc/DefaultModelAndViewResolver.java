package org.sothis.web.mvc;

import java.util.Collections;
import java.util.Map;

import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.web.mvc.annotation.Sothis;

/**
 * ModelAndViewResolver接口的默认实现
 * 
 * @author velna
 * 
 */
@Bean(scope = Scope.SINGLETON)
public class DefaultModelAndViewResolver implements ModelAndViewResolver {

	public View getView(String typeName, final ActionInvocation invocation) throws ViewCreationException {
		return createView(typeName, invocation.getActionContext());
	}

	public ResolvedModelAndView resolve(final Object actionResult, final ActionInvocation invocation)
			throws ViewCreationException {
		ModelAndView model = null;
		View view = null;
		Sothis sothis = invocation.getAction().getAnnotation(Sothis.class);
		String viewType = DEFAULT_VIEW_TYPE;
		if (null != sothis && null != sothis.defaultView()) {
			viewType = sothis.defaultView();
		}
		if (actionResult instanceof ModelAndView) {
			model = (ModelAndView) actionResult;
			if (!model.viewType().equals(DEFAULT_VIEW_TYPE)) {
				viewType = model.viewType();
			}
			view = createView(viewType, invocation.getActionContext());
		} else {
			view = createView(viewType, invocation.getActionContext());
			final String fv = viewType;
			model = new ModelAndView() {

				public Object model() {
					return actionResult;
				}

				public String viewType() {
					return fv;
				}

				public Map<String, Object> viewParams() {
					return Collections.emptyMap();
				}

			};
		}

		return new ResolvedModelAndView(model, view);
	}

	private View createView(String typeName, ActionContext context) throws ViewCreationException {
		if (null == typeName) {
			typeName = DEFAULT_VIEW_TYPE;
		}
		SothisConfig config = (SothisConfig) context.get(ActionContext.SOTHIS_CONFIG);
		Class<? extends View> viewClass = DEFAULT_VIEW_TYPE.equals(typeName) ? config.getDefaultView() : config.getViews().get(
				typeName);
		if (null == viewClass) {
			throw new ViewCreationException("no view type of:" + typeName);
		}
		try {
			return context.getBeanFactory().getBean(viewClass);
		} catch (Exception e) {
			throw new ViewCreationException("error creation view of class " + viewClass, e);
		}
	}

}
