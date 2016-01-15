package org.sothis.mvc.interceptors.param;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Action方法的参数注入注解<br>
 * 
 * <pre>
 * class UserController{
 *   public void loginAction(@Param(name="username") String username, @Param(name="password") password){
 *   	User user = dao.findByUsername(username);
 *   	if(user.password.equals(password)){
 *   		...
 *   	}else{
 *   		...
 *   	}
 *   }
 * }
 * </pre>
 * 
 * @author velna
 * 
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
	/**
	 * 请求中的参数名
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * 正则表达式匹配，只有匹配才会进行参数注入。对于{@link Date}类型则对应{@link SimpleDateFormat}的pattern
	 * 
	 * @return
	 */
	String pattern() default "";
}