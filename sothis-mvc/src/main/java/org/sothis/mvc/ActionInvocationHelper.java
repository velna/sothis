package org.sothis.mvc;

public class ActionInvocationHelper {
	public static boolean invoke(ActionContext context) throws Exception {
		actionContextCheck(context);
		ActionInvocation invocation = prepareActionInvocation(context);
		if (null != invocation) {
			Object result = invocation.invoke();
			if (!context.isAsyncStarted()) {
				render(invocation, result);
			}
			return true;
		} else {
			return false;
		}
	}

	public static void render(ActionInvocation invocation, Object result) throws Exception {
		ModelAndViewResolver mavResolver = (ModelAndViewResolver) invocation.getActionContext().get(
				ActionContext.MODEL_AND_VIEW_RESOLVER);
		ResolvedModelAndView mav = mavResolver.resolve(result, invocation);
		mav.getView().render(mav.getModelAndView(), invocation);
	}

	private static void actionContextCheck(ActionContext context) {
		if (null == context) {
			throw new IllegalArgumentException("context can not be null!");
		}
		checkContextKeyValue(context, ActionContext.MODEL_AND_VIEW_RESOLVER, ModelAndViewResolver.class);
		checkContextKeyValue(context, ActionContext.ACTION_MAPPER, ActionMapper.class);
	}

	private static void checkContextKeyValue(ActionContext context, String key, Class<?> valueClass) {
		if (!valueClass.isInstance(context.get(key))) {
			throw new IllegalArgumentException("context.get(" + key + ") is not a valid instance of " + valueClass);
		}
	}

	private static ActionInvocation prepareActionInvocation(ActionContext context) {
		ActionInvocation invocation = (ActionInvocation) context.get(ActionContext.ACTION_INVOCATION);
		if (null == invocation) {
			ActionMapper actionMapper = (ActionMapper) context.get(ActionContext.ACTION_MAPPER);

			Action action = actionMapper.resolve(context);
			if (null == action) {
				return null;
			}

			context.put(ActionContext.ACTION, action);

			context.put(ActionContext.ACTION_PARAMS, new Object[] { context.getRequest(), context.getResponse() });

			Object controllerInstance = context.getApplicationContext().getBeanFactory()
					.getBean(action.getController().getControllerClass());
			invocation = new DefaultActionInvocation(controllerInstance, context);
			context.put(ActionContext.ACTION_INVOCATION, invocation);
		}
		return invocation;
	}

}
