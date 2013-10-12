package org.sothis.web.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.sothis.web.mvc.annotation.Sothis;

public abstract class AbstractActionBase implements ActionBase {

	private final InterceptorStack interceptorStack;
	private final Map<Class<?>, List<Annotation>> annotations;

	public AbstractActionBase(AnnotatedElement... parents) {
		this.annotations = new HashMap<Class<?>, List<Annotation>>(4);
		if (null != parents) {
			for (AnnotatedElement ae : parents) {
				if (null != ae) {
					for (Annotation a : ae.getAnnotations()) {
						List<Annotation> as = this.annotations.get(a.annotationType());
						if (null == as) {
							as = new ArrayList<Annotation>();
							this.annotations.put(a.annotationType(), as);
						}
						as.add(a);
					}
				}
			}
		}

		Sothis[] ss = getAnnotation(Sothis.class);
		SothisConfig config = SothisConfig.getConfig();
		if (null == config) {
			throw new NullPointerException("sothis config not initialized !");
		}
		InterceptorStack stack = null;
		for (Sothis s : ss) {
			if (null != s && StringUtils.isNotBlank(s.stack())) {
				stack = config.getInterceptorStack(s.stack());
				break;
			}
		}
		this.interceptorStack = stack == null ? config.getDefaultInterceptorStack() : stack;
	}

	@Override
	public InterceptorStack getInterceptorStack() {
		return interceptorStack;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T[] getAnnotation(Class<T> annotationClass) {
		List<Annotation> as = this.annotations.get(annotationClass);
		if (null == as) {
			return (T[]) Array.newInstance(annotationClass, 0);
		}
		Annotation[] ret = new Annotation[as.size()];
		this.annotations.values().toArray(ret);
		return (T[]) ret;
	}

	@Override
	public Annotation[] getAnnotations() {
		Annotation[] ret = new Annotation[this.annotations.size()];
		this.annotations.values().toArray(ret);
		return ret;
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return getAnnotations();
	}

	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return this.annotations.containsKey(annotationClass);
	}

}
