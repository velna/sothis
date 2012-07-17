package org.sothis.web.mvc.views.xml.converter;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface TypeConverter<T> {
	public void convertor(XmlConverter converter, T object, Writer out, String alias, List<Attribute> attributes)
			throws IOException;
}
