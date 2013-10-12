package org.sothis.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.sothis.core.beans.Autowire;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.web.mvc.annotation.Sothis;

/**
 * ActionMapper接口的默认实现
 * 
 * @author velna
 * 
 */
@Bean(scope = Scope.SINGLETON, autowire = Autowire.NO)
public class DefaultActionMapper implements ActionMapper {

	@Override
	public String map(Action action) {
		return action.getFullName();
	}

	@Override
	public Action resolve(HttpServletRequest request, HttpServletResponse response, ActionStore store) {
		if (null == request || null == response)
			throw new IllegalArgumentException("null request or response.");

		String key = null;
		if (null != request.getAttribute(ActionContext.ACTION_URI)) {
			key = request.getAttribute(ActionContext.ACTION_URI).toString();
		} else {
			key = request.getRequestURI().substring(request.getContextPath().length());
		}
		if (!key.startsWith("/")) {
			key = "/" + key;
		}
		if (key.endsWith("/")) {
			key = key + "index";
		}
		Action action = store.getAction(key);
		if (null != action) {
			Sothis[] ss = action.getAnnotation(Sothis.class);
			String method = null;
			for (Sothis s : ss) {
				if (null != s.method() && StringUtils.isNotEmpty(s.method())) {
					method = s.method();
					break;
				}
			}
			if (null != method && !method.equalsIgnoreCase(request.getMethod())) {
				action = null;
			}
		}
		return action;
	}

}
