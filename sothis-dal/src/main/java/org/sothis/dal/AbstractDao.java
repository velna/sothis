package org.sothis.dal;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.sothis.core.util.ExecuteCounter;
import org.sothis.core.util.LoggerFactory;
import org.sothis.core.util.Pager;
import org.sothis.dal.query.Chain;
import org.sothis.dal.query.Cnd;

/**
 * Dao的虚基类，已经通过反射得到了实际的实体类。并提供了一些常用方法。
 * 
 * @author velna
 * 
 * @param <E>
 * @param <K>
 */
public abstract class AbstractDao<E extends Entity, K extends Serializable> implements Dao<E, K> {

	/**
	 * 用来记录性能日志数据的ThreadLocal key。
	 * 
	 * @see {@link ExecuteCounter}
	 */
	public final static String EXECUTE_COUNTER_KEY = "org.sothis.dal.EntityDao.EXECUTE_COUNTER_KEY";
	private final Logger PERFORMANCE_LOGGER = LoggerFactory.getPerformanceLogger(this.getClass());
	private final long MAX_EXECUTE_TIME = 100;

	protected final Class<E> entityClass;

	@SuppressWarnings("unchecked")
	public AbstractDao() {
		Type type = this.getClass().getGenericSuperclass();
		if (null == type || !(type instanceof ParameterizedType)) {
			throw new RuntimeException("no entity class defined of " + this.getClass().getName());
		}
		ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		Type[] ts = parameterizedType.getActualTypeArguments();
		if (null == ts || ts.length == 0) {
			throw new RuntimeException("no entity class defined of " + this.getClass().getName());
		}
		entityClass = (Class<E>) ts[0];
	}

	/**
	 * 对当前线程的数据库访问进行计数，并记录花费的时间
	 * 
	 * @param operation
	 *            操作类型
	 * @param time
	 *            花费时间，单位：毫秒
	 */
	protected final void increaseExecuteCounter(String operation, long time, Object... queryParams) {
		ExecuteCounter.getThreadLocalInstance(EXECUTE_COUNTER_KEY).increase(entityClass.getSimpleName(), operation, time);
		if (time > MAX_EXECUTE_TIME) {
			StringBuilder builder = new StringBuilder();
			builder.append("\ttype:performance\ttime:").append(time);
			builder.append("\tentity:").append(entityClass.getSimpleName());
			builder.append("\toperation:").append(operation);
			builder.append("\tquery:");
			if (null != queryParams) {
				builder.append('[');
				for (int j = 0; j < queryParams.length; j++) {
					Object param = queryParams[j];
					if (null != param && param.getClass().isArray()) {
						int length = Array.getLength(param);
						builder.append('[');
						for (int i = 0; i < length; i++) {
							builder.append(Array.get(param, i));
							if (i < length - 1) {
								builder.append(',');
							}
						}
						builder.append(']');
					} else {
						builder.append(param);
					}
					if (j < queryParams.length - 1) {
						builder.append(',');
					}
				}
				builder.append(']');
			}
			if (time < MAX_EXECUTE_TIME * 5) {
				PERFORMANCE_LOGGER.warn(builder.toString());
			} else {
				PERFORMANCE_LOGGER.error(builder.toString());
			}
		}
	}

	@Override
	public List<E> find(Cnd cnd, Pager pager) {
		return find(cnd, pager, null);
	}

	@Override
	public List<E> find(Cnd cnd) {
		return find(cnd, null, null);
	}

	@Override
	public E findOne(Cnd cnd, Chain chain) {
		List<E> list = find(cnd, Pager.make(0, 1), null);
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public E findOne(Cnd cnd) {
		return findOne(cnd, null);
	}

	@Override
	public int count() {
		return count(null);
	}

	@Override
	public Class<E> getEntityClass() {
		return entityClass;
	}

	@Override
	public List<E> findAndCount(Cnd cnd, Pager pager, Chain chain) {
		List<E> list = this.find(cnd, pager, chain);
		pager.setTotalRows(this.count(cnd));
		return list;
	}

	@Override
	public List<E> findAndCount(Cnd cnd, Pager pager) {
		List<E> list = this.find(cnd, pager, null);
		pager.setTotalRows(this.count(cnd));
		return list;
	}

}
