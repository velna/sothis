package org.sothis.mvc.http.views.json;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ModelAndView;
import org.sothis.mvc.Response;
import org.sothis.mvc.View;
import org.sothis.mvc.ViewRenderException;
import org.sothis.mvc.http.HttpConstants;

import com.fasterxml.jackson.databind.ObjectMapper;

@Bean(scope = Scope.SINGLETON)
public class JsonView implements View {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ViewRenderException {
		Object model = mav.model();
		Map<String, Object> params = mav.viewParams();
		ActionContext context = invocation.getActionContext();
		Response response = context.getResponse();
		response.headers().setString(HttpConstants.HeaderNames.CONTENT_TYPE,
				MapUtils.getString(params, "contentType", "text/plain;charset=" + context.getRequest().getCharset()));
		objectMapper.writeValue(response.getOutputStream(), model);
	}
}
