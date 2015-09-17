package org.sothis.mvc;

import org.sothis.core.beans.BeanFactory;

public class ActionInvocationHelper {
	public static boolean invoke(ActionContext context, ApplicationContext appContext, Request req, Response resp)
			throws Exception {
		try {
			BeanFactory beanFactory = appContext.getBeanFactory();
			Configuration config = appContext.getConfiguration();
			context.setApplicationContext(appContext);
			context.setRequest(req);
			context.setRequestParameters(req.parameters().toMap());
			context.setResponse(resp);
			context.setActionMapper(beanFactory.getBean(config.getActionMapper()));
			context.setModelAndViewResolver(beanFactory.getBean(config.getModelAndViewResolver()));
			context.setExceptionHandler(beanFactory.getBean(config.getExceptionHandler()));

			Flash flash = context.getFlash(false);
			if (null != flash) {
				flash.flash();
			}
			actionContextCheck(context);
			ActionInvocation invocation = prepareActionInvocation(context);
			if (null != invocation) {
				Object result = invocation.invoke();
				if (!context.isAsyncStarted() && invocation.getAction().getActionMethod().getReturnType() != void.class) {
					render(invocation, result);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			ExceptionHandler exceptionHandler = context.getExceptionHandler();
			if (null != exceptionHandler) {
				exceptionHandler.exceptionCaught(context, e);
			} else {
				throw e;
			}
			return false;
		} finally {
			context.clear();
		}
	}

	public static void render(ActionInvocation invocation, Object result) throws Exception {
		ModelAndViewResolver mavResolver = invocation.getActionContext().getModelAndViewResolver();
		ResolvedModelAndView mav = mavResolver.resolve(result, invocation);
		mav.getView().render(mav.getModelAndView(), invocation);
	}

	private static void actionContextCheck(ActionContext context) {
		if (null == context) {
			throw new IllegalArgumentException("context can not be null!");
		}
		if (null == context.getModelAndViewResolver()) {
			throw new IllegalArgumentException("ModelAndViewResolver can not be null!");
		}
		if (null == context.getActionMapper()) {
			throw new IllegalArgumentException("ActionMapper can not be null!");
		}
	}

	private static ActionInvocation prepareActionInvocation(ActionContext context) {
		ActionInvocation invocation = context.getActionInvocation();
		if (null == invocation) {
			ActionMapper actionMapper = context.getActionMapper();

			Action action = actionMapper.resolve(context);
			if (null == action) {
				return null;
			}

			context.setAction(action);

			context.setActionParams(new Object[] { context.getRequest(), context.getResponse() });

			Object controllerInstance = context.getApplicationContext().getBeanFactory()
					.getBean(action.getController().getControllerClass());
			invocation = new DefaultActionInvocation(controllerInstance, context);
			context.setActionInvocation(invocation);
		}
		return invocation;
	}

}
