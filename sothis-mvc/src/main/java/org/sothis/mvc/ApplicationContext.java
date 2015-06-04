package org.sothis.mvc;

import java.util.Map;

import org.sothis.core.beans.BeanFactory;

public interface ApplicationContext {

	Configuration getConfiguration();

	BeanFactory getBeanFactory();

	boolean containsAction(Object key);

	Action getAction(Object key);

	Map<Object, Action> getActions();

	String getContextPath();

	Object getNativeContext();
}
