package org.sothis.mvc.http.views.freemarker;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.BeanInstantiationException;
import org.sothis.core.beans.Scope;
import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ConfigurationException;
import org.sothis.mvc.ModelAndView;
import org.sothis.mvc.Response;
import org.sothis.mvc.View;
import org.sothis.mvc.ViewRenderException;
import org.sothis.mvc.http.HttpConstants;
import org.sothis.mvc.util.MvcUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Bean(scope = Scope.SINGLETON, initMethod = "init")
public class FreemarkerView implements View {

	private Configuration configuration;

	public void init() throws ClassNotFoundException, BeanInstantiationException, ConfigurationException {
		ActionContext context = ActionContext.getContext();
		Class<? extends ConfigurationFactory> configurationFactoryClass = context.getApplicationContext().getConfiguration()
				.getClass("freemarker.configurationFactory.class", DefaultConfigurationFactory.class);
		context.getApplicationContext().getBeanFactory()
				.registerBean(configurationFactoryClass.getName(), configurationFactoryClass);
		ConfigurationFactory configurationFactory = context.getApplicationContext().getBeanFactory()
				.getBean(configurationFactoryClass);
		configuration = configurationFactory.createConfiguration(context);
	}

	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ViewRenderException {
		if (null == mav.model()) {
			renderAsTemplate(null, null, invocation);
		} else if (mav.model() instanceof String) {
			renderAsText((String) mav.model(), invocation);
		} else {
			renderAsTemplate(mav.model(), mav.viewParams(), invocation);
		}
	}

	private void renderAsTemplate(Object model, Map<String, Object> params, ActionInvocation invocation) throws IOException {
		String path = MvcUtils.resolvePath(MapUtils.getString(params, "location"), invocation) + ".ftl";
		ActionContext context = invocation.getActionContext();
		Response response = context.getResponse();
		try {
			Template template = configuration.getTemplate(path);
			if (!response.headers().contains(HttpConstants.HeaderNames.CONTENT_TYPE)) {
				Object contentType = template.getCustomAttribute("content_type");
				if (null == contentType) {
					contentType = "text/html; charset=" + context.getRequest().getCharset();
				} else {
					contentType = template.getCustomAttribute("content_type").toString();
				}
				response.headers().setString(HttpConstants.HeaderNames.CONTENT_TYPE, contentType.toString());
			}
			Integer status = MapUtils.getInteger(params, "status");
			if (status != null) {
				response.setStatus(status);
			}
			template.process(new AllScopesHashModel(context, model), response.getWriter());
		} catch (TemplateException e) {
			throw new IOException("error processing freemarker template '" + path + "': ", e);
		}
	}

	private void renderAsText(String text, ActionInvocation invocation) throws IOException {
		ActionContext context = invocation.getActionContext();
		Response response = context.getResponse();
		response.headers().setString(HttpConstants.HeaderNames.CONTENT_TYPE,
				"text/plain; charset=" + context.getRequest().getCharset());
		response.getWriter().append(text);
	}

}