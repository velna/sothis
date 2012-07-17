package org.sothis.web.mvc.views.freemarker;

import org.sothis.web.mvc.ActionContext;
import org.sothis.web.mvc.ConfigurationException;

import freemarker.template.Configuration;

public interface ConfigurationFactory {
	Configuration createConfiguration(ActionContext actionContext) throws ConfigurationException;
}
