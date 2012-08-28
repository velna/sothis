package org.sothis.web.mvc.views.xml.converter;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.sothis.web.mvc.views.xml.Attribute;
import org.sothis.web.mvc.views.xml.XmlNode;

public class BasicConverter implements TypeConverter<Object> {

	public void convertor(XmlConverter converter, Object object, Writer out, String alias, List<Attribute> attributes) throws IOException {
		if (object == null) {
			return;
		}
		
		XmlNode node = new XmlNode();
		node.setNode(alias);
		node.setValue(StringEscapeUtils.escapeXml(object.toString()));
		node.setAttributes(attributes);
		out.write(node.toString());
	}

}
