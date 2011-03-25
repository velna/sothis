package org.sothis.web.mvc;

import java.lang.annotation.Annotation;

public interface Controller {

	public Action getAction(String actionName);

	public Object newInstance() throws Exception;

	public Annotation[] getAnnotations();

	public Class<?> getControllerClass();

	public String getName();

}
