package org.sothis.mvc.http.views.freemarker;

import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ConfigurationException;

import freemarker.template.Configuration;

public interface ConfigurationFactory {
	Configuration createConfiguration(ActionContext actionContext) throws ConfigurationException;
}