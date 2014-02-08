package org.sothis.web.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.sothis.mvc.AbstractActionBase;
import org.sothis.mvc.Action;
import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ActionInvocationException;
import org.sothis.mvc.Configuration;
import org.sothis.mvc.Controller;

public class MockAction extends AbstractActionBase implements Action {

	private Object invokeResult;
	private Controller controller;
	private Class<?>[] parameterTypes;

	public MockAction(Configuration config) {
		super(config);
	}

	public Object invoke(ActionContext context, Object... params) throws ActionInvocationException {
		return invokeResult;
	}

	public Controller getController() {
		return controller;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public Annotation[][] getParameterAnnotations() {
		return new Annotation[0][0];
	}

	public String getName() {
		return null;
	}

	public void setInvokeResult(Object invokeResult) {
		this.invokeResult = invokeResult;
	}

	public Method getActionMethod() {
		return null;
	}

	@Override
	public String getFullName() {
		return null;
	}

	@Override
	public <T extends Annotation> T[] getAnnotation(Class<T> annotationClass) {
		return (T[]) Array.newInstance(annotationClass, 0);
	}

	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return false;
	}

}
