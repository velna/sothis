package org.sothis.mvc.http.views.freemarker;

import javax.servlet.ServletContext;

import org.sothis.mvc.ActionContext;

import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;

public class ServletConfigurationFactory extends DefaultConfigurationFactory {

	@Override
	protected TemplateLoader buildTemplateLoader(ActionContext actionContext) {
		ServletContext servletContext = (ServletContext) actionContext.getApplicationContext().getNativeContext();
		MultiTemplateLoader multiTemplateLoader = new MultiTemplateLoader(new TemplateLoader[] { new ClassPathTemplateLoader(),
				new WebappTemplateLoader(servletContext), new WebappTemplateLoader(servletContext, "/WEB-INF/") });
		return multiTemplateLoader;
	}

}
