package org.sothis.dal;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class DaoFactoryBean implements MethodInterceptor, FactoryBean {

	private Class<?> daoClass;
	private JdbcTemplate jdbcTemplate;

	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		Type type = method.getGenericReturnType();
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type rawType = parameterizedType.getRawType();
			if (rawType instanceof Class
					&& List.class.isAssignableFrom((Class<?>) parameterizedType
							.getRawType())) {
				Class<?> entityClass = (Class<?>) parameterizedType
						.getActualTypeArguments()[0];
//				jdbcTemplate.
//				jdbcTemplate.set
			}
		}
		return null;
	}

	@Override
	public Object getObject() throws Exception {
		Enhancer e = new Enhancer();
		e.setSuperclass(daoClass);
		e.setCallback(this);
		return e.create();
	}

	@Override
	public Class<?> getObjectType() {
		return daoClass;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public void setDaoClass(Class<?> daoClass) {
		this.daoClass = daoClass;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
