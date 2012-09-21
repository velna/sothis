package org.sothis.web.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public final class SothisConfig extends PropertiesConfiguration {

	public static final String DEFAULT_STACK_NAME = "default";

	private static SothisConfig config;

	private final Map<String, Class<Interceptor>> interceptors;
	private final Map<String, InterceptorStack> interceptorStacks;

	private final Class<? extends ActionMapper> actionMapper;
	private final Class<? extends ModelAndViewResolver> modelAndViewResolver;
	private final Class<? extends ExceptionHandler> exceptionHandler;
	private final Class<? extends Flash> flash;

	private final Map<String, Class<View>> views;
	private final Class<View> defaultView;

	private final Class<BeanFactory> beanFactoryClass;
	private final String[] controllerPackages;
	private final String characterEncoding;
	private final boolean initializeControllerOnStartup;

	@SuppressWarnings({ "unchecked" })
	private SothisConfig(final Properties properties) throws ConfigurationException {
		super(properties);
		try {
			interceptors = (Map) this.getAsGroup(Pattern.compile("sothis\\.interceptors\\.(\\w+)\\.class"), Class.class);
			modelAndViewResolver = this.getClass("sothis.viewResolver.class", DefaultModelAndViewResolver.class);
			actionMapper = this.getClass("sothis.actionMapper.class", DefaultActionMapper.class);
			flash = this.getClass("sothis.flash.class", DefaultFlash.class);
			exceptionHandler = (Class<? extends ExceptionHandler>) this.getClass("sothis.exception.handler.class");

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

			characterEncoding = this.get("sothis.http.characterEncoding", "UTF-8");
			initializeControllerOnStartup = this.getBoolean("sothis.controller.initializeOnStartup", true);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException(e);
		}
	}

	private Map<String, InterceptorStack> findInterceptorStacks(Map<String, Class<Interceptor>> interceptorMap) throws ClassNotFoundException {
		Map<String, InterceptorStack> map = new HashMap<String, InterceptorStack>();
		Map<String, String> stackMap = getAsGroup(Pattern.compile("sothis\\.interceptors\\.stack\\.(\\w+)"), String.class);
		for (String key : stackMap.keySet()) {
			String[] names = stackMap.get(key).split(",");
			List<Class<Interceptor>> stackInterceptors = new ArrayList<Class<Interceptor>>(names.length);
			for (String n : names) {
				if (interceptorMap.containsKey(n)) {
					stackInterceptors.add(interceptorMap.get(n));
				}
			}
			map.put(key, new DefaultInterceptorStack(stackInterceptors));
		}
		return map;
	}

	public synchronized static SothisConfig initConfig(String configLocation) throws ConfigurationException, IOException {
		if (null == config) {
			InputStream input = SothisConfig.class.getClassLoader().getResourceAsStream(configLocation);
			if (null == input) {
				input = SothisConfig.class.getClassLoader().getResourceAsStream("sothis.default.properties");
			}
			Properties properties = new Properties();
			properties.load(new InputStreamReader(input, "UTF-8"));
			config = new SothisConfig(properties);
		}
		return config;
	}

	public synchronized static SothisConfig initConfig(Properties properties) throws ConfigurationException {
		if (null == config) {
			config = new SothisConfig(properties);
		}
		return config;
	}

	public static SothisConfig getConfig() {
		return config;
	}

	public Class<BeanFactory> getBeanFactoryClass() {
		return beanFactoryClass;
	}

	public String[] getControllerPackages() {
		return Arrays.copyOf(controllerPackages, controllerPackages.length);
	}

	public InterceptorStack getInterceptorStack(String stackName) {
		return this.interceptorStacks.get(stackName);
	}

	public InterceptorStack getDefaultInterceptorStack() {
		return this.interceptorStacks.get(DEFAULT_STACK_NAME);
	}

	public Class<Interceptor> getInterceptor(String name) {
		return this.interceptors.get(name);
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public boolean isInitializeControllerOnStartup() {
		return initializeControllerOnStartup;
	}

	public Map<String, Class<View>> getViews() {
		return Collections.unmodifiableMap(views);
	}

	public Class<View> getDefaultView() {
		return defaultView;
	}

	public Class<? extends ActionMapper> getActionMapper() {
		return actionMapper;
	}

	public Class<? extends ModelAndViewResolver> getModelAndViewResolver() {
		return modelAndViewResolver;
	}

	public Class<? extends ExceptionHandler> getExceptionHandler() {
		return exceptionHandler;
	}

	public Class<? extends Flash> getFlash() {
		return flash;
	}

}
