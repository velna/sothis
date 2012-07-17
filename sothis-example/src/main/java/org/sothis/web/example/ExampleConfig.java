package org.sothis.web.example;

import java.io.IOException;
import java.net.URISyntaxException;

import org.sothis.core.config.ConfigurationSupport;

public class ExampleConfig extends ConfigurationSupport {

	private final static ExampleConfig CONFIG;
	static {
		try {
			CONFIG = new ExampleConfig();
		} catch (IOException e) {
			throw new RuntimeException("error init config: ", e);
		} catch (URISyntaxException e) {
			throw new RuntimeException("error init config: ", e);
		}
	}

	private ExampleConfig() throws IOException, URISyntaxException {
		super(System.getProperty("sothis.example.overridePropertiesLocation", "D:/goojiaconfig/goojia.ini"));
	}

	public static ExampleConfig getConfig() {
		return CONFIG;
	}
}
