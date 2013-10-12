package org.sothis.web.mvc.views.xml;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang.StringUtils;
import org.sothis.web.mvc.views.xml.converter.TypeConverter;
import org.sothis.web.mvc.views.xml.converter.XmlConverter;

public class XmlSerializable {
	private XmlConverter converter;

	public XmlSerializable(XmlConverter converter) {
		this.converter = converter;
	}

	public void objectToXml(Object object, Writer out) throws IOException {
		converter.objectToXml(converter, object, out, StringUtils.uncapitalize(object.getClass().getSimpleName()));
	}

	@SuppressWarnings({ "rawtypes" })
	public void addConverter(Class className, TypeConverter converter) {
		this.converter.register(className, converter);
	}

}
