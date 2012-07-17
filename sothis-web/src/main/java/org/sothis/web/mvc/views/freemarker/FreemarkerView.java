package org.sothis.web.mvc.views.freemarker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.sothis.core.beans.Bean;
import org.sothis.core.beans.BeanInstantiationException;
import org.sothis.core.beans.Scope;
import org.sothis.core.util.MapUtils;
import org.sothis.web.mvc.ActionContext;
import org.sothis.web.mvc.ActionInvocation;
import org.sothis.web.mvc.ConfigurationException;
import org.sothis.web.mvc.ModelAndView;
import org.sothis.web.mvc.SothisConfig;
import org.sothis.web.mvc.View;
import org.sothis.web.mvc.util.MvcUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Bean(scope = Scope.SINGLETON)
public class FreemarkerView implements View {

	private Configuration configuration;

	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ServletException {
		if (null == mav.model()) {

		} else if (mav.model() instanceof String) {
			renderAsText((String) mav.model(), invocation);
		} else {
			renderAsTemplate(mav.model(), mav.viewParams(), invocation);
		}
	}

	private void renderAsTemplate(Object model, Map<String, Object> params, ActionInvocation invocation) throws IOException,
			ServletException {
		String path = MvcUtils.resolvePath(MapUtils.getString(params, "path"), invocation) + ".ftl";
		ActionContext context = invocation.getActionContext();
		HttpServletResponse response = context.getResponse();
		Configuration configuration = getConfiguration();
		try {
			Template template = configuration.getTemplate(path);
			Object contentType = template.getCustomAttribute("content_type");
			if (null == contentType) {
				contentType = "text/html; charset=" + SothisConfig.getConfig().getCharacterEncoding();
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
			throw new ServletException("error processing freemarker template '" + path + "': ", e);
		}
	}

	private void renderAsText(String text, ActionInvocation invocation) throws IOException {
		HttpServletResponse response = invocation.getActionContext().getResponse();
		response.setContentType("text/plain; charset=" + SothisConfig.getConfig().getCharacterEncoding());
		response.getWriter().append(text);
	}

	private Configuration getConfiguration() throws ServletException {
		if (null == this.configuration) {
			try {
				initConfiguration();
			} catch (Exception e) {
				throw new ServletException("error init freemarker configuration: ", e);
			}
		}
		return configuration;
	}

	private synchronized void initConfiguration() throws BeanInstantiationException, ConfigurationException,
			ClassNotFoundException {
		if (null == configuration) {
			Class<? extends ConfigurationFactory> configurationFactoryClass = SothisConfig.getConfig().getClass(
					"sothis.freemarker.configurationFactory.class", DefaultConfigurationFactory.class);
			ConfigurationFactory configurationFactory = ActionContext.getContext().getBeanFactory().getBean(
					configurationFactoryClass);
			configuration = configurationFactory.createConfiguration(ActionContext.getContext());
		}
	}
}
