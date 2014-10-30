package org.sothis.web.mvc.views.xml.converter;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.sothis.core.util.StringUtils;
import org.sothis.web.mvc.views.xml.Attribute;

@SuppressWarnings("rawtypes")
public class ListConverter implements TypeConverter<List> {

	public void convertor(XmlConverter converter, List list, Writer out, String alias, List<Attribute> attributes)
			throws IOException {
		for (int i = 0; i < list.size(); i++) {
			converter.objectToXml(converter, list.get(i), out, StringUtils.uncapitalize(list.get(i).getClass().getSimpleName()));
		}
	}

}
