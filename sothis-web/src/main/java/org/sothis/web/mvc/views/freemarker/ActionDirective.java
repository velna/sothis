package org.sothis.web.mvc.views.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.sothis.core.util.MapUtils;
import org.sothis.web.mvc.ActionContext;
import org.sothis.web.mvc.ActionInvocationHelper;
import org.sothis.web.mvc.util.WrappedHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ActionDirective implements TemplateDirectiveModel {

	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {

		ActionContext actionContext = ActionContext.getContext();
		if (StringUtils.isBlank(actionContext.getAction().getName())) {
			throw new IllegalArgumentException("d_action actionName is null!");
		}
		String controller = MapUtils.getString(params, "controller", actionContext.getAction().getController().getName());
		if (StringUtils.isNotBlank(controller)) {
			controller = "/" + controller;
		}
		String action = MapUtils.getString(params, "action", actionContext.getAction().getName());

		Writer out = env.getOut();
		if (body != null) {
			body.render(out);
		}
		Map<String, String[]> myParams = new HashMap<String, String[]>();
		String[] attrs = env.getCustomAttributeNames();
		if (attrs != null) {
			for (int i = 0; i < attrs.length; i++) {
				myParams.put(attrs[i],
						new String[] { URLDecoder.decode(String.valueOf(env.getCustomAttribute(attrs[i])), "UTF-8") });
				env.removeCustomAttribute(attrs[i]);
			}
		}
		Map<String, Object> orgContext = actionContext.getContextMap();
		String orgActionUri = (String) actionContext.getRequest().getAttribute(ActionContext.ACTION_URI);
		MockHttpServletResponse response = new MockHttpServletResponse();
		actionContext.setResponse(response);
		WrappedHttpServletRequest myRequest = new WrappedHttpServletRequest(actionContext.getRequest(), myParams);
		myRequest.setAttribute(ActionContext.ACTION_URI, controller + "/" + action);
		actionContext.setRequest(myRequest);
		try {
			ActionInvocationHelper.invoke(actionContext);
			env.getOut().write(response.getContentAsString());
		} catch (ServletException e) {
			throw new TemplateException("error invoke action directive:" + controller + "/" + action,
					(Exception) e.getRootCause(), env);
		} finally {
			actionContext.setContextMap(orgContext);
			actionContext.getRequest().setAttribute(ActionContext.ACTION_URI, orgActionUri);
		}
	}

}
