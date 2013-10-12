package org.sothis.web.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MockAction extends AbstractActionBase implements Action {

	private Object invokeResult;
	private Controller controller;
	private Class<?>[] parameterTypes;

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
		return null;
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return new Annotation[0];
	}

	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return false;
	}

}
