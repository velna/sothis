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
		<filter-class>org.sothis.web.mvc.SothisFilter</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>sothis</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>


2) spring.xml:

	nothing is must :)


3) put sothis.properties at classpath root:

	sothis.controller.packages=com.myapp.controller

	sothis.beanFactory.class=org.sothis.web.mvc.support.SpringBeanFactory

	sothis.interceptor.params.class=org.sothis.web.mvc.interceptors.ParametersInterceptor
	sothis.interceptor.upload.class=org.sothis.web.mvc.interceptors.FileUploadInterceptor

	sothis.interceptor.stack.default=upload,params

	sothis.view.default=org.sothis.web.mvc.views.JspView

	sothis.viewResolver.class=org.sothis.web.mvc.DefaultModelAndViewResolver


4) default controller:

	package com.myaqpp.controller;

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

	package com.myapp.controller;

	import java.io.File;
	import java.util.Date;
	import java.util.List;

	import org.sothis.web.mvc.annotation.Param;
	import com.myapp.model.HomePageModel;

	public class HelloController {
		private String message;

		public void sayAction(HomePageModel model,
				@Param(name = "myFile") List<File> someFiles,
				@Param(name = "date", pattern = "yyyy-MM-dd") Date date) {
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


7) HomePageModel.java:

	package com.myapp.model;

	import java.io.File;

	public class HomePageModel {
		private String message;
		private int status;
		private File myFile;
		private String myFileFileName;
		private String myFileContentType;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public File getMyFile() {
			return myFile;
		}

		public void setMyFile(File myFile) {
			this.myFile = myFile;
		}

		public String getMyFileFileName() {
			return myFileFileName;
		}

		public void setMyFileFileName(String myFileFileName) {
			this.myFileFileName = myFileFileName;
		}

		public String getMyFileContentType() {
			return myFileContentType;
		}

		public void setMyFileContentType(String myFileContentType) {
			this.myFileContentType = myFileContentType;
		}
	}


8) /hello/say.jsp:

	I said "hello" !


That's all, now you can access http://localhost:8080/app/ , choose some file, and sumbit, you'll see the results from the console