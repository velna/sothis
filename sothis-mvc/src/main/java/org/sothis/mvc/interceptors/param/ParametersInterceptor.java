package org.sothis.mvc.interceptors.param;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.core.util.DateUtils;
import org.sothis.core.util.StringUtils;
import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ActionInvocationException;
import org.sothis.mvc.Ignore;
import org.sothis.mvc.Interceptor;
import org.sothis.mvc.Request;
import org.sothis.mvc.Response;

/**
 * 参数注入拦截器
 * 
 * @author velna
 * 
 */
@Bean(scope = Scope.SINGLETON)
public class ParametersInterceptor implements Interceptor {

	public Object intercept(ActionInvocation invocation) throws Exception {
		ActionContext context = (ActionContext) invocation.getActionContext();
		Map<String, Object[]> parameterMap = context.getRequestParameters();
		Class<?>[] paramTypes = invocation.getAction().getActionMethod().getParameterTypes();
		final Object[] actionParams = new Object[paramTypes.length];
		Annotation[][] paramAnnotations = invocation.getAction().getActionMethod().getParameterAnnotations();

		for (int i = 0; i < paramTypes.length; i++) {
			Class<?> type = paramTypes[i];
			if (type.isAssignableFrom(Request.class)) {
				actionParams[i] = context.getRequest();
			} else if (type.isAssignableFrom(Response.class)) {
				actionParams[i] = context.getResponse();
			} else {
				Annotation[] annotations = paramAnnotations[i];
				Param parameter = null;
				for (Annotation a : annotations) {
					if (a.annotationType() == Param.class) {
						parameter = (Param) a;
					}
				}
				try {
					actionParams[i] = getActionParamByAnnotation(parameter, parameterMap, type, context);
				} catch (Exception e) {
					throw new ActionInvocationException(e);
				}
			}
		}
		context.put(ActionContext.ACTION_PARAMS, actionParams);
		return invocation.invoke();
	}

