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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sothis.core.util.CollectionUtils;

public class ConfigurationSupport extends PropertiesConfiguration {

	private final static String CLASSPATH_PREFIX = "classpath:";

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
		Properties allProperties = getClasspathProperties();
		Properties overrideProperties = getOverrideProperties(overridePropertiesLocation);
		CollectionUtils.mergePropertiesIntoMap(overrideProperties, allProperties);
		loadImportedProperties(allProperties);
		allProperties = resolvePlaceHolders(allProperties);
		return allProperties;
	}

	private static void loadImportedProperties(Properties allProperties) throws IOException {
		String imports = allProperties.getProperty("import");
		if (null == imports) {
			return;
		}
		String[] files = imports.split(",");
		for (String f : files) {
			InputStream input;
			if (f.startsWith(CLASSPATH_PREFIX)) {
				f = f.substring(CLASSPATH_PREFIX.length());
				input = ConfigurationSupport.class.getClassLoader().getResourceAsStream(f.trim());
			} else {
				input = new FileInputStream(f);
			}
			if (null != input) {
				Properties properties = new Properties();
				properties.load(new InputStreamReader(input, "UTF-8"));
				CollectionUtils.mergePropertiesIntoMap(properties, allProperties);
			} else {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("can not load imported config file: {}", f);
				}
			}
		}
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
		Properties properties = new Properties();
		if (StringUtils.isEmpty(overridePropertiesLocation)) {
			return properties;
		}
		File file = new File(overridePropertiesLocation);
		if (!file.exists() || file.isDirectory()) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("overried properties file is not exit or is a directory: {}", overridePropertiesLocation);
			}
			return properties;
		}
		if (overridePropertiesLocation.endsWith(".xml")) {
			properties.loadFromXML(new FileInputStream(overridePropertiesLocation));
		} else {
			properties.load(new InputStreamReader(new FileInputStream(overridePropertiesLocation), "UTF-8"));
		}
		return properties;
	}

	protected static Properties getClasspathProperties() throws IOException, URISyntaxException {
		Properties allProperties = new Properties();
		URL url = ConfigurationSupport.class.getClassLoader().getResource("");
		if (null == url) {
			return allProperties;
		}
		File rootFolder = new File(url.toURI());
		LOGGER.info("load property files from {}", rootFolder.getAbsolutePath());
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
		if (null != propertiesFiles) {
			for (File pFile : propertiesFiles) {
				LOGGER.info("load properties from {}", pFile.getAbsolutePath());
				Properties properties = new Properties();
				properties.load(new InputStreamReader(new FileInputStream(pFile), "UTF-8"));
				CollectionUtils.mergePropertiesIntoMap(properties, allProperties);
			}
		}
		return allProperties;
	}

}
