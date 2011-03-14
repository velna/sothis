package org.sothis.dal;

public interface Query<E> extends Iterable<E> {
	Query<E> select(Field... fields);

	Query<E> select();

	Query<E> where(Field field, Operator op, Object value);

	Query<E> orderBy(Field field, boolean asc);

	Query<E> batchSize(int size);

	Query<E> limit(int limit);
}
