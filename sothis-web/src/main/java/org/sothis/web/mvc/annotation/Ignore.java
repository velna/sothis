package org.sothis.web.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 如果注解在set方法上，则对应的request参数不会被set<br>
 * 如果注解在action方法上，则该action方法不会被注册到sothis中<br>
 * 如果注解在controller类上，则该controller中的所有action都不会被注册在sothis中<br>
 * 如果注解在package上，则该package中的所有controller都不会被注册在sothis中<br>
 * 
 * @author velna
 * 
 */
@Target( { ElementType.METHOD, ElementType.TYPE, ElementType.PACKAGE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {

}
