package org.sothis.web.mvc.views.json;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ModelAndView;
import org.sothis.mvc.View;
import org.sothis.mvc.ViewRenderException;
import org.sothis.web.mvc.WebActionContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@Bean(scope = Scope.SINGLETON)
public class JsonView implements View {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ViewRenderException {
		Object model = mav.model();
		Map<String, Object> params = mav.viewParams();
		WebActionContext context = (WebActionContext) invocation.getActionContext();
		HttpServletResponse response = context.getResponse();
		response.setContentType(MapUtils.getString(params, "contentType", "text/plain;charset="
				+ context.getConfiguration().getCharacterEncoding()));
		objectMapper.writeValue(response.getOutputStream(), model);
	}
}
