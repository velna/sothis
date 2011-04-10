package org.sothis.dal;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.sothis.dal.annotation.Column;
import org.sothis.dal.annotation.GeneratedValue;
import org.sothis.dal.annotation.Id;
import org.sothis.dal.annotation.Table;

public class EntityInfo {
	private final String tableName;
	private final Map<String, PropertyInfo> fieldsMap;
	private String idColumnName;
	private boolean idGeneratedValue;
	private final Class<?> entityClass;

	public EntityInfo(Class<?> entityClass) {
		this.entityClass = entityClass;
		// table annotation
		Table table = this.getEntityClass().getAnnotation(Table.class);
		if (null == table) {
			throw new RuntimeException(
					"no Table annotation found of entity class "
							+ this.getEntityClass().getName());
		}
		tableName = table.name();
		if (StringUtils.isEmpty(tableName)) {
			throw new RuntimeException(
					"name of Table annotation is empty of entity class "
							+ this.getEntityClass().getName());
		}
		if (!tableName.toLowerCase().equals(tableName)) {
			throw new IllegalArgumentException("table name of class  ["
					+ this.getEntityClass().getName()
					+ "] must be lower cased, current is [" + tableName + "]");
		}

		// fields annotation
		PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(this.getEntityClass());
		Map<String, PropertyInfo> _fieldsMap = new HashMap<String, PropertyInfo>(
				propertyDescriptors.length);
		boolean idFind = false;
		for (PropertyDescriptor descriptor : propertyDescriptors) {
			Method readMethod = descriptor.getReadMethod();
			if (null != readMethod) {
				Column column = readMethod.getAnnotation(Column.class);
				if (null == column) {
					continue;
				}
				Id id = readMethod.getAnnotation(Id.class);
				PropertyInfo info = new PropertyInfo(descriptor, column,
						this.getEntityClass(), id != null);
				if (null != id) {
					if (idFind) {
						throw new RuntimeException(
								"multi Id annotation found of entity class "
										+ this.getEntityClass().getName());
					}
					idFind = true;
					idColumnName = descriptor.getName();
					GeneratedValue generatedValue = readMethod
							.getAnnotation(GeneratedValue.class);
					idGeneratedValue = generatedValue != null;
				}
				_fieldsMap.put(descriptor.getName(), info);
			}
		}
		fieldsMap = Collections.unmodifiableMap(_fieldsMap);
	}

	public String getIdColumnName() {
		return idColumnName;
	}

	public void setIdColumnName(String idColumnName) {
		this.idColumnName = idColumnName;
	}

	public boolean isIdGeneratedValue() {
		return idGeneratedValue;
	}

	public void setIdGeneratedValue(boolean idGeneratedValue) {
		this.idGeneratedValue = idGeneratedValue;
	}

	public String getTableName() {
		return tableName;
	}

	public Map<String, PropertyInfo> getFieldsMap() {
		return fieldsMap;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}
}
