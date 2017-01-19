package org.sothis.webstart;

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

	private final boolean authEnabled;
	private final int pageSize;

	private AppConfig() throws IOException, URISyntaxException {
		super(System.getProperty("sothis.overridePropertiesLocation", "/etc/sitex.properties"));

		authEnabled = this.getBoolean("auth.enable", true);
		pageSize = this.getInteger("pageSize", 10);
	}

	public static AppConfig getConfig() {
		return CONFIG;
	}

	public boolean isAuthEnabled() {
		return authEnabled;
	}

	public int getPageSize() {
		return pageSize;
	}

}
