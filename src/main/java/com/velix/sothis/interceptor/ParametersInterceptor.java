package com.velix.sothis.interceptor;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.time.DateUtils;

import com.velix.sothis.ActionContext;
import com.velix.sothis.ActionInvocation;
import com.velix.sothis.HttpServletRequestAware;
import com.velix.sothis.HttpServletResponseAware;
import com.velix.sothis.annotation.Parameter;

public class ParametersInterceptor implements Interceptor {

	@Override
	public Object intercept(ActionInvocation invocation) throws Exception {
		ActionContext context = invocation.getInvocationContext();
		Map<String, Object[]> parameterMap = getParameterMap(context
				.getRequest());

		Class<?>[] paramTypes = invocation.getAction().getParameterTypes();
		Object[] actionParams = new Object[paramTypes.length];
		Annotation[][] paramAnnotations = invocation.getAction()
				.getParameterAnnotations();

		for (int i = 0; i < paramTypes.length; i++) {
			Class<?> type = paramTypes[i];
			if (type.isAssignableFrom(HttpServletRequest.class)) {
				actionParams[i] = context.getRequest();
			} else if (type.isAssignableFrom(HttpServletResponse.class)) {
				actionParams[i] = context.getResponse();
			} else {
				Annotation[] annotations = paramAnnotations[i];
				Parameter parameter = null;
				for (Annotation a : annotations) {
					if (a.annotationType() == Parameter.class) {
						parameter = (Parameter) a;
					}
				}
				actionParams[i] = getActionParamByAnnotation(parameter,
						parameterMap, type, context);
			}
		}
		context.put(ActionContext.ACTION_PARAMS, actionParams);
		return invocation.invoke();
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object[]> getParameterMap(HttpServletRequest request) {
		if (request instanceof MultipartHttpServletRequest) {
			return ((MultipartHttpServletRequest) request).getAllParameterMap();
		} else {
			return request.getParameterMap();
		}
	}

	private Object getActionParamByAnnotation(Parameter parameter,
			Map<String, Object[]> parameterMap, Class<?> type,
			ActionContext context) throws Exception {
		String name = null == parameter ? "" : parameter.name();
		String pattern = null == parameter ? "" : parameter.pattern();
		if ("".equals(name)) {
			Object paramBean = type.newInstance();
			if (paramBean instanceof HttpServletRequestAware) {
				((HttpServletRequestAware) paramBean).setRequest(context
						.getRequest());
			}
			if (paramBean instanceof HttpServletResponseAware) {
				((HttpServletResponseAware) paramBean).setResponse(context
						.getResponse());
			}
			return populate(parameterMap, paramBean, pattern);
		} else {
			return convert(parameterMap.get(name), type, pattern);
		}
	}

	private Object populate(Map<String, Object[]> parameterMap,
			Object paramBean, String p) throws Exception {
		Pattern pattern = "".equals(p) ? null : Pattern.compile(p);
		for (Map.Entry<String, Object[]> entry : parameterMap.entrySet()) {
			if (null == entry.getKey()
					|| (null != pattern && !pattern.matcher(entry.getKey())
							.matches())) {
				continue;
			}
			Object bean = paramBean;
			StringTokenizer tokenizer = new StringTokenizer(entry.getKey(), ".");
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				if (bean instanceof Map) {
					while (tokenizer.hasMoreTokens())
						token += "." + tokenizer.nextToken();
					((Map) bean).put(token, entry.getValue());
					break;
				}
				PropertyDescriptor descriptor = PropertyUtils
						.getPropertyDescriptor(bean, token);
				if (null == descriptor) {
					break;
				}
				Class<?> propertyType = descriptor.getPropertyType();
				Method writeMethod = PropertyUtils.getWriteMethod(descriptor);
				if (null == writeMethod) {
					break;
				}
				if (tokenizer.hasMoreTokens()) {
					Object propertyValue = propertyType.newInstance();
					writeMethod.invoke(bean, propertyValue);
					bean = propertyValue;
				} else {
					Object propertyValue = convert(entry.getValue(),
							propertyType, p);
					if (null != propertyValue) {
						writeMethod.invoke(bean, propertyValue);
					}
				}
			}
		}
		return paramBean;
	}

	@SuppressWarnings("unchecked")
	private Object convert(Object[] values, Class<?> targetType, String pattern)
			throws Exception {
		if (null == values || values.length == 0 || null == targetType) {
			return null;
		}
		Object bean = null;
		if (targetType.isArray()) {
			Class<?> componentType = targetType.getComponentType();
			bean = Array.newInstance(componentType, values.length);
			for (int i = 0; i < values.length; i++) {
				Object value = convert(new Object[] { values[i] },
						componentType, pattern);
				Array.set(bean, i, value);
			}
		} else if (Collection.class.isAssignableFrom(targetType)) {
			Collection<Object> collection = null;
			if (targetType == List.class) {
				collection = new ArrayList<Object>(values.length);
			} else if (targetType == Set.class) {
				collection = new HashSet<Object>(values.length);
			} else if (targetType == Queue.class) {
				collection = new LinkedList<Object>();
			} else {
				collection = (Collection<Object>) targetType.newInstance();
			}
			for (Object value : values) {
				collection.add(value);
			}
			bean = collection;
		} else if (null != values[0]) {
			Object value = values[0];
			String stringValue = value.toString();
			if (targetType.isInstance(value)) {
				bean = value;
			} else if (targetType == String.class) {
				bean = stringValue;
			} else if (stringValue.length() > 0) {
				if (targetType == boolean.class || targetType == Boolean.class) {
					bean = Boolean.parseBoolean(stringValue);
				} else if (targetType == byte.class || targetType == Byte.class) {
					bean = Byte.parseByte(stringValue);
				} else if (targetType == char.class
						|| targetType == Character.class) {
					bean = stringValue.charAt(0);
				} else if (targetType == short.class
						|| targetType == Short.class) {
					bean = Short.parseShort(stringValue);
				} else if (targetType == int.class
						|| targetType == Integer.class) {
					bean = Integer.parseInt(stringValue);
				} else if (targetType == long.class || targetType == Long.class) {
					bean = Long.parseLong(stringValue);
				} else if (targetType == float.class
						|| targetType == Float.class) {
					bean = Float.parseFloat(stringValue);
				} else if (targetType == double.class
						|| targetType == Double.class) {
					bean = Double.parseDouble(stringValue);
				} else if (targetType.isEnum()) {
					Object[] enums = targetType.getEnumConstants();
					for (Object obj : enums) {
						Enum<?> e = (Enum<?>) obj;
						if (e.name().equals(stringValue)) {
							bean = e;
						}
					}
				} else if (Date.class.isAssignableFrom(targetType)) {
					Date date = (Date) targetType.newInstance();
					date.setTime(DateUtils.parseDate(
							stringValue,
							new String[] { "".equals(pattern) ? "yyyy-MM-dd"
									: pattern }).getTime());
					bean = date;
				} else {
					// empty
				}
			} else {
				// empty
			}
		} else {
			// empty
		}
		return bean;
	}
}
