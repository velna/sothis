package org.sothis.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.sothis.core.beans.BeanFactory;
import org.sothis.core.config.PropertiesConfiguration;

public class Configuration extends PropertiesConfiguration {

	public static final String DEFAULT_STACK_NAME = "default";

	private final Map<String, Class<Interceptor>> interceptors;
	private final Map<String, List<Class<Interceptor>>> interceptorStacks;

	private final Class<? extends ActionMapper> actionMapper;
	private final Class<? extends ModelAndViewResolver> modelAndViewResolver;

	private final Map<String, Class<View>> views;
	private final Class<View> defaultView;

	private final Class<BeanFactory> beanFactoryClass;
	private final String[] controllerPackages;
	private final boolean initializeControllerOnStartup;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Configuration(final Properties properties) throws ConfigurationException {
		super(properties);
		try {
			interceptors = (Map) this.getAsGroup(Pattern.compile("sothis\\.interceptors\\.(\\w+)\\.class"), Class.class);
			modelAndViewResolver = this.getClass("sothis.viewResolver.class", DefaultModelAndViewResolver.class);
			actionMapper = this.getClass("sothis.actionMapper.class");

			views = (Map) this.getAsGroup(Pattern.compile("sothis\\.views\\.(\\w+)\\.class"), Class.class);
			defaultView = views.get(get("sothis.views.default", "jsp"));

			// sothis.interceptor.stack.*.class
			interceptorStacks = findInterceptorStacks(interceptors);

			beanFactoryClass = (Class<BeanFactory>) getClass("sothis.beanFactory.class");

			String property = get("sothis.controller.packages");
			if (null != property) {
				controllerPackages = property.split(",");
			} else {
				controllerPackages = new String[0];
			}

			initializeControllerOnStartup = this.getBoolean("sothis.controller.initializeOnStartup", true);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException(e);
		}
	}

	private Map<String, List<Class<Interceptor>>> findInterceptorStacks(Map<String, Class<Interceptor>> interceptorMap) throws ClassNotFoundException {
		Map<String, List<Class<Interceptor>>> map = new HashMap<String, List<Class<Interceptor>>>();
		Map<String, String> stackMap = getAsGroup(Pattern.compile("sothis\\.interceptors\\.stack\\.(\\w+)"), String.class);
		for (String key : stackMap.keySet()) {
			String[] names = stackMap.get(key).split(",");
			List<Class<Interceptor>> stackInterceptors = new ArrayList<Class<Interceptor>>(names.length);
			for (String n : names) {
				if (interceptorMap.containsKey(n)) {
					stackInterceptors.add(interceptorMap.get(n));
				}
			}
			map.put(key, stackInterceptors);
		}
		return map;
	}

	public Class<BeanFactory> getBeanFactoryClass() {
		return beanFactoryClass;
	}

	public String[] getControllerPackages() {
		return Arrays.copyOf(controllerPackages, controllerPackages.length);
	}

	public List<Class<Interceptor>> getInterceptorStack(String name) {
		return this.interceptorStacks.get(name);
	}

	public List<Class<Interceptor>> getDefaultInterceptorStack() {
		List<Class<Interceptor>> ret = this.interceptorStacks.get(DEFAULT_STACK_NAME);
		if (null == ret) {
			return Collections.emptyList();
		} else {
			return ret;
		}
	}

	public Class<Interceptor> getInterceptor(String name) {
		return this.interceptors.get(name);
	}

	public Class<View> getView(String name) {
		return this.views.get(name);
	}

	public Class<View> getDefaultView() {
		return defaultView;
	}

	public Class<? extends ModelAndViewResolver> getModelAndViewResolver() {
		return modelAndViewResolver;
	}

	public Class<? extends ActionMapper> getActionMapper() {
		return actionMapper;
	}

	public boolean isInitializeControllerOnStartup() {
		return initializeControllerOnStartup;
	}

	public Map<String, Class<Interceptor>> getInterceptors() {
		return interceptors;
	}

	public Map<String, Class<View>> getViews() {
		return views;
	}

}
