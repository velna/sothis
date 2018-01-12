package org.sothis.mvc;

import java.util.Iterator;

import org.apache.commons.collections.iterators.EmptyIterator;

public abstract class ReadOnlyHeaders extends Headers {

	public static final Headers EMPTY = new ReadOnlyHeaders() {

		@SuppressWarnings("unchecked")
		@Override
		public Iterator<String> names() {
			return EmptyIterator.INSTANCE;
		}

		@Override
		public String[] getStrings(String name) {
			return null;
		}

		@Override
		public boolean contains(String name) {
			return false;
		}

	};

	@Override
	public void addString(String name, String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setString(String name, String value) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void remove(String name) {
		throw new UnsupportedOperationException();
	}

}
