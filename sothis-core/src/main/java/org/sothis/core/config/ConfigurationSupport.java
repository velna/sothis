package org.sothis.core.config;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sothis.core.util.CollectionUtils;
import org.sothis.core.util.StringUtils;
import org.springframework.util.ClassUtils;

public class ConfigurationSupport extends PropertiesConfiguration {

	private final static String CLASSPATH_PREFIX = "classpath:";
	private final static String IMPORT_KEY = "import";

	protected final static Logger LOGGER = LoggerFactory.getLogger(ConfigurationSupport.class);

	protected ConfigurationSupport(String overridePropertiesLocation) throws IOException, URISyntaxException {
		super(mergeProperties(overridePropertiesLocation));
	}

	/**
	 * 子类可以重写这个方法，如果需要做一些自定义的操作
	 * 
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	protected static Properties mergeProperties(String overridePropertiesLocation) throws IOException, URISyntaxException {
		Properties overrideProperties = getOverrideProperties(overridePropertiesLocation);
		Properties classpathProperties = getClasspathProperties();

		Properties allProperties = loadImportedProperties(new Properties(), overrideProperties.getProperty(IMPORT_KEY));
		allProperties = loadImportedProperties(allProperties, classpathProperties.getProperty(IMPORT_KEY));

		CollectionUtils.mergePropertiesIntoMap(classpathProperties, allProperties);
		CollectionUtils.mergePropertiesIntoMap(overrideProperties, allProperties);

		allProperties = resolvePlaceHolders(allProperties);
		return allProperties;
	}

	protected static Properties resolvePlaceHolders(Properties properties) {
		PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}", ":", false);
		for (String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			String newValue = helper.replacePlaceholders(value, properties);
			properties.setProperty(key, newValue);
		}
		return properties;
	}

	protected static Properties getOverrideProperties(String overridePropertiesLocation) throws IOException {
		if (StringUtils.isEmpty(overridePropertiesLocation)) {
			throw new IllegalArgumentException("overridePropertiesLocation can not be null");
		}
		File file = new File(overridePropertiesLocation);
		return loadProperties(file);
	}

	protected static Properties getClasspathProperties() throws IOException, URISyntaxException {
		Properties allProperties = new Properties();
		URL url = ClassUtils.getDefaultClassLoader().getResource("");
		if (null == url) {
			return allProperties;
		}
		File rootFolder = new File(url.toURI());
		File[] propertiesFiles = rootFolder.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				if (null == pathname) {
					return false;
				}
				if (pathname.isDirectory()) {
					return false;
				}
				return pathname.getName().endsWith(".properties");
			}
		});
		for (File pFile : propertiesFiles) {
			Properties properties = loadProperties(pFile);
			CollectionUtils.mergePropertiesIntoMap(properties, allProperties);
		}
		return allProperties;
	}

	private static Properties loadImportedProperties(Properties allProperties, String imports) throws IOException,
			URISyntaxException {
		if (StringUtils.isEmpty(imports)) {
			return allProperties;
		}
		String[] files = imports.split("[, ]");
		Properties properties = new Properties();
		for (String f : files) {
			InputStream input = null;
			if (f.startsWith(CLASSPATH_PREFIX)) {
				f = f.substring(CLASSPATH_PREFIX.length());
				input = ConfigurationSupport.class.getClassLoader().getResourceAsStream(f.trim());
			} else {
				input = new FileInputStream(f);
			}
			if (null != input) {
				LOGGER.info("load imported properties from {}", f);
				properties.load(input);
			} else {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("can not load imported config file: {}", f);
				}
			}
		}

		CollectionUtils.mergePropertiesIntoMap(allProperties, properties);
		return properties;
	}

	private static Properties loadProperties(File file) throws IOException {
		Properties properties = new Properties();

		if (!file.exists() || file.isDirectory()) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("properties file is not exit or is a directory: {}", file);
			}
		} else {
			if (file.getName().endsWith(".xml")) {
				properties.loadFromXML(new FileInputStream(file));
			} else {
				properties.load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			}
			LOGGER.info("load properties from file {}", file);
		}
		return properties;
	}

}
