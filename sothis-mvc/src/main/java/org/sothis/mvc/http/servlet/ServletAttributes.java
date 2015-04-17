package org.sothis.mvc.http.servlet;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;

import org.sothis.mvc.Attributes;

public abstract class ServletAttributes implements Attributes {

	protected abstract Enumeration<String> getAttributeNames();

	@Override
	public Iterator<Entry<String, Object>> iterator() {
		return new AttributeIterator(getAttributeNames());
	}

	@Override
	public Iterator<String> names() {
		return new NamesIterator(getAttributeNames());
	}

	private class NamesIterator implements Iterator<String> {
		Enumeration<String> names;
		String current;

		public NamesIterator(Enumeration<String> names) {
			this.names = names;
		}

		@Override
		public boolean hasNext() {
			return names.hasMoreElements();
		}

		@Override
		public String next() {
			current = names.nextElement();
			return current;
		}

		@Override
		public void remove() {
			ServletAttributes.this.remove(current);
		}

	}

	private class AttributeIterator implements Iterator<Entry<String, Object>> {
		Enumeration<String> names;
		String current;

		public AttributeIterator(Enumeration<String> names) {
			this.names = names;
		}

		@Override
		public boolean hasNext() {
			return names.hasMoreElements();
		}

		@Override
		public Entry<String, Object> next() {
			current = names.nextElement();
			return new AttributeEntry(current);
		}

		@Override
		public void remove() {
			ServletAttributes.this.remove(current);
		}

	}

	private class AttributeEntry implements Entry<String, Object> {
		private final String name;
		private Object value;

		public AttributeEntry(String name) {
			super();
			this.name = name;
		}

		@Override
		public String getKey() {
			return name;
		}

		@Override
		public Object getValue() {
			if (null == value) {
				value = get(name);
			}
			return value;
		}

		@Override
		public Object setValue(Object value) {
			Object old = this.value;
			set(name, value);
			this.value = value;
			return old;
		}

	}

}
