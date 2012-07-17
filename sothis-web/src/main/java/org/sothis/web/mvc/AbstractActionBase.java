package org.sothis.web.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.sothis.web.mvc.annotation.Sothis;

public abstract class AbstractActionBase implements ActionBase {

	private final InterceptorStack interceptorStack;
	private final Map<Class<?>, Annotation> annotations;

	public AbstractActionBase(AnnotatedElement... elements) {
		if (null != elements) {
			this.annotations = new HashMap<Class<?>, Annotation>(4);
			for (AnnotatedElement ae : elements) {
				if (null != ae) {
					for (Annotation a : ae.getAnnotations()) {
						this.annotations.put(a.annotationType(), a);
					}
				}
			}
		} else {
			this.annotations = Collections.emptyMap();
		}

		Sothis sothis = getAnnotation(Sothis.class);
		SothisConfig config = SothisConfig.getConfig();
		if (null == config) {
			throw new NullPointerException("sothis config not initialized !");
		}
		if (null != sothis && StringUtils.isNotBlank(sothis.stack())) {
			this.interceptorStack = config.getInterceptorStack(sothis.stack());
		} else {
			this.interceptorStack = config.getDefaultInterceptorStack();
		}
	}

	@Override
	public InterceptorStack getInterceptorStack() {
		return interceptorStack;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return (T) annotations.get(annotationClass);
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
