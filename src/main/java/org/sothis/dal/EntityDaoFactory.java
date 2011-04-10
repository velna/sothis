package org.sothis.dal;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.sothis.dal.annotation.EntityClass;
import org.sothis.dal.annotation.Select;
import org.springframework.jdbc.core.JdbcTemplate;

public class EntityDaoFactory implements MethodInterceptor {
	private JdbcTemplate jdbcTemplate;

	public <D> D getDao(Class<D> daoClass) {
		Enhancer e = new Enhancer();
		e.setSuperclass(daoClass);
		e.setCallback(this);
		return (D) e.create();
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		EntityClass aEntityClass = obj.getClass().getAnnotation(
				EntityClass.class);
		if (null == aEntityClass || null == aEntityClass.value()) {
			return method.invoke(obj, args);
		}
		EntityInfo entityInfo = getEntityInfo(aEntityClass.value());

		String methodName = method.getName();
		if (methodName.startsWith("find")) {
			return doFind(entityInfo, obj, method, args);
		} else if (methodName.startsWith("save")) {
			return doSave(entityInfo, obj, method, args);
		} else if (methodName.startsWith("delete")) {
			return doDelete(entityInfo, obj, method, args);
		} else if (methodName.startsWith("count")) {
			return doCount(entityInfo, obj, method, args);
		} else if (methodName.startsWith("update")) {
			return doUpdate(entityInfo, obj, method, args);
		} else if (methodName.equals("getEntityClass")) {
			return aEntityClass.value();
		} else {
			return method.invoke(obj, args);
		}
	}

	private EntityInfo getEntityInfo(Class<?> entityClass) {
		return new EntityInfo(entityClass);
	}

	private Object doFind(EntityInfo entityInfo, Object obj, Method method,
			Object[] args) {
		Select aSelect = method.getAnnotation(Select.class);
		return null;
	}

	private Object doSave(EntityInfo entityInfo, Object obj, Method method,
			Object[] args) {
		return null;
	}

	private Object doDelete(EntityInfo entityInfo, Object obj, Method method,
			Object[] args) {
		return null;
	}

	private Object doUpdate(EntityInfo entityInfo, Object obj, Method method,
			Object[] args) {
		return null;
	}

	private Object doCount(EntityInfo entityInfo, Object obj, Method method,
			Object[] args) {
		return null;
	}
}
