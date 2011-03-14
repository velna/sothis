package org.sothis.web.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Controller {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Class<?> controllerClass;
	private final Map<String, Action> actionMap;
	private final BeanFactory beanFactory;
	private final String name;

	public Controller(String name, Class<?> controllerClass,
			BeanFactory beanFactory) {
		this.name = name;
		this.controllerClass = controllerClass;
		this.beanFactory = beanFactory;
		this.actionMap = new HashMap<String, Action>();
		Method[] methods = this.controllerClass.getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.endsWith("Action")) {
				String actionName = methodName.substring(0,
						methodName.length() - 6);
				if (actionMap.containsKey(actionName)) {
					if (logger.isWarnEnabled()) {
						logger.warn(
								"action already exist:{} of controller: {}",
								actionName, controllerClass);
					}
					continue;
				}
				actionMap.put(actionName, new Action(method, this));
			}
		}
	}

	public Action getAction(String actionName) {
		return actionMap.get(actionName);
	}

	public Object newInstance() throws Exception {
		return beanFactory.getBean(this.controllerClass);
	}

	public Annotation[] getAnnotations() {
		return this.controllerClass.getAnnotations();
	}

	public Class<?> getControllerClass() {
		return controllerClass;
	}

	public String getName() {
		return name;
	}

}
