package org.sothis.core.beans;

/**
 * bean的作用域，同spring中的定义
 * 
 * @author velna
 * 
 */
public enum Scope {
	/**
	 * 默认作用域
	 */
	DEFAULT,
	/**
	 * 多例
	 */
	PROTOTYPE,
	/**
	 * 单例
	 */
	SINGLETON
}
