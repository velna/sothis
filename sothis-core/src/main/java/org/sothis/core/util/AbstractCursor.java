package org.sothis.core.util;

public abstract class AbstractCursor<E> implements Cursor<E> {
	protected int batchSize;
	protected int limit;
	protected int skip;

	@Override
	public Cursor<E> batchSize(int batchSize) {
		this.batchSize = batchSize;
		return this;
	}

	@Override
	public Cursor<E> limit(int limit) {
		this.limit = limit;
		return this;
	}

	@Override
	public Cursor<E> skip(int skip) {
		this.skip = skip;
		return this;
	}

}
