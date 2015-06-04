package org.sothis.mvc.views.freemarker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.BeanInstantiationException;
import org.sothis.core.beans.Scope;
import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ConfigurationException;
import org.sothis.mvc.ModelAndView;
import org.sothis.mvc.View;
import org.sothis.mvc.ViewRenderException;
import org.sothis.mvc.http.HttpHeaders;
import org.sothis.mvc.http.HttpResponse;

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
		if (invocation.getAction().getActionMethod().getReturnType() == void.class) {
			// empty
		} else if (null == mav.model()) {
			renderAsTemplate(null, null, invocation);
		} else if (mav.model() instanceof String) {
			renderAsText((String) mav.model(), invocation);
		} else {
			renderAsTemplate(mav.model(), mav.viewParams(), invocation);
		}
	}

	private void renderAsTemplate(Object model, Map<String, Object> params, ActionInvocation invocation) throws IOException {
		String path = resolvePath(MapUtils.getString(params, "path"), invocation) + ".ftl";
		ActionContext context = invocation.getActionContext();
		HttpResponse response = (HttpResponse) context.getResponse();
		try {
			Template template = configuration.getTemplate(path);
			Object contentType = template.getCustomAttribute("content_type");
			if (null == contentType) {
				contentType = "text/html; charset=" + context.getRequest().getCharset();
			} else {
				contentType = template.getCustomAttribute("content_type").toString();
			}
			Integer status = MapUtils.getInteger(params, "status");
			if (status != null) {
				response.setStatus(status);
			}
			response.headers().setString(HttpHeaders.Names.CONTENT_TYPE, contentType.toString());
			template.process(new AllScopesHashModel(context, model), response.getWriter());
		} catch (FileNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (TemplateException e) {
			throw new IOException("error processing freemarker template '" + path + "': ", e);
		}
	}

	private void renderAsText(String text, ActionInvocation invocation) throws IOException {
		ActionContext context = invocation.getActionContext();
		HttpResponse response = (HttpResponse) context.getResponse();
		response.headers().setString(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=" + context.getRequest().getCharset());
		response.getWriter().append(text);
	}

	private static String resolvePath(String path, ActionInvocation invocation) {
		if (StringUtils.isEmpty(path)) {
			return invocation.getAction().getFullName();
		} else {
			if (path.charAt(0) == '/') {
				return path;
			} else {
				return new StringBuilder().append(invocation.getAction().getController().getFullName()).append(path).toString();
			}
		}
	}
}