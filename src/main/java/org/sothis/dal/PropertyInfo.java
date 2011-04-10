package org.sothis.dal;

import java.beans.PropertyDescriptor;

import org.sothis.dal.annotation.Column;

/**
 * 实体类字段属性信息
 * 
 * @author velna
 * 
 */
public class PropertyInfo {

	private final PropertyDescriptor propertyDescriptor;
	private final Column column;
	private final Class<?> clazz;
	private final boolean id;

	public PropertyInfo(PropertyDescriptor propertyDescriptor, Column column,
			Class<?> clazz, boolean id) {
		if (!column.name().toLowerCase().equals(column.name())) {
			throw new IllegalArgumentException("name of column ["
					+ propertyDescriptor.getName()
					+ "] must be lower cased of class " + clazz.getName()
					+ ", current is [" + column.name() + "]");
		}
		this.propertyDescriptor = propertyDescriptor;
		this.column = column;
		this.clazz = clazz;
		this.id = id;
	}

	/**
	 * 得到字段的描述信息
	 * 
	 * @return
	 */
	public PropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	/**
	 * 得到Column注解
	 * 
	 * @return
	 */
	public Column getColumn() {
		return column;
	}

	/**
	 * 是否为id字段
	 * 
	 * @return
	 */
	public boolean isID() {
		return id;
	}

	/**
	 * 得到所属的实体类
	 * 
	 * @return
	 */
	public Class<?> getClazz() {
		return clazz;
	}

}
