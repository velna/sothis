package org.sothis.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 对Action方法的简单封装
 * 
 * @author velna
 * 
 */
public interface Action extends ActionBase {

	public final static String ACTION_SUFFIX = "Action";

	/**
	 * 
	 * @return
	 */
	Method getActionMethod();

	/**
	 * 得到这个Action对应的Controller对象
	 * 
	 * @return
	 */
	Controller getController();

	/**
	 * 得到Action名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 得到Action的完整名称，包括package和controller路径
	 * 
	 * @return
	 */
	String getFullName();

	/**
	 * 指定的注解是否在action method、controller class或controller package中出现<br>
	 * {@inheritDoc}
	 */
	boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);

	/**
	 * 得到在action method或controller class或controller package中出现的注解<br>
	 * {@inheritDoc}
	 */
	<T extends Annotation> T[] getAnnotation(Class<T> annotationClass);

	/**
	 * 得到所有在action method、controller class和controller package中声明的注解<br>
	 * {@inheritDoc}
	 */
	Annotation[] getAnnotations();

	/**
	 * 同 {@link Action#getAnnotations()}<br>
	 * {@inheritDoc}
	 */
	Annotation[] getDeclaredAnnotations();
}
