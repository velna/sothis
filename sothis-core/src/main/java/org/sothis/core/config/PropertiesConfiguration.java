package org.sothis.core.config;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 属性配置读取工具类
 * </p>
 */
public class PropertiesConfiguration implements PropertiesBean {

	private final Properties properties;

	public PropertiesConfiguration(Properties properties) {
		this.properties = properties;
	}

	/**
	 * <p>
	 * 根据提供的正则表达式pattern，到properties配置文件中取出指定类型valueClass的已配置信息
	 * </p>
	 * 
	 * @param <T>
	 * @param pattern
	 * @param valueClass
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> getAsGroup(Pattern pattern, Class<T> valueClass) throws ClassNotFoundException {
		Map<String, T> ret = new HashMap<String, T>();
		for (String key : this.properties.stringPropertyNames()) {
			Matcher matcher = pattern.matcher(key);
			if (matcher.matches()) {
				Object value = null;
				if (valueClass == String.class) {
					value = get(key);
				} else if (valueClass == Boolean.class) {
					value = getBoolean(key);
				} else if (valueClass == Integer.class) {
					value = getInteger(key);
				} else if (valueClass == Long.class) {
					value = getLong(key);
				} else if (valueClass == Float.class) {
					value = getFloat(key);
				} else if (valueClass == Double.class) {
					value = getDouble(key);
				} else if (valueClass == File.class) {
					value = getFile(key);
				} else if (valueClass == URL.class) {
					value = getUrl(key);
				} else if (valueClass == Class.class) {
					value = getClass(key);
				} else {
					throw new IllegalArgumentException("value class of " + valueClass + " is not supported.");
				}
				ret.put(matcher.group(1), (T) value);
			}
		}
		return ret;
	}

	public String get(String key) {
		return get(key, null);
	}

	public String get(String key, String defaultValue) {
		String value = this.properties.getProperty(key);
		if (null == value) {
			return defaultValue;
		}
		return value;
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		String value = this.properties.getProperty(key);
		if (null == value) {
			return defaultValue;
		}
		return Boolean.parseBoolean(value);
	}

	public Boolean getBoolean(String key) {
		return getBoolean(key, null);
	}

	public Integer getInteger(String key, Integer defaultValue) {
		String value = this.properties.getProperty(key);
		if (null == value) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public Integer getInteger(String key) {
		return getInteger(key, null);
	}

	public Long getLong(String key, Long defaultValue) {
		String value = this.properties.getProperty(key);
		if (null == value) {
			return defaultValue;
		}
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public Long getLong(String key) {
		return getLong(key, null);
	}

	public Float getFloat(String key, Float defaultValue) {
		String value = this.properties.getProperty(key);
		if (null == value) {
			return defaultValue;
		}
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public Float getFloat(String key) {
		return getFloat(key, null);
	}

	public Double getDouble(String key, Double defaultValue) {
		String value = this.properties.getProperty(key);
		if (null == value) {
			return defaultValue;
		}
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public Double getDouble(String key) {
		return getDouble(key, null);
	}

	public URL getUrl(String key, URL defaultValue) {
		String value = this.properties.getProperty(key);
		if (null == value) {
			return defaultValue;
		}
		try {
			return new URL(value);
		} catch (MalformedURLException e) {
			return defaultValue;
		}
	}

	public URL getUrl(String key) {
		return getUrl(key, null);
	}

	public File getFile(String key, File defaultValue) {
		String value = this.properties.getProperty(key);
		if (null == value) {
			return defaultValue;
		}
		return new File(value);
	}

	public File getFile(String key) {
		return getFile(key, null);
	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends T> getClass(String key, Class<? extends T> defaultValue) throws ClassNotFoundException {
		String value = this.properties.getProperty(key);
		if (null == value) {
			return defaultValue;
		}
		return (Class<T>) Class.forName(value);
	}

	public <T> Class<? extends T> getClass(String key) throws ClassNotFoundException {
		return getClass(key, null);
	}

	public Properties getProperties() {
		return properties;
	}

}
