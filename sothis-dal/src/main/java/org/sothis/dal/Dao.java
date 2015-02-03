package org.sothis.dal;

import java.io.Serializable;
import java.util.List;

import org.sothis.core.util.Cursor;
import org.sothis.core.util.Pager;
import org.sothis.dal.query.Chain;
import org.sothis.dal.query.Cnd;

/**
 * 数据库访问接口。定义了数据库访问的常用方法。
 * 
 * @author velna
 * 
 * @param <E>
 * @param <K>
 */
public interface Dao<E extends Entity, K extends Serializable> {

	/**
	 * 返回本DAO对应的数据库实体类
	 * 
	 * @return
	 */
	Class<E> getEntityClass();

	/**
	 * 根据条件查询数据库，并返回一个列表。<br>
	 * {@code dao.find(Cnd.make("age", 22).asc("id"), Pager.make(0, 10), Chain.make("username").add("gender"));}
	 * <br>
	 * 相当于select username, gender from mytable where age=22 order by id asc
	 * limit 0, 10
	 * 
	 * @param cnd
	 *            查询条件
	 * @param pager
	 *            分页，如果为null则返回所有数据记录
	 * @param fields
	 *            需要返回的字段集，如果为null则查询所有字段
	 * @return
	 */
	List<E> find(Cnd cnd, Pager pager, Chain fields);

	/**
	 * 相当于{@code find(cnd, pager, fields)}<br>
	 * {@code pager.setTotalRows(count(cnd))}
	 * 
	 * @param cnd
	 * @param pager
	 * @param fields
	 * @return
	 */
	List<E> findAndCount(Cnd cnd, Pager pager, Chain fields);

	/**
	 * 相当于{@code find(cnd, pager, null)}<br>
	 * {@code pager.setTotalRows(count(cnd))}
	 * 
	 * @param cnd
	 * @param pager
	 * @return
	 */
	List<E> findAndCount(Cnd cnd, Pager pager);

	/**
	 * 相当于{@code find(cnd, pager, null)}
	 * 
	 * @param cnd
	 * @param pager
	 * @return
	 */
	List<E> find(Cnd cnd, Pager pager);

	/**
	 * 相当于{@code find(cnd, null, null)}
	 * 
	 * @param cnd
	 * @return
	 */
	List<E> find(Cnd cnd);

	/**
	 * 以迭代方式查询。
	 * 
	 * @param cnd
	 * @param fields
	 * @return
	 */
	Cursor<E> cursor(Cnd cnd, Chain fields);

	/**
	 * 相当于{@code find(cnd, Pager.make(0,1), fields)}
	 * 
	 * @param cnd
	 * @param fields
	 * @return
	 */
	E findOne(Cnd cnd, Chain fields);

	/**
	 * 相当于{@code find(cnd, Pager.make(0,1), null)}
	 * 
	 * @param cnd
	 * @return
	 */
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

	/**
	 * 根据查询条件{@code cnd}更新数据库。<br>
	 * {@code dao.update(Cnd.make("age", 24), Chain.make("age", 23));}<br>
	 * 相当于：update mytable set age=23 where age=24
	 * 
	 * @param cnd
	 * @param update
	 * @return 受影响的记录数
	 */
	int update(Cnd cnd, Chain update);

	/**
	 * 根据id更新数据<br>
	 * 
	 * @param id
	 * @param update
	 * @return
	 */
	int updateById(K id, Chain update);

	/**
	 * 根据主键将实体类中的所有字段更新入数据库中。
	 * 
	 * @param entity
	 * @return 受影响的记录数
	 */
	E update(E entity);

	/**
	 * 根据查询条件删除记录。
	 * 
	 * @param cnd
	 * @return 受影响的记录数
	 */
	int delete(Cnd cnd);

	/**
	 * 根据id删除一条数据
	 * 
	 * @param id
	 * @return 受影响的记录数
	 */
	int deleteById(K id);

	/**
	 * 根据id列表删除一批数据
	 * 
	 * @param idList
	 * @return 受影响的记录数
	 */
	int deleteByIds(List<K> idList);

	/**
	 * 插入一条记录。
	 * 
	 * @param entity
	 * @return
	 */
	E insert(E entity);

	/**
	 * 批量插入
	 * 
	 * @param entityList
	 * @return
	 */
	List<E> insert(List<E> entityList);

	/**
	 * 得到数据表的所有记录条数。
	 * 
	 * @return
	 */
	int count();

	/**
	 * 根据查询条件得到满足该条件的记录数。
	 * 
	 * @param cnd
	 * @return
	 */
	int count(Cnd cnd);
}
