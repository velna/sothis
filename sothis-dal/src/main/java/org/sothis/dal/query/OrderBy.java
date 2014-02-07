package org.sothis.dal.query;

import java.util.List;

/**
 * 排序条件
 * 
 * @author velna
 * 
 */
public interface OrderBy {

	/**
	 * 添加一个按{@code field}升序的排序条件
	 * 
	 * @param field
	 * @return
	 */
	OrderBy asc(String field);

	/**
	 * 添加一个按{@code field}降序的排序条件
	 * 
	 * @param field
	 * @return
	 */
	OrderBy desc(String field);

	/**
	 * 得到所有的排序条件
	 * 
	 * @return
	 */
	List<Sort> getSorts();

}
