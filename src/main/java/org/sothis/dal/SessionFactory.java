package org.sothis.dal;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.beanutils.PropertyUtils;

public class SessionFactory implements MethodInterceptor {

	private final Connection connection;

	public SessionFactory() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager
				.getConnection(
						"jdbc:mysql://localhost:3306/wh365?useUnicode=true&characterEncoding=utf-8",
						"root", "root");
	}

	public <T> T get(Class<T> daoClass) {
		Enhancer e = new Enhancer();
		e.setSuperclass(daoClass);
		e.setCallback(this);
		return (T) e.create();
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		Statement statement = connection.createStatement();
		statement.setMaxRows(1);
		ResultSet result = statement
				.executeQuery("select id, email, password from member");
		Object ret = method.getReturnType().newInstance();
		while (result.next()) {
			PropertyUtils.setSimpleProperty(ret, "id", result.getLong("id"));
			PropertyUtils.setSimpleProperty(ret, "email",
					result.getString("email"));
			PropertyUtils.setSimpleProperty(ret, "password",
					result.getString("password"));
		}
		statement.close();
		return ret;
	}
}
