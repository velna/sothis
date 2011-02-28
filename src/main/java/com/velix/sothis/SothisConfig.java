package com.velix.sothis;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SothisConfig {

	private static SothisConfig config;

	private Map<String, Class<?>> interceptorMap;
	private Map<String, List<Class<?>>> interceptorStackMap;
	private BeanFactory beanFactory;

	private Properties properties = new Properties();

	private SothisConfig() throws Exception {
		InputStream input = SothisConfig.class.getClassLoader()
				.getResourceAsStream("sothis.properties");
		if (null != input) {
			properties.load(input);
			interceptorMap = findInterceptors();
			interceptorStackMap = findInterceptorStacks(interceptorMap);
			String beanFactroyClassName = getProperty("sothis.beanFactory");
			if (null != beanFactroyClassName) {
				Class<?> beanFactoryClass = Class.forName(beanFactroyClassName);
				beanFactory = (BeanFactory) beanFactoryClass.newInstance();
			} else {
				beanFactory = new SimpleBeanFactory();
			}
		}
	}

	private Map<String, Class<?>> findInterceptors() throws Exception {
		Map<String, String> pMap = getPropertyByPattern(Pattern
				.compile("sothis\\.interceptor\\.(\\w+)\\.class"));
		return asClassMap(pMap);
	}

	private Map<String, Class<?>> asClassMap(Map<String, String> map)
			throws ClassNotFoundException {
		Map<String, Class<?>> ret = new HashMap<String, Class<?>>(map.size());
		for (String key : map.keySet()) {
			ret.put(key, Class.forName(map.get(key)));
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

	private Map<String, List<Class<?>>> findInterceptorStacks(
			Map<String, Class<?>> interceptorMap) throws Exception {
		Map<String, List<Class<?>>> map = new HashMap<String, List<Class<?>>>();
		Map<String, String> stackMap = getPropertyByPattern(Pattern
				.compile("sothis\\.interceptor\\.stack\\.(\\w)"));
		for (String key : stackMap.keySet()) {
			String[] names = stackMap.get(key).split(",");
			List<Class<?>> stack = new ArrayList<Class<?>>(names.length);
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

	private String getProperty(String name, String defaultValue) {
		String property = properties.getProperty(name);
		return null == property ? defaultValue : property;
	}

	public BeanFactory getBeanFactory() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		return beanFactory;
	}

	public String[] getControllerPackages() {
		String property = getProperty("sothis.controller.packages");
		if (null != property) {
			return property.split(",");
		} else {
			return new String[0];
		}
	}

	public List<Class<?>> getInterceptorStack(String stackName) {
		return this.interceptorStackMap.get(stackName);
	}

	public Class<?> getInterceptor(String name) {
		return this.interceptorMap.get(name);
	}
}
