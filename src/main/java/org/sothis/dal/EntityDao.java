package org.sothis.dal;

import java.io.Serializable;
import java.util.List;

public interface EntityDao<E extends Serializable, K extends Serializable> {

	/**
	 * 根据id查找对象
	 * 
	 * @param id
	 * @return
	 */
	E findById(K id);

	/**
	 * 查找所有对象
	 * 
	 * @return
	 */
	List<E> findAll();

	/**
	 * 根据id列表查找所有对象，返回的列表根据传入的idList排序
	 * 
	 * @param idList
	 * @return
	 */
	List<E> findAllByIds(List<K> idList);

	/**
	 * 得到所有数量
	 * 
	 * @return
	 */
	long count();

	/**
	 * 向数据库插入一条数据
	 * 
	 * @param entity
	 */
	void save(E entity);

	/**
	 * 向数据库插入一批数据
	 * 
	 * @param entities
	 */
	void saveList(List<E> entities);

	/**
	 * 根据id删除一条数据
	 * 
	 * @param id
	 */
	void deleteById(K id);

	/**
	 * 根据id列表删除一批数据
	 * 
	 * @param idList
	 */
	void deleteByIds(List<K> idList);

	/**
	 * 得到对应的实体类
	 * 
	 * @return
	 */
	Class<E> getEntityClass();
}