package org.sothis.web.mvc.views.xml;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.core.util.MapUtils;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ModelAndView;
import org.sothis.mvc.View;
import org.sothis.mvc.ViewRenderException;
import org.sothis.web.mvc.WebActionContext;
import org.sothis.web.mvc.views.xml.converter.XmlConverterImpl;

@Bean(scope = Scope.SINGLETON)
public class XmlView implements View {
	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ViewRenderException {
		Object object = mav.model();
		XmlSerializable serializ = new XmlSerializable(new XmlConverterImpl());
		WebActionContext context = (WebActionContext) invocation.getActionContext();
		HttpServletResponse response = context.getResponse();
		response.setContentType(MapUtils.getString(mav.viewParams(), "contentType", "text/xml;charset=" + context.getConfiguration().getCharacterEncoding()));
		Writer writer = response.getWriter();
		writer.write("<?xml version=\"1.0\" encoding=\"" + context.getConfiguration().getCharacterEncoding() + "\" ?>");
		serializ.objectToXml(object, response.getWriter());
	}

}
