package org.sothis.web.mvc.views.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ModelAndView;
import org.sothis.mvc.View;
import org.sothis.mvc.ViewRenderException;

@Bean(scope = Scope.SINGLETON)
public class StreamView implements View {

	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ViewRenderException {
		Object model = mav.model();
		Map<String, Object> params = mav.viewParams();
		InputStream input = null;
		if (model instanceof StreamModel) {
			input = ((StreamModel) model).getInputStream();
		} else if (model instanceof InputStream) {
			input = (InputStream) model;
		} else {
			input = (InputStream) MapUtils.getObject(params, "inputStream");
		}
		HttpServletResponse response = (HttpServletResponse) invocation.getActionContext().getResponse();
		response.setContentType(MapUtils.getString(params, "contentType"));
		response.setHeader("Content-Disposition", MapUtils.getString(params, "Content-Disposition"));
		if (null != input) {
			IOUtils.copy(input, response.getOutputStream());
		}
	}

}
