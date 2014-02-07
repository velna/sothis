package org.sothis.mvc;

import java.lang.annotation.Annotation;
import java.util.List;

public interface ActionBase {

	/**
	 * 名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 完整名称，包括package和controller路径
	 * 
	 * @return
	 */
	String getFullName();

	List<Class<Interceptor>> getInterceptors();

	boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);

	<T extends Annotation> T[] getAnnotation(Class<T> annotationClass);

	Annotation[] getAnnotations();

	Annotation[] getDeclaredAnnotations();
}
