package org.sothis.web.mvc.views.xml.converter;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("unchecked")
public class ListConverter implements TypeConverter<List> {
	public void convertor(XmlConverter converter, List list, Writer out, String alias, List<Attribute> attributes) throws IOException {
		for (int i = 0; i < list.size(); i++) {
			converter.objectToXml(converter, list.get(i), out, StringUtils.uncapitalize(list.get(i).getClass().getSimpleName()));
		}
	}

}
