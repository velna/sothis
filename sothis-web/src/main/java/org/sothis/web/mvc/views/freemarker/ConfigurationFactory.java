package org.sothis.web.mvc.views.freemarker;

import org.sothis.mvc.ConfigurationException;
import org.sothis.web.mvc.WebActionContext;

import freemarker.template.Configuration;

public interface ConfigurationFactory {
	Configuration createConfiguration(WebActionContext actionContext) throws ConfigurationException;
}
