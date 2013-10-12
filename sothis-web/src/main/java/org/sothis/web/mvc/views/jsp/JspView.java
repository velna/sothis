package org.sothis.web.mvc.views.jsp;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.core.util.MapUtils;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ModelAndView;
import org.sothis.mvc.View;
import org.sothis.mvc.ViewRenderException;
import org.sothis.web.mvc.util.MvcUtils;

@Bean(scope = Scope.SINGLETON)
public class JspView implements View {

	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ViewRenderException {
		Object model = mav.model();
		Map<String, Object> params = mav.viewParams();
		HttpServletRequest request = (HttpServletRequest) invocation.getActionContext().getRequest();
		if (null != model) {
			Map<?, ?> attrMap;
			if (model instanceof Map) {
				attrMap = (Map<?, ?>) model;
			} else {
				try {
					attrMap = PropertyUtils.describe(model);
				} catch (Exception e) {
					throw new RuntimeException("error describe view model:" + model, e);
				}
			}
			for (Object key : attrMap.keySet()) {
				request.setAttribute(String.valueOf(key), attrMap.get(key));
			}
		}
		String path = MvcUtils.resolvePath(MapUtils.getString(params, "path"), invocation) + ".jsp";
		try {
			request.getRequestDispatcher(path).forward(request, (HttpServletResponse) invocation.getActionContext().getResponse());
		} catch (ServletException e) {
			throw new ViewRenderException(e);
		}
	}

}
