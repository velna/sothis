package org.sothis.mvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.sothis.core.beans.BeanFactory;
import org.sothis.core.beans.BeanInstantiationException;
import org.sothis.core.util.ClassUtils;
import org.sothis.core.util.StringUtils;

public class DefaultApplicationContext implements ApplicationContext {
	private final Map<Object, Action> actions = new HashMap<Object, Action>();
	private final BeanFactory beanFactory;
	private final Configuration configuration;

	public DefaultApplicationContext(BeanFactory beanFactory, Configuration configuration) throws BeanInstantiationException,
			ClassNotFoundException, IOException, ConfigurationException {
		this.beanFactory = beanFactory;
		this.configuration = configuration;
		regBeans();
		initActions();
	}

	private void regBean(Class<?> c) {
		this.beanFactory.registerBean(c.getName(), c);
	}

	private void regBeans() {
		regBean(this.configuration.getActionMapper());
		regBean(this.configuration.getModelAndViewResolver());

		Map<String, Class<View>> views = this.configuration.getViews();
		for (Map.Entry<String, Class<View>> entry : views.entrySet()) {
			regBean(entry.getValue());
		}

		Map<String, Class<Interceptor>> interceptors = this.configuration.getInterceptors();
		for (Map.Entry<String, Class<Interceptor>> entry : interceptors.entrySet()) {
			regBean(entry.getValue());
		}
	}

	private void initActions() throws BeanInstantiationException, ClassNotFoundException, IOException, ConfigurationException {
		final String[] packageNames = configuration.getControllerPackages();
		ActionMapper actionMapper = beanFactory.getBean(configuration.getActionMapper());
		for (String packageName : packageNames) {
			if (StringUtils.isEmpty(packageName)) {
				continue;
			}
			final Class<?>[] classes = ClassUtils.getClasses(packageName);
			for (Class<?> c : classes) {
				if (c.isLocalClass() || c.isMemberClass() || c.isAnonymousClass() || c.isAnnotationPresent(Ignore.class)
						|| c.getPackage().isAnnotationPresent(Ignore.class)) {
					continue;
				}
				final String className = c.getName().substring(packageName.length() + 1);
				String subPackageName;
				int dotIndex = className.lastIndexOf('.');
				if (dotIndex > 0) {
					subPackageName = className.substring(0, dotIndex).replaceAll("\\.", "/");
				} else {
					subPackageName = "";
				}
				final String simpleName = c.getSimpleName();
				dotIndex = simpleName.indexOf(Controller.CONTROLLER_SUFFIX);
				String name;
				if (dotIndex > 0) {
					name = StringUtils.uncapitalize(simpleName.substring(0, dotIndex));
				} else if (dotIndex == 0) {
					name = "";
				} else {
					continue;
				}
				Controller controller = new DefaultController(this.configuration, subPackageName, name, c);
				beanFactory.registerBean(c.getName(), c);
				Map<String, Action> controllerActions = controller.getActions();
				for (String actionName : controllerActions.keySet()) {
					Action action = controllerActions.get(actionName);
					Object actionKey = actionMapper.map(this, action);
					if (actions.containsKey(actionKey)) {
						throw new ConfigurationException("duplicated action key:" + actionKey + ", which already registered as "
								+ actions.get(actionKey));
					}
					actions.put(actionKey, action);
				}
				if (configuration.isInitializeControllerOnStartup()) {
					this.beanFactory.getBean(c);
				}
			}
		}
	}

	@Override
	public boolean containsAction(Object key) {
		return this.actions.containsKey(key);
	}

	@Override
	public Action getAction(Object key) {
		return this.actions.get(key);
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

	@Override
	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

}
