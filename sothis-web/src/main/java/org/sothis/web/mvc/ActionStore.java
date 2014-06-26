package org.sothis.web.mvc;

import java.util.Map;

public interface ActionStore {

	boolean containsAction(Object key);

	Action getAction(Object key);

	Map<Object, Action> getActions();
}
