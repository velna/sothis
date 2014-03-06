package org.sothis.web.mvc;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.sothis.core.beans.Autowire;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.mvc.Action;
import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ActionMapper;
import org.sothis.mvc.ApplicationContext;

/**
 * ActionMapper接口的默认实现<br>
 * 可选参数：<br>
 * uriStyle: underlined|default
 * 
 * @author velna
 * 
 */
@Bean(scope = Scope.SINGLETON, autowire = Autowire.NO)
public class WebActionMapper implements ActionMapper {

	private String uriStyle;

	@Override
	public String map(ApplicationContext appContext, Action action) {
		if ("underlined".equals(uriStyle)) {
			return org.sothis.core.util.StringUtils.underlined(action.getFullName());
		} else {
			return action.getFullName();
		}
	}

	@Override
	public Action resolve(ActionContext context) {

		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		String key = null;
		if (null != request.getAttribute(WebActionContext.ACTION_URI)) {
			key = request.getAttribute(WebActionContext.ACTION_URI).toString();
		} else {
			key = request.getRequestURI().substring(request.getContextPath().length());
		}
		if (!key.startsWith("/")) {
			key = "/" + key;
		}
		if (key.endsWith("/")) {
			key = key + "index";
		}
		Action action = context.getApplicationContext().getAction(key);
		if (null != action) {
			Method[] ms = action.getAnnotation(Method.class);
			String method = null;
			for (Method m : ms) {
				if (StringUtils.isNotEmpty(m.value())) {
					method = m.value();
					break;
				}
			}
			if (null != method && !method.equalsIgnoreCase(request.getMethod())) {
				action = null;
			}
		}
		return action;
	}

	public void setUriStyle(String uriStyle) {
		this.uriStyle = uriStyle;
	}

}
