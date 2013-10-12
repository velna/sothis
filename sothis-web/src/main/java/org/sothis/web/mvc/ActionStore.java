package org.sothis.web.mvc;

public interface ActionStore {

	boolean containsAction(Object key);

	Action getAction(Object key);
}
