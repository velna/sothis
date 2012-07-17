package org.sothis.web.mvc;

import java.lang.reflect.AnnotatedElement;

public interface ActionBase extends AnnotatedElement {

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

	InterceptorStack getInterceptorStack();

}
