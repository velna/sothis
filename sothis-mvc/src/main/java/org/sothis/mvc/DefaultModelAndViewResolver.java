package org.sothis.mvc;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;

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

	public ResolvedModelAndView resolve(final Object actionResult, final ActionInvocation invocation) throws ViewCreationException {
		ModelAndView model = null;
		View view = null;
		Sothis[] ss = invocation.getAction().getAnnotation(Sothis.class);
		String viewType = DEFAULT_VIEW_TYPE;
		for (Sothis sothis : ss) {
			if (null != sothis && StringUtils.isNotEmpty(sothis.defaultView())) {
				viewType = sothis.defaultView();
				break;
			}
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
		Configuration config = context.getApplicationContext().getConfiguration();
		Class<? extends View> viewClass = DEFAULT_VIEW_TYPE == typeName ? config.getDefaultView() : config.getView(typeName);
		if (null == viewClass) {
			throw new ViewCreationException("no view type of:" + typeName);
		}
		return context.getApplicationContext().getBeanFactory().getBean(viewClass);
	}

}
