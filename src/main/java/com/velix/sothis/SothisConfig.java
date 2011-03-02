package com.velix.sothis;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.velix.sothis.interceptor.Interceptor;
import com.velix.sothis.view.DefaultModelAndViewResolver;
import com.velix.sothis.view.ModelAndViewResolver;

public class SothisConfig {

	public static final String DEFAULT_STACK_NAME = "default";

	private static SothisConfig config;

	private Map<String, Class<? extends Interceptor>> interceptorMap;
	private Map<String, List<Class<? extends Interceptor>>> interceptorStackMap;
	private BeanFactory beanFactory;
	private String[] controllerPackages;
	private Class<? extends ModelAndViewResolver> viewResolverClass;

	private Properties properties = new Properties();

	@SuppressWarnings("unchecked")
	private SothisConfig() throws Exception {
		InputStream input = SothisConfig.class.getClassLoader()
				.getResourceAsStream("sothis.properties");
		if (null == input) {
			throw new RuntimeException(
					"file sothis.properties can not be found!");
		}
		properties.load(input);

		// sothis.interceptor.*.class
		interceptorMap = findInterceptors();

		// sothis.interceptor.stack.*.class
		interceptorStackMap = findInterceptorStacks(interceptorMap);

		// sothis.beanFactory
		String beanFactroyClassName = getProperty("sothis.beanFactory.class");
		if (null != beanFactroyClassName) {
			Class<?> beanFactoryClass = Class.forName(beanFactroyClassName);
			beanFactory = (BeanFactory) beanFactoryClass.newInstance();
		} else {
			beanFactory = new SimpleBeanFactory();
		}

		// sothis.controller.packages
		String property = getProperty("sothis.controller.packages");
		if (null != property) {
			controllerPackages = property.split(",");
		} else {
			controllerPackages = new String[0];
		}

		property = getProperty("sothis.viewResolver.class");
		if (null != property) {
			viewResolverClass = (Class<? extends ModelAndViewResolver>) Class
					.forName(property);
		} else {
			viewResolverClass = DefaultModelAndViewResolver.class;
		}
	}

	private Map<String, Class<? extends Interceptor>> findInterceptors()
			throws Exception {
		Map<String, String> pMap = getPropertyByPattern(Pattern
				.compile("sothis\\.interceptor\\.(\\w+)\\.class"));
		return asClassMap(pMap, Interceptor.class);
	}

	@SuppressWarnings("unchecked")
	private <T> Map<String, Class<? extends T>> asClassMap(
			Map<String, String> map, Class<T> beanClass)
			throws ClassNotFoundException {
		Map<String, Class<? extends T>> ret = new HashMap<String, Class<? extends T>>(
				map.size());
		for (String key : map.keySet()) {
			ret.put(key, (Class<? extends T>) Class.forName(map.get(key)));
		}
		return ret;
	}

	private Map<String, String> getPropertyByPattern(Pattern p)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		for (String name : properties.stringPropertyNames()) {
			Matcher m = p.matcher(name);
			if (m.matches()) {
				map.put(m.group(1), this.properties.getProperty(name));
			}
		}
		return map;
	}

	private Map<String, List<Class<? extends Interceptor>>> findInterceptorStacks(
			Map<String, Class<? extends Interceptor>> interceptorMap)
			throws Exception {
		Map<String, List<Class<? extends Interceptor>>> map = new HashMap<String, List<Class<? extends Interceptor>>>();
		Map<String, String> stackMap = getPropertyByPattern(Pattern
				.compile("sothis\\.interceptor\\.stack\\.(\\w+)"));
		for (String key : stackMap.keySet()) {
			String[] names = stackMap.get(key).split(",");
			List<Class<? extends Interceptor>> stack = new ArrayList<Class<? extends Interceptor>>(
					names.length);
			for (String n : names) {
				if (interceptorMap.containsKey(n)) {
					stack.add(interceptorMap.get(n));
				}
			}
			map.put(key, stack);
		}
		return map;
	}

	public static SothisConfig getConfig() throws Exception {
		if (null == config) {
			synchronized (SothisConfig.class) {
				if (null == config) {
					config = new SothisConfig();
				}
			}
		}
		return config;
	}

	private String getProperty(String name) {
		return properties.getProperty(name);
	}

	// private String getProperty(String name, String defaultValue) {
	// String property = properties.getProperty(name);
	// return null == property ? defaultValue : property;
	// }

	public BeanFactory getBeanFactory() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		return beanFactory;
	}

	public String[] getControllerPackages() {
		return controllerPackages;
	}

	public List<Class<? extends Interceptor>> getInterceptorStackClasses(
			String stackName) {
		return this.interceptorStackMap.get(stackName);
	}

	public List<Class<? extends Interceptor>> getDefaultInterceptorStackClasses() {
		return this.interceptorStackMap.get(DEFAULT_STACK_NAME);
	}

	public Class<?> getInterceptor(String name) {
		return this.interceptorMap.get(name);
	}

	public Class<? extends ModelAndViewResolver> getViewResolverClass() {
		return viewResolverClass;
	}
}
