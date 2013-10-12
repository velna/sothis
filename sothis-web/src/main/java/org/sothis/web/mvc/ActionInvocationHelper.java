package org.sothis.web.mvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sothis.core.beans.BeanInstantiationException;

public class ActionInvocationHelper {
	public static void invoke(ActionContext context) throws ServletException, IOException {
		ExceptionHandler exceptionHandler = context.getExceptionHandler();
		if (null != exceptionHandler) {
			try {
				doInvoke(context);
			} catch (ServletException e) {
				exceptionHandler.onExcetion(e.getRootCause());
			} catch (Throwable e) {
				exceptionHandler.onExcetion(e);
			}
		} else {
			try {
				doInvoke(context);
			} catch (BeanInstantiationException e) {
				throw new ServletException(e);
			} catch (ViewCreationException e) {
				throw new ServletException(e);
			}
		}
	}

	private static void doInvoke(ActionContext context) throws ServletException, IOException, BeanInstantiationException, ViewCreationException {
		actionContextCheck(context);
		HttpServletRequest request = context.getRequest();
		HttpServletResponse response = context.getResponse();
		SothisConfig config = (SothisConfig) context.get(ActionContext.SOTHIS_CONFIG);
		ModelAndViewResolver mavResolver = (ModelAndViewResolver) context.get(ActionContext.MODEL_AND_VIEW_RESOLVER);
		if (null == request.getCharacterEncoding()) {
			request.setCharacterEncoding(config.getCharacterEncoding());
		}
		ActionInvocation invocation = prepareActionInvocation(context, request, response, config);
		if (null != invocation) {
			Object result = invocation.invoke();
			ResolvedModelAndView mav = mavResolver.resolve(result, invocation);
			mav.getView().render(mav.getModelAndView(), invocation);
		} else {
			if (!response.isCommitted()) {
				request.getRequestDispatcher(request.getRequestURI().substring(request.getContextPath().length())).forward(request, response);
			}
		}
		response.flushBuffer();
	}

	private static void actionContextCheck(ActionContext context) {
		if (null == context) {
			throw new IllegalArgumentException("context can not be null!");
		}
		checkContextKeyValue(context, ActionContext.HTTP_REQUEST, HttpServletRequest.class);
		checkContextKeyValue(context, ActionContext.HTTP_RESPONSE, HttpServletResponse.class);
		checkContextKeyValue(context, ActionContext.SOTHIS_CONFIG, SothisConfig.class);
		checkContextKeyValue(context, ActionContext.MODEL_AND_VIEW_RESOLVER, ModelAndViewResolver.class);
		checkContextKeyValue(context, ActionContext.ACTION_MAPPER, ActionMapper.class);
		checkContextKeyValue(context, ActionContext.ACTION_STORE, ActionStore.class);
	}

	private static void checkContextKeyValue(ActionContext context, String key, Class<?> valueClass) {
		if (!valueClass.isInstance(context.get(key))) {
			throw new IllegalArgumentException("context.get(" + key + ") is not a valid instance of " + valueClass);
		}
	}

	@SuppressWarnings("unchecked")
	private static ActionInvocation prepareActionInvocation(ActionContext context, HttpServletRequest request, HttpServletResponse response, SothisConfig config)
			throws IOException, BeanInstantiationException, ServletException {
		ActionMapper actionMapper = (ActionMapper) context.get(ActionContext.ACTION_MAPPER);
		ActionStore actions = (ActionStore) context.get(ActionContext.ACTION_STORE);

		context.setParameters(new HashMap<String, Object[]>(request.getParameterMap()));

		Action action = actionMapper.resolve(request, response, actions);
		if (null == action) {
			return null;
		}

		context.set(ActionContext.ACTION, action);
		request.setAttribute(ActionContext.ACTION_URI, action.getFullName());

		Object controllerInstance = context.getBeanFactory().getBean(action.getController().getControllerClass());
		return new DefaultActionInvocation(action, controllerInstance, context);
	}

}
