package org.sothis.web.mvc.support;

import java.io.IOException;
import java.util.Properties;

import org.sothis.core.config.PropertiesBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.CollectionUtils;

public class SpringPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements PropertiesBean {

	private Properties properties;
	private PropertiesBean propertiesBean;

	@Override
	protected Properties mergeProperties() throws IOException {
		properties = super.mergeProperties();
		CollectionUtils.mergePropertiesIntoMap(propertiesBean.getProperties(), properties);
		return properties;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setPropertiesBean(PropertiesBean propertiesBean) {
		this.propertiesBean = propertiesBean;
	}

}
