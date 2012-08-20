package org.sothis.web.mvc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;

@Bean(scope=Scope.PROTOTYPE)
public class DefaultFlash implements Flash {
	private static final long serialVersionUID = -5188599456800029672L;
	private final Map<String, FlashValue> attributes = new HashMap<String, FlashValue>();

	public DefaultFlash() {
	}

	@Override
	public synchronized void flash() {
		for (Iterator<String> i = this.attributes.keySet().iterator(); i.hasNext();) {
			String key = i.next();
			FlashValue fv = this.attributes.get(key);
			if (!fv.flash()) {
				i.remove();
			}
		}
	}

	@Override
	public Object getAttribute(String key) {
		FlashValue fv = this.attributes.get(key);
		return null == fv ? null : fv.value;
	}

	@Override
	public Object setAttribute(String key, Object value) {
		return attributes.put(key, new FlashValue(value));
	}

	@Override
	public boolean containsAttribute(String key) {
		return attributes.containsKey(key);
	}

	@Override
	public Object removeAttribute(String key) {
		return attributes.remove(key);
	}

	@Override
	public Iterator<String> iterator() {
		return this.attributes.keySet().iterator();
	}

	private static class FlashValue implements Serializable {
		private static final long serialVersionUID = -7622561590903556883L;
		private final Object value;
		private int status = 0;

		public FlashValue(Object value) {
			this.value = value;
		}

		public boolean flash() {
			return ++status <= 1;
		}

	}

}
