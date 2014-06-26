package org.sothis.web.mvc;

import java.util.HashMap;
import java.util.Map;

public class DefaultActionStore implements ActionStore {
	private final Map<Object, Action> actions = new HashMap<Object, Action>();

	public void setAction(Object key, Action action) {
		this.actions.put(key, action);
	}

	@Override
	public Action getAction(Object key) {
		return actions.get(key);
	}

	@Override
	public boolean containsAction(Object key) {
		return this.actions.containsKey(key);
	}

	@Override
	public Map<Object, Action> getActions() {
		return this.actions;
	}

}
