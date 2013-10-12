package org.sothis.web.mvc;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * controller的简单封装
 * 
 * @author velna
 * 
 */
public interface Controller extends ActionBase {

	public final static String CONTROLLER_SUFFIX = "Controller";

	/**
	 * 得到这个controller中name为actionName的Action
	 * 
	 * @param actionName
	 * @return
	 */
	Action getAction(String actionName);

	/**
	 * 得到这个controller中所有的action
	 * 
	 * @return 所有Action的Map，Map的key为action名称
	 */
	Map<String, Action> getActions();

	/**
	 * 得到实际controller的类
	 * 
	 * @return
	 */
	Class<?> getControllerClass();

	/**
	 * 得到controller名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 得到controller所在的包，这个包是相对于sothis.controller.packages配置的包名<br>
	 * 比如sothis.controller.packages=com.my.app.controller，那么：<br>
	 * com.my.app.controller.MyController，返回空字符串<br>
	 * com.my.app.controller.user.LoginController，返回user<br>
	 * com.my.app.controller.user.task.TaskController，返回user/task
	 * 
	 * @return 相对的package名称
	 */
	String getPackageName();

	/**
	 * 得到controller的完整名称，包括package路径
	 * 比如sothis.controller.packages=com.my.app.controller，那么：<br>
	 * com.my.app.controller.MyController，返回/my/<br>
	 * com.my.app.controller.user.LoginController，返回/user/my/<br>
	 * com.my.app.controller.user.Controller，返回/user/<br>
	 * com.my.app.controller.user.task.TaskController，返回/user/task/my/
	 * 
	 * @return
	 */
	String getFullName();

	/**
	 * 指定的注解是否在controller class或controller package中出现<br>
	 * {@inheritDoc}
	 */
	boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);

	/**
	 * 得到在controller class或controller package中出现的注解<br>
	 * {@inheritDoc}
	 */
	<T extends Annotation> T[] getAnnotation(Class<T> annotationClass);

	/**
	 * 得到所有在controller class和controller package中声明的注解<br>
	 * {@inheritDoc}
	 */
	Annotation[] getAnnotations();

	/**
	 * 同 {@link Action#getAnnotations()}<br>
	 * {@inheritDoc}
	 */
	Annotation[] getDeclaredAnnotations();
}
