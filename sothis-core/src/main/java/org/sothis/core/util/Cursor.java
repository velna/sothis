package org.sothis.core.util;

import java.util.List;

public interface Cursor<E> extends Iterable<E> {

	Cursor<E> batchSize(int batchSize);

	Cursor<E> limit(int limit);

	Cursor<E> skip(int skip);

	int count();

	List<E> toList();

	Cursor<E> sort(String field, boolean asc);
}
