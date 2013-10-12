package org.sothis.web.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.sothis.mvc.Configuration;
import org.sothis.mvc.ConfigurationException;

public class WebConfiguration extends Configuration {

	private final Class<? extends Flash> flash;
	private final String characterEncoding;

	private WebConfiguration(Properties properties) throws ConfigurationException {
		super(properties);
		try {
			characterEncoding = this.get("sothis.http.characterEncoding", "UTF-8");
			flash = this.getClass("sothis.flash.class", DefaultFlash.class);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException(e);
		}
	}

	public static WebConfiguration create(Properties properties) throws ConfigurationException {
		return new WebConfiguration(properties);
	}

	public static WebConfiguration create(String propertyFile) throws ConfigurationException, IOException {
		InputStream input = WebConfiguration.class.getClassLoader().getResourceAsStream(propertyFile);
		if (null == input) {
			throw new ConfigurationException("can not find property file " + propertyFile + " in classpath ");
		}
		Properties properties = new Properties();
		properties.load(new InputStreamReader(input, "UTF-8"));
		input.close();
		return new WebConfiguration(properties);
	}

	public Class<? extends Flash> getFlash() {
		return flash;
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

}
