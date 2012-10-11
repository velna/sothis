package org.sothis.dal;

import java.io.Serializable;
import java.util.List;

import org.sothis.core.util.Pager;
import org.sothis.dal.query.Chain;
import org.sothis.dal.query.Cnd;

public interface Dao<E extends Entity, K extends Serializable> {
	public final static String EXECUTE_COUNTER_KEY = "org.sothis.dal.EntityDao.EXECUTE_COUNTER_KEY";

	Class<E> getEntityClass();

	List<E> find(Cnd cnd, Pager pager, Chain chain);

	List<E> find(Cnd cnd, Pager pager);

	List<E> find(Cnd cnd);

	E findOne(Cnd cnd, Chain chain);

	E findOne(Cnd cnd);

	/**
	 * 根据id查找对象
	 * 
	 * @param id
	 * @return
	 */
	E findById(K id);

	/**
	 * 根据id列表查找所有对象，返回的列表根据传入的idList排序
	 * 
	 * @param idList
	 * @return
	 */
	List<E> findByIds(List<K> idList);

	int update(Cnd cnd, Chain chain);

	/**
	 * 根据id更新数据
	 * 
	 * @param data
	 *            需要更新的字段名和字段值，key为字段名，value为字段值
	 * @param id
	 */
	int updateById(K id, Chain chain);

	E update(E entity);

	int delete(Cnd cnd);

	/**
	 * 根据id删除一条数据
	 * 
	 * @param id
	 */
	int deleteById(K id);

	/**
	 * 根据id列表删除一批数据
	 * 
	 * @param idList
	 */
	int deleteByIds(List<K> idList);

	E insert(E entity);

	int count();

	int count(Cnd cnd);
}
