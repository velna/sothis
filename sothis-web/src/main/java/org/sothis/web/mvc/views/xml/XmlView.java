package org.sothis.web.mvc.views.xml;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.core.util.MapUtils;
import org.sothis.web.mvc.ActionInvocation;
import org.sothis.web.mvc.ModelAndView;
import org.sothis.web.mvc.SothisConfig;
import org.sothis.web.mvc.View;
import org.sothis.web.mvc.views.xml.converter.XmlConverterImpl;

@Bean(scope = Scope.SINGLETON)
public class XmlView implements View {
	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ServletException {
		Object object = mav.model();
		XmlSerializable serializ = new XmlSerializable(new XmlConverterImpl());
		HttpServletResponse response = invocation.getActionContext().getResponse();
		response.setContentType(MapUtils.getString(mav.viewParams(), "contentType", "text/xml;charset="
				+ SothisConfig.getConfig().getCharacterEncoding()));
		Writer writer = response.getWriter();
		writer.write("<?xml version=\"1.0\" encoding=\"" + SothisConfig.getConfig().getCharacterEncoding() + "\" ?>");
		serializ.objectToXml(object, response.getWriter());
	}

}
