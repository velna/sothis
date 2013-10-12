package org.sothis.web.mvc.views.redirect;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.core.util.MapUtils;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ModelAndView;
import org.sothis.mvc.View;
import org.sothis.mvc.ViewRenderException;
import org.sothis.web.mvc.WebActionContext;
import org.sothis.web.mvc.util.MvcUtils;

@Bean(scope = Scope.SINGLETON)
public class RedirectView implements View {

	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ViewRenderException {
		Map<String, Object> params = mav.viewParams();
		HttpServletResponse response = (HttpServletResponse) invocation.getActionContext().getResponse();
		String location = MapUtils.getString(params, "location");
		if (isPathUrl(location)) {
			String contextPath = WebActionContext.getContext().getServletContext().getContextPath();
			if (!location.startsWith(contextPath)) {
				location = contextPath + MvcUtils.resolvePath(location, invocation);
			}
		}
		String statusCode = MapUtils.getString(params, "statusCode");
		if (NumberUtils.isNumber(statusCode)) {
			response.setStatus(Integer.parseInt(statusCode));
			response.setHeader("Location", location);
			response.getWriter().write(location);
			response.getWriter().close();
		} else {
			response.sendRedirect(location);
		}
	}

	private static boolean isPathUrl(String url) {
		return (url.indexOf(':') == -1);
	}

}
