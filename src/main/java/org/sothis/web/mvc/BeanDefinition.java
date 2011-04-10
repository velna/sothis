package org.sothis.web.mvc;

public class BeanDefinition {
	private boolean singleton;

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}
}
