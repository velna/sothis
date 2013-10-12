package org.sothis.mvc;

import org.sothis.core.beans.BeanFactory;

public interface ApplicationContext {

	Configuration getConfiguration();

	BeanFactory getBeanFactory();

	boolean containsAction(Object key);

	Action getAction(Object key);
}
