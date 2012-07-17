package org.sothis.web.mvc;

import java.util.Collections;
import java.util.Map;

/**
 * ¿ÕµÄcontroller
 * 
 * @author velna
 * 
 */
public class EmptyController extends AbstractActionBase implements Controller {

	private final String name;

	public EmptyController(final String name) {
		if (null == name) {
			throw new IllegalArgumentException("name is null");
		}
		this.name = name;
	}

	public Action getAction(final String actionName) {
		return new EmptyAction(actionName, this);
	}

	public Class<?> getControllerClass() {
		return this.getClass();
	}

	public String getName() {
		return name;
	}

	public String getFullName() {
		return null;
	}

	public Map<String, Action> getActions() {
		return Collections.emptyMap();
	}

	public String getPackageName() {
		return "";
	}

}
