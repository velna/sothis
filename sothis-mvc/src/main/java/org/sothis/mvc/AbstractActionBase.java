package org.sothis.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sothis.core.util.StringUtils;

public abstract class AbstractActionBase implements ActionBase {

	private final List<Class<Interceptor>> interceptors;
	private final Map<Class<?>, Object> annotations;

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
		annotations = new HashMap<Class<?>, Object>(4);
		for (Map.Entry<Class<?>, List<Annotation>> entry : aMap.entrySet()) {
			Object as = Array.newInstance(entry.getKey(), entry.getValue().size());
			entry.getValue().toArray((Annotation[]) as);
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
		T[] as = (T[]) this.annotations.get(annotationClass);
		if (null == as) {
			as = (T[]) Array.newInstance(annotationClass, 0);
		}
		return as;
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
