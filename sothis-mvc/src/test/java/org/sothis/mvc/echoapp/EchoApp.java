package org.sothis.mvc.echoapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import org.sothis.core.beans.BeanFactory;
import org.sothis.core.beans.SimpleBeanFactory;
import org.sothis.mvc.ActionInvocationHelper;
import org.sothis.mvc.ApplicationContext;
import org.sothis.mvc.Configuration;
import org.sothis.mvc.DefaultApplicationContext;

public class EchoApp {

	private final ApplicationContext applicationContext;

	public EchoApp(ApplicationContext applicationContext) {
		super();
		this.applicationContext = applicationContext;
	}

	public void start() throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String line = reader.readLine();
			if (null != line) {
				ActionInvocationHelper.invoke(applicationContext, new EchoAppRequest("/", line), new EchoAppResponse());
			} else {
				break;
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Properties properties = new Properties();
		properties.put("sothis.controller.packages", "org.sothis.mvc.controllers");
		properties.put("sothis.interceptors.echoapp.class", "org.sothis.mvc.EchoAppInterceptor");
		properties.put("sothis.interceptors.stack.default", "echoapp");
		Configuration config = new Configuration(properties);

		BeanFactory beanFactory = new SimpleBeanFactory();

		ApplicationContext applicationContext = new DefaultApplicationContext(beanFactory, config);
		new EchoApp(applicationContext).start();
	}
}
