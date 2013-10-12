package org.sothis.web.mvc.views.freemarker;

import java.util.Map;
import java.util.regex.Pattern;

import org.sothis.mvc.ConfigurationException;
import org.sothis.web.mvc.WebActionContext;
import org.sothis.web.mvc.WebConfiguration;

import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateHashModel;

public class DefaultConfigurationFactory implements ConfigurationFactory {

	public Configuration createConfiguration(WebActionContext actionContext) throws ConfigurationException {
		try {
			WebConfiguration config = actionContext.getConfiguration();

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
			Map<String, Class<?>> sharedDirectiveVariables = (Map) config.getAsGroup(Pattern.compile("freemarker\\.directive\\.(\\w+)\\.class"), Class.class);
			for (String key : sharedDirectiveVariables.keySet()) {
				configuration.setSharedVariable(key, sharedDirectiveVariables.get(key).newInstance());
			}

			// shared statics
			Map<String, String> sharedStaticVariables = (Map) config.getAsGroup(Pattern.compile("freemarker\\.static\\.(\\w+)\\.class"), String.class);
			TemplateHashModel staticModels = wrapper.getStaticModels();
			for (String key : sharedStaticVariables.keySet()) {
				configuration.setSharedVariable(key, staticModels.get(sharedStaticVariables.get(key)));
			}
			MultiTemplateLoader multiTemplateLoader = new MultiTemplateLoader(new TemplateLoader[] { new ClassPathTemplateLoader(),
					new WebappTemplateLoader(actionContext.getServletContext()), new WebappTemplateLoader(actionContext.getServletContext(), "/WEB-INF/") });
			configuration.setTemplateLoader(multiTemplateLoader);
			return configuration;
		} catch (Exception e) {
			throw new ConfigurationException("error create freemarker configuration: ", e);
		}
	}

}
