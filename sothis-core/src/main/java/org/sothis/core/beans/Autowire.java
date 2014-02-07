package org.sothis.core.beans;

/**
 * Bean自动装配模式，同spring中的定义
 * 
 * @author velna
 * 
 */
public enum Autowire {
	/**
	 * 不自动装配
	 */
	NO,
	/**
	 * 根据bean名称自动装配
	 */
	BY_NAME,
	/**
	 * 根据类型自动装配
	 */
	BY_TYPE,
	/**
	 * 根据构造方法自动装配
	 */
	CONSTRUCTOR
}
