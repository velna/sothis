package org.sothis.mvc.http.views.freemarker;

import java.util.Map;
import java.util.regex.Pattern;

import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ConfigurationException;

import freemarker.cache.TemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateHashModel;

public class DefaultConfigurationFactory implements ConfigurationFactory {

	public Configuration createConfiguration(ActionContext actionContext) throws ConfigurationException {
		try {
			org.sothis.mvc.Configuration config = actionContext.getApplicationContext().getConfiguration();

			Configuration configuration = new Configuration();
			Map<String, String> configMap = config.getAsGroup(Pattern.compile("freemarker\\.settings\\.(.*)"), String.class);
			for (Map.Entry<String, String> entry : configMap.entrySet()) {
				configuration.setSetting(entry.getKey(), entry.getValue());
			}
			if (configuration.getObjectWrapper() instanceof BeansWrapper) {
				((BeansWrapper) configuration.getObjectWrapper()).setExposureLevel(BeansWrapper.EXPOSE_PROPERTIES_ONLY);
			}

			BeansWrapper wrapper = BeansWrapper.getDefaultInstance();

			configuration.setSharedVariable("enums", wrapper.getEnumModels());
			// shared directives
			Map<String, Class<?>> sharedDirectiveVariables = (Map) config.getAsGroup(
					Pattern.compile("freemarker\\.directive\\.(\\w+)\\.class"), Class.class);
			for (String key : sharedDirectiveVariables.keySet()) {
				configuration.setSharedVariable(key, sharedDirectiveVariables.get(key).newInstance());
			}

			// shared statics
			Map<String, String> sharedStaticVariables = (Map) config.getAsGroup(
					Pattern.compile("freemarker\\.static\\.(\\w+)\\.class"), String.class);
			TemplateHashModel staticModels = wrapper.getStaticModels();
			for (String key : sharedStaticVariables.keySet()) {
				configuration.setSharedVariable(key, staticModels.get(sharedStaticVariables.get(key)));
			}
			configuration.setTemplateLoader(buildTemplateLoader(actionContext));
			return configuration;
		} catch (Exception e) {
			throw new ConfigurationException("error create freemarker configuration: ", e);
		}
	}

	protected TemplateLoader buildTemplateLoader(ActionContext actionContext) {
		return new ClassPathTemplateLoader();
	}
}