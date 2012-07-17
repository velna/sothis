package org.sothis.web.mvc.views.xml.converter;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class XmlConverter {
	static Map<Class, TypeConverter> converterMap;

	static {
		if (converterMap == null) {
			converterMap = new HashMap<Class, TypeConverter>();
		}
		converterMap.put(Integer.class, new BasicConverter());
		converterMap.put(String.class, new BasicConverter());
		converterMap.put(Double.class, new BasicConverter());
		converterMap.put(Float.class, new BasicConverter());
		converterMap.put(Date.class, new DateConverter());
		converterMap.put(Collection.class, new ListConverter());
	}

	public abstract void objectToXml(XmlConverter converter, Object object, Writer out, String alias) throws IOException;

	public void register(Class className, TypeConverter converter) {
		converterMap.put(className, converter);
	}
}
