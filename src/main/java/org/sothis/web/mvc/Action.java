package org.sothis.web.mvc;

import java.lang.annotation.Annotation;

public interface Action {

	public Object invoke(ActionContext context, Object... params)
			throws Exception;

	public Controller getController();

	public Class<?>[] getParameterTypes();

	public Annotation[][] getParameterAnnotations();

	public Annotation[] getAnnotations();

	public String getName();
}
