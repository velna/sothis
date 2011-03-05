## sothis ##

a rails like java mvc framework

code example: ('/app' is your context path)
1) in web.xml:

	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>
			/WEB-INF/spring.xml
		</param-value>
	</context-param>

	<listener>
		<listener-class>
			org.springframework.web.context.ContextLoaderListener
		</listener-class>
	</listener>

	<filter>
		<filter-name>sothis</filter-name>
		<filter-class>com.velix.sothis.SothisFilter</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>sothis</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>


2) spring.xml:

	<bean class="com.velix.sothis.interceptor.ParametersInterceptor" />
	<bean class="com.velix.sothis.interceptor.FileUploadInterceptor" />
	<bean class="com.velix.sothis.view.DefaultModelAndViewResolver" />

	<bean class="com.velix.thinkapp.controller.HelloController" scope="prototype">
		<property name="message" value="hello world~~" />
	</bean>
	<bean class="com.velix.thinkapp.controller.Controller" scope="prototype">


3) add sothis.properties in classpath root:

	sothis.controller.packages=com.velix.thinkapp.controller

	sothis.beanFactory.class=com.velix.sothis.spring.SpringBeanFactory

	sothis.interceptor.params.class=com.velix.sothis.interceptor.ParametersInterceptor
	sothis.interceptor.upload.class=com.velix.sothis.interceptor.FileUploadInterceptor

	sothis.interceptor.stack.default=upload,params

	sothis.view.default=com.velix.sothis.view.JspView

	sothis.viewResolver.class=com.velix.sothis.view.DefaultModelAndViewResolver


4) write your controllers:

default controller:

	package com.velix.thinkapp.controller;

	public class Controller {
		public void indexAction() {
		}
	}

this controller does nothing, but now you can make request '/app/index' or just '/app/'

5) index.jsp

	<%@ page contentType="text/html; charset=UTF-8"%>
	<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> 
	</head>
	<body>
	<h2>Hello World!</h2>
	<form action="hello/say" method="post" enctype="multipart/form-data">
	<input type="file" name="myFile"/>
	<input type="file" name="myFile"/>
	<input name="message" value="some message"/>
	<input name="date" value="2011-03-06"/>
	<input type="submit"/>
	</form>
	</body>
	</html>

6) HelloController:

	package com.velix.thinkapp.controller;

	import java.io.File;
	import java.util.Date;
	import java.util.List;

	import com.velix.sothis.annotation.Parameter;
	import com.velix.thinkapp.model.HomePageModel;

	public class HelloController {
		private String message;

		public void sayAction(HomePageModel model,
				@Parameter(name = "myFile") List<File> someFiles,
				@Parameter(name = "date", pattern = "yyyy-MM-dd") Date date) {
			System.out.println(model.getMyFile());
			System.out.println(model.getMyFileContentType());
			System.out.println(model.getMyFileFileName());
			System.out.println(model.getMessage());
			System.out.println(model.getStatus());
			System.out.println(message);
			for (File f : someFiles) {
				System.out.println("someFiles:" + f);
			}
			System.out.println("date:" + date);
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

7) /hello/say.jsp:

	I said "hello" !

That's all, now you can access http://localhost:8080/app/ , choose some file, and sumbit, you'll see the results from the console