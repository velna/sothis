package org.sothis.mvc.http.views.redirect;

import java.io.IOException;

import org.apache.commons.collections4.MapUtils;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ModelAndView;
import org.sothis.mvc.Response;
import org.sothis.mvc.View;
import org.sothis.mvc.ViewRenderException;
import org.sothis.mvc.http.HttpConstants;
import org.sothis.mvc.util.MvcUtils;

@Bean(scope = Scope.SINGLETON)
public class RedirectView implements View {

	@Override
	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ViewRenderException {
		Response response = invocation.getActionContext().getResponse();
		String location = MapUtils.getString(mav.viewParams(), "location");
		if (null == location) {
			throw new ViewRenderException("no location found.");
		}
		if (isPathUrl(location)) {
			String contextPath = invocation.getActionContext().getApplicationContext().getContextPath();
			if (!location.startsWith(contextPath)) {
				location = contextPath + MvcUtils.resolvePath(location, invocation);
			}
		}
		Integer status = MapUtils.getInteger(mav.viewParams(), "status", HttpConstants.StatusCodes.SC_MOVED_TEMPORARILY);
		response.setStatus(status);
		response.headers().setString(HttpConstants.HeaderNames.LOCATION, location);
		response.getWriter().print(location);
	}

	private static boolean isPathUrl(String url) {
		return (url.indexOf(':') == -1);
	}

}
