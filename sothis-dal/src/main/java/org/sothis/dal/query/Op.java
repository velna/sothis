package org.sothis.dal.query;

/**
 * 运算符
 * 
 * @author velna
 * 
 */
public enum Op {
	/**
	 * 大于
	 */
	GT,
	/**
	 * 大于等于
	 */
	GTE,
	/**
	 * 小于
	 */
	LT,
	/**
	 * 小于等于
	 */
	LTE,
	/**
	 * 等于
	 */
	EQ,
	/**
	 * 不等于
	 */
	NE,
	/**
	 * 集合in
	 */
	IN,
	/**
	 * 集合not in
	 */
	NIN,
	/**
	 * 模糊匹配
	 */
	LIKE
}