	@SuppressWarnings("unchecked")
	private Object getActionParamByAnnotation(Param parameter, Map<String, Object[]> parameterMap, Class<?> type,
			ActionContext context) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		String name = null == parameter ? "" : parameter.name();
		String pattern = null == parameter ? "" : parameter.pattern();
		if ("".equals(name)) {
			if (type.isPrimitive()) {
				return getPrimitiveDefaultValue(type);
			}
			if (isPrimitiveLike(type)) {
				return null;
			}
			if (type.isEnum()) {
				return getEnumValue(parameterMap, (Class<Enum<?>>) type);
			}
			Object paramBean = newInstance(type);
			return populate(parameterMap, paramBean, pattern);
		} else {
			return convert(parameterMap.get(name), type, pattern);
		}
	}

	private Enum<?> getEnumValue(Map<String, Object[]> parameterMap, Class<Enum<?>> type) {
		if (null != parameterMap) {
			Enum<?>[] es = type.getEnumConstants();
			if (null != es) {
				for (Enum<?> e : es) {
					if (parameterMap.containsKey(e.name())) {
						return e;
					}
				}
			}
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes" })
	private Object newInstance(Class<?> type) throws InstantiationException, IllegalAccessException {
		if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
			if (Map.class.isAssignableFrom(type)) {
				return new HashMap();
			} else {
				return null;
			}
		}
		return type.newInstance();
	}

	private Object getPrimitiveDefaultValue(Class<?> type) {
		if (type == boolean.class) {
			return false;
		} else if (type == byte.class) {
			return (byte) 0;
		} else if (type == short.class) {
			return (short) 0;
		} else if (type == int.class) {
			return 0;
		} else if (type == long.class) {
			return 0L;
		} else if (type == float.class) {
			return 0F;
		} else if (type == double.class) {
			return 0D;
		} else if (type == char.class) {
			return (char) 0;
		} else {
			return null;
		}
	}

	private boolean isPrimitiveLike(Class<?> type) {
		if (type == Boolean.class || type == Short.class || type == Byte.class || type == Integer.class || type == Long.class
				|| type == Double.class || type == Float.class || type == String.class || type == Character.class
				|| type == Date.class || type == Number.class) {
			return true;
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object populate(Map<String, Object[]> parameterMap, Object paramBean, String p) throws IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Pattern pattern = "".equals(p) ? null : Pattern.compile(p);
		for (Map.Entry<String, Object[]> entry : parameterMap.entrySet()) {
			if (null == entry.getKey() || (null != pattern && !pattern.matcher(entry.getKey()).matches())) {
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
				PropertyDescriptor descriptor = null;
				try {
					descriptor = PropertyUtils.getPropertyDescriptor(bean, token);
				} catch (NoSuchMethodException e) {
				}
				if (null == descriptor) {
					break;
				}
				Class<?> propertyType = descriptor.getPropertyType();
				Method writeMethod = PropertyUtils.getWriteMethod(descriptor);
				if (null == writeMethod || writeMethod.isAnnotationPresent(Ignore.class)) {
					break;
				}
				Type[] types = writeMethod.getGenericParameterTypes();
				Annotation[] as = writeMethod.getParameterAnnotations()[0];
				Param parameter = null;
				for (Annotation a : as) {
					if (a.annotationType() == Param.class) {
						parameter = (Param) a;
					}
				}
				// 如果是泛型，则尽量找到泛型对应的实际类型
				if (types != null && types.length > 0 && types[0] instanceof TypeVariable) {
					TypeVariable typeVariable = (TypeVariable) types[0];
					Type type = bean.getClass().getGenericSuperclass();
					if (type instanceof ParameterizedType) {
						ParameterizedType parameterizedType = (ParameterizedType) type;
						TypeVariable[] typeVariables = ((Class) parameterizedType.getRawType()).getTypeParameters();
						Type[] ts = parameterizedType.getActualTypeArguments();
						for (int i = 0; i < typeVariables.length; i++) {
							if (typeVariables[i].equals(typeVariable)) {
								propertyType = (Class) ts[i];
								break;
							}
						}
					}
				}
				if (tokenizer.hasMoreTokens()) {
					Method readMethod = PropertyUtils.getReadMethod(descriptor);
					Object propertyValue = null;
					if (null != readMethod) {
						propertyValue = readMethod.invoke(bean, (Object[]) null);
					}
					if (null == propertyValue) {
						propertyValue = newInstance(propertyType);
						writeMethod.invoke(bean, propertyValue);
					}
					bean = propertyValue;
				} else {
					Object propertyValue = convert(entry.getValue(), propertyType, parameter != null ? parameter.pattern() : p);
					if (null != propertyValue) {
						writeMethod.invoke(bean, propertyValue);
					}
				}
			}
		}
		return paramBean;
	}

	@SuppressWarnings("unchecked")
	private Object convert(Object[] values, Class<?> targetType, String pattern) throws InstantiationException,
			IllegalAccessException {
		if (null == values || values.length == 0) {
			if (targetType.isPrimitive()) {
				return getPrimitiveDefaultValue(targetType);
			} else {
				return null;
			}
		}
		Object bean = null;
		if (targetType.isArray()) {
			Class<?> componentType = targetType.getComponentType();
			Object objectArray = Array.newInstance(componentType, values.length);
			int notNullCount = 0;
			for (int i = 0; i < values.length; i++) {
				Object value = convert(new Object[] { values[i] }, componentType, pattern);
				if (null != value) {
					Array.set(objectArray, notNullCount, value);
					notNullCount++;
				}
			}
			bean = Array.newInstance(componentType, notNullCount);
			System.arraycopy(objectArray, 0, bean, 0, notNullCount);
		} else if (Collection.class.isAssignableFrom(targetType)) {
			Collection<Object> collection = null;
			if (targetType == List.class) {
				collection = new ArrayList<Object>(values.length);
			} else if (targetType == Set.class) {
				collection = new HashSet<Object>(values.length);
			} else {
				collection = (Collection<Object>) targetType.newInstance();
			}
			for (Object value : values) {
				collection.add(value);
			}
			bean = collection;
		} else if (targetType == String.class) {
			if (1 == values.length) {
				bean = values[0];
			} else {
				bean = StringUtils.join(values, ',');
			}
		} else if (null != values[0]) {
			Object value = values[0];
			String stringValue = value.toString();
			if (targetType.isInstance(value)) {
				bean = value;
			} else if (stringValue.length() > 0) {
				try {
					if (targetType == boolean.class || targetType == Boolean.class) {
						bean = Boolean.parseBoolean(stringValue);
					} else if (targetType == byte.class || targetType == Byte.class) {
						bean = Byte.parseByte(stringValue);
					} else if (targetType == char.class || targetType == Character.class) {
						bean = stringValue.charAt(0);
					} else if (targetType == short.class || targetType == Short.class) {
						bean = Short.parseShort(stringValue);
					} else if (targetType == int.class || targetType == Integer.class) {
						bean = Integer.parseInt(stringValue);
					} else if (targetType == long.class || targetType == Long.class) {
						bean = Long.parseLong(stringValue);
					} else if ((targetType == float.class || targetType == Float.class) && isSimpleNumberic(stringValue)) {
						bean = Float.parseFloat(stringValue);
					} else if ((targetType == double.class || targetType == Double.class || targetType == Number.class)
							&& isSimpleNumberic(stringValue)) {
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
						date.setTime(DateUtils.parseDate(stringValue,
								new String[] { "".equals(pattern) ? "yyyy-MM-dd" : pattern }).getTime());
						bean = date;
					}
				} catch (NumberFormatException e) {

				} catch (ParseException e) {

				}
			}
		}
		return bean;
	}

	private boolean isSimpleNumberic(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			char ch = str.charAt(i);
			if (Character.isDigit(ch) || ch == '.' || ch == '-' || ch == '+') {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}
}