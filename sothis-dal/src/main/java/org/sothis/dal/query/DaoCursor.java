package org.sothis.dal.query;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.sothis.core.util.AbstractCursor;
import org.sothis.core.util.Pager;
import org.sothis.dal.Dao;
import org.sothis.dal.Entity;

public class DaoCursor<E extends Entity, K extends Serializable> extends AbstractCursor<E> {

	private final Cnd cnd;
	private final Chain chain;
	private List<E> result;
	private int count = -1;
	private int seen = 0;
	private Dao<E, K> dao;

	public DaoCursor(Dao<E, K> dao, Cnd cnd, Chain chain) {
		this.dao = dao;
		this.cnd = cnd;
		this.chain = chain;
	}

	@Override
	public Iterator<E> iterator() {
		return new CursorIterator();
	}

	@Override
	public int count() {
		if (count == -1) {
			count = dao.count(cnd);
		}
		return count;
	}

	private class CursorIterator implements Iterator<E> {
		private Iterator<E> i;

		@Override
		public boolean hasNext() {
			if (null != i && i.hasNext()) {
				return true;
			}
			if (seen < limit) {
				result = dao.find(cnd, Pager.make(skip + seen, Math.min(batchSize, limit - seen)), chain);
				seen += result.size();
				i = result.iterator();
			}
			return null != i && i.hasNext();
		}

		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return i.next();
		}

		@Override
		public void remove() {

		}

	}
}
