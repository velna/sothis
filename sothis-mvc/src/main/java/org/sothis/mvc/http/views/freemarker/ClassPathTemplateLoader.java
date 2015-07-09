package org.sothis.mvc.http.views.freemarker;

import java.net.URL;

import freemarker.cache.URLTemplateLoader;

public class ClassPathTemplateLoader extends URLTemplateLoader {

	@Override
	protected URL getURL(String name) {
		return this.getClass().getClassLoader().getResource(name);
	}

}