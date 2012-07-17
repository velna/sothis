package org.sothis.web.mvc.views.xml.converter;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlConverterImpl extends XmlConverter {

	private final static Logger LOGGER = LoggerFactory.getLogger(XmlConverterImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public void objectToXml(XmlConverter converter, Object object, Writer out, String alias) throws IOException {
		Field[] fields = object.getClass().getDeclaredFields();

		// 获取xml属性节点
		List<Attribute> attrs = getAttributeNode(object);

		out.write("<");
		out.write(alias);
		if (CollectionUtils.isNotEmpty(attrs)) {
			StringBuilder builder = new StringBuilder();
			for (Attribute attr : attrs) {
				builder.append(" ").append(attr.name).append("=\"").append(attr.value).append("\"");
			}
			out.write(builder.toString());
		}
		out.write(">");

		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			Object result = getFieldValue(fields[i].getName(), object);
			if (result == null) {
				continue;
			}
			if (result instanceof Collection) {
				converterMap.get(Collection.class).convertor(converter, result, out, fieldName, attrs);
			} else if (converterMap.containsKey(result.getClass())) {
				XmlAttribute annotation = fields[i].getAnnotation(XmlAttribute.class);
				if (annotation != null && !annotation.isNode()) {
					continue;
				}

				converterMap.get(result.getClass()).convertor(converter, result, out, fieldName, attrs);
			} else {
				objectToXml(converter, result, out, fieldName);
			}
		}

		out.write("</" + alias + ">");
		out.flush();
	}

	private List<Attribute> getAttributeNode(Object object) {
		Field[] fields = object.getClass().getDeclaredFields();
		List<Attribute> attrs = new ArrayList<Attribute>();
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			XmlAttribute annotation = fields[i].getAnnotation(XmlAttribute.class);
			if (annotation == null) {
				continue;
			}

			String val = annotation.value();
			if (val != null && !"".equals(val)) {
				attrs.add(new Attribute(fields[i].getName(), StringEscapeUtils.escapeXml(val)));
			} else {
				Object result = getFieldValue(fieldName, object);
				if (result == null) {
					continue;
				}
				attrs.add(new Attribute(fields[i].getName(), StringEscapeUtils.escapeXml(result.toString())));
			}

		}

		return attrs;
	}

	private Object getFieldValue(String fieldName, Object object) {
		String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		Method method = MethodUtils.getAccessibleMethod(object.getClass(), methodName, new Class[] {});
		if (method == null) {
			return null;
		}
		Object result = null;
		try {
			result = method.invoke(object, new Object[] {});
		} catch (IllegalArgumentException e) {
			LOGGER.error("", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("", e);
		} catch (InvocationTargetException e) {
			LOGGER.error("", e);
		}

		return result;
	}

}
