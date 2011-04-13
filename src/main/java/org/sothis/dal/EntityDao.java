package org.sothis.dal;

import java.io.Serializable;
import java.util.List;

import org.sothis.util.Cursor;
import org.sothis.util.Pager;

public interface EntityDao<E extends Serializable, K extends Serializable> {

	/**
	 * ����id���Ҷ���
	 * 
	 * @param id
	 * @return
	 */
	E findById(K id);

	/**
	 * ָ�뷽ʽ�������ж���
	 * 
	 * @return
	 */
	Cursor<E> findAll();

	/**
	 * ��ҳ��ʽ�������ж���
	 * 
	 * @return
	 */
	List<E> findAll(Pager pager);

	/**
	 * ����id�б��������ж��󣬷��ص��б����ݴ����idList����
	 * 
	 * @param idList
	 * @return
	 */
	List<E> findAllByIds(List<K> idList);

	/**
	 * �õ���������
	 * 
	 * @return
	 */
	long count();

	/**
	 * �����ݿ����һ������
	 * 
	 * @param entity
	 */
	void save(E entity);

	/**
	 * �����ݿ����һ������
	 * 
	 * @param entities
	 */
	void saveList(List<E> entities);

	/**
	 * ����idɾ��һ������
	 * 
	 * @param id
	 */
	void deleteById(K id);

	/**
	 * ����id�б�ɾ��һ������
	 * 
	 * @param idList
	 */
	void deleteByIds(List<K> idList);

	/**
	 * �õ���Ӧ��ʵ����
	 * 
	 * @return
	 */
	Class<E> getEntityClass();
}