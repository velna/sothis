package org.sothis.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractActionBase implements ActionBase {

	private final List<Class<Interceptor>> interceptors;
	private final Map<Class<?>, Annotation[]> annotations;

	public AbstractActionBase(Configuration config, AnnotatedElement... parents) {
		Map<Class<?>, List<Annotation>> aMap = new HashMap<Class<?>, List<Annotation>>(4);
		if (null != parents) {
			for (AnnotatedElement ae : parents) {
				if (null != ae) {
					for (Annotation a : ae.getAnnotations()) {
						List<Annotation> as = aMap.get(a.annotationType());
						if (null == as) {
							as = new ArrayList<Annotation>();
							aMap.put(a.annotationType(), as);
						}
						as.add(a);
					}
				}
			}
		}
		annotations = new HashMap<Class<?>, Annotation[]>(4);
		for (Map.Entry<Class<?>, List<Annotation>> entry : aMap.entrySet()) {
			Annotation[] as = new Annotation[entry.getValue().size()];
			entry.getValue().toArray(as);
			annotations.put(entry.getKey(), as);
		}

		Sothis[] ss = getAnnotation(Sothis.class);
		if (null == config) {
			throw new NullPointerException("sothis config not initialized !");
		}
		List<Class<Interceptor>> stack = null;
		for (Sothis s : ss) {
			if (null != s && StringUtils.isNotBlank(s.stack())) {
				stack = config.getInterceptorStack(s.stack());
				break;
			}
		}
		this.interceptors = stack == null ? config.getDefaultInterceptorStack() : stack;
	}

	@Override
	public List<Class<Interceptor>> getInterceptors() {
		return interceptors;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T[] getAnnotation(Class<T> annotationClass) {
		Annotation[] as = this.annotations.get(annotationClass);
		if (null == as) {
			return (T[]) Array.newInstance(annotationClass, 0);
		}
		return (T[]) as;
	}

	@Override
	public Annotation[][] getAnnotations() {
		Annotation[][] ret = new Annotation[this.annotations.size()][];
		this.annotations.values().toArray(ret);
		return ret;
	}

	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return this.annotations.containsKey(annotationClass);
	}

}
