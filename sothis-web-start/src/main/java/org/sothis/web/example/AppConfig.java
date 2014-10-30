package org.sothis.web.example;

import java.io.IOException;
import java.net.URISyntaxException;

import org.sothis.core.config.ConfigurationSupport;

public class AppConfig extends ConfigurationSupport {

	private final static AppConfig CONFIG;
	static {
		try {
			CONFIG = new AppConfig();
		} catch (IOException e) {
			throw new RuntimeException("error init config: ", e);
		} catch (URISyntaxException e) {
			throw new RuntimeException("error init config: ", e);
		}
	}

	private AppConfig() throws IOException, URISyntaxException {
		super(System.getProperty("sothis.overridePropertiesLocation", "/etc/sothis-app.properties"));
	}

	public static AppConfig getConfig() {
		return CONFIG;
	}
}
