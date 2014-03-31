package org.sothis.web.mvc.views.freemarker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.sothis.core.beans.Bean;
import org.sothis.core.beans.BeanInstantiationException;
import org.sothis.core.beans.Scope;
import org.sothis.core.util.MapUtils;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ConfigurationException;
import org.sothis.mvc.ModelAndView;
import org.sothis.mvc.View;
import org.sothis.mvc.ViewRenderException;
import org.sothis.web.mvc.WebActionContext;
import org.sothis.web.mvc.util.MvcUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Bean(scope = Scope.SINGLETON, initMethod = "init")
public class FreemarkerView implements View {

	private Configuration configuration;

	public void init() throws ClassNotFoundException, BeanInstantiationException, ConfigurationException {
		WebActionContext context = WebActionContext.getContext();
		Class<? extends ConfigurationFactory> configurationFactoryClass = context.getConfiguration().getClass(
				"freemarker.configurationFactory.class", DefaultConfigurationFactory.class);
		context.getApplicationContext().getBeanFactory()
				.registerBean(configurationFactoryClass.getName(), configurationFactoryClass);
		ConfigurationFactory configurationFactory = context.getApplicationContext().getBeanFactory()
				.getBean(configurationFactoryClass);
		configuration = configurationFactory.createConfiguration(WebActionContext.getContext());
	}

	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ViewRenderException {
		if (invocation.getAction().getActionMethod().getReturnType() == Void.class) {
			renderAsTemplate(null, null, invocation);
		} else if (null == mav.model()) {
			// empty
		} else if (mav.model() instanceof String) {
			renderAsText((String) mav.model(), invocation);
		} else {
			renderAsTemplate(mav.model(), mav.viewParams(), invocation);
		}
	}

	private void renderAsTemplate(Object model, Map<String, Object> params, ActionInvocation invocation) throws IOException {
		String path = MvcUtils.resolvePath(MapUtils.getString(params, "path"), invocation) + ".ftl";
		WebActionContext context = (WebActionContext) invocation.getActionContext();
		HttpServletResponse response = context.getResponse();
		try {
			Template template = configuration.getTemplate(path);
			Object contentType = template.getCustomAttribute("content_type");
			if (null == contentType) {
				contentType = "text/html; charset=" + context.getConfiguration().getCharacterEncoding();
			} else {
				contentType = template.getCustomAttribute("content_type").toString();
			}
			Integer status = MapUtils.getInteger(params, "status");
			if (status != null) {
				response.setStatus(status);
			}
			response.setContentType(contentType.toString());
			template.process(new AllHttpScopesHashModel(context.getRequest(), model), response.getWriter());
		} catch (FileNotFoundException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, path);
		} catch (TemplateException e) {
			throw new IOException("error processing freemarker template '" + path + "': ", e);
		}
	}

	private void renderAsText(String text, ActionInvocation invocation) throws IOException {
		WebActionContext context = (WebActionContext) invocation.getActionContext();
		HttpServletResponse response = context.getResponse();
		response.setContentType("text/plain; charset=" + context.getConfiguration().getCharacterEncoding());
		response.getWriter().append(text);
	}

}
