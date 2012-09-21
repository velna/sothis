package org.sothis.core.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在类声明上注解，定义这个类的bean属性
 * 
 * @author velna
 * 
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
	/**
	 * bean作用域，默认是单例
	 * 
	 * @return
	 */
	Scope scope() default Scope.SINGLETON;

	/**
	 * bean自动装配模式，默认是根据名称自动装配(BY_NAME)
	 * 
	 * @return
	 */
	Autowire autowire() default Autowire.BY_NAME;

	/**
	 * bean实例化后的初始化方法的名称
	 * 
	 * @return
	 */
	String initMethod() default "";
}
