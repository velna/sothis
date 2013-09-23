package org.sothis.core.util;

public interface Cursor<E> extends Iterable<E> {

	Cursor<E> batchSize(int batchSize);

	Cursor<E> limit(int limit);

	Cursor<E> skip(int skip);

	int count();

}
