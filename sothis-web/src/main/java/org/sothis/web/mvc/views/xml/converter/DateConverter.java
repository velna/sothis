package org.sothis.web.mvc.views.xml.converter;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
public class DateConverter implements TypeConverter<Date> {

	public void convertor(XmlConverter converter, Date object, Writer out, String alias, List<Attribute> attributes) throws IOException {
		if(object == null) {
			return;
		}
		XmlNode node = new XmlNode();
		node.setNode(alias);
		node.setValue(DateFormatUtils.format((Date)object, "yyyy-MM-dd hh:mm:ss"));
		out.write(node.toString());
	}

}
