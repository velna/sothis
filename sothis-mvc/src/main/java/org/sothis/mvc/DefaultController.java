package org.sothis.mvc;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sothis.mvc.annotation.Ignore;

/**
 * Controller接口的默认实现
 * 
 * @author velna
 * 
 */
public class DefaultController extends AbstractActionBase implements Controller {

	private final static Logger LOGGER = LoggerFactory.getLogger(DefaultController.class);

	private final Class<?> controllerClass;
	private final Map<String, Action> actionMap;
	private final String name;
	private final String packageName;
	private final String fullName;

	public DefaultController(Configuration config, final String packageName, final String name, final Class<?> controllerClass) {
		super(config, controllerClass, controllerClass.getPackage());
		this.packageName = packageName;
		this.name = name;

		StringBuilder ret = new StringBuilder();
		ret.append('/');
		if (StringUtils.isNotEmpty(packageName)) {
			ret.append(this.packageName).append('/');
		}
		if (StringUtils.isNotEmpty(name)) {
			ret.append(this.name).append('/');
		}
		this.fullName = ret.toString();

		this.controllerClass = controllerClass;
		this.actionMap = new HashMap<String, Action>();
		final Method[] methods = this.controllerClass.getMethods();
		for (Method method : methods) {
			final String methodName = method.getName();
			if (!methodName.endsWith(Action.ACTION_SUFFIX) || method.isAnnotationPresent(Ignore.class)) {
				continue;
			}
			final String actionName = methodName.substring(0, methodName.length() - Action.ACTION_SUFFIX.length());
			if (actionMap.containsKey(actionName)) {
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn("action already exist:{} of controller: {}", actionName, controllerClass);
				}
				continue;
			}
			actionMap.put(actionName, new DefaultAction(config, method, this));
		}
	}

	public Action getAction(final String actionName) {
		Action action = actionMap.get(actionName);
		return action;
	}

	public Class<?> getControllerClass() {
		return controllerClass;
	}

	public String getName() {
		return name;
	}

	public String getFullName() {
		return fullName;
	}

	public Map<String, Action> getActions() {
		return Collections.unmodifiableMap(actionMap);
	}

	public String getPackageName() {
		return packageName;
	}

	@Override
	public String toString() {
		return getFullName();
	}

}
