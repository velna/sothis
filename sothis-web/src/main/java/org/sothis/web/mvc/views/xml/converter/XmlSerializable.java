package org.sothis.web.mvc.views.xml.converter;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang.StringUtils;

public class XmlSerializable {
	private XmlConverter converter;

	public XmlSerializable(XmlConverter converter) {
		this.converter = converter;
	}

	public void objectToXml(Object object, Writer out) throws IOException {
		converter.objectToXml(converter, object, out, StringUtils.uncapitalize(object.getClass().getSimpleName()));
	}

	@SuppressWarnings("unchecked")
	public void addConverter(Class className, TypeConverter converter) {
		this.converter.register(className, converter);
	}

}
