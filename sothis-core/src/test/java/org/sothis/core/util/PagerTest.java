package org.sothis.core.util;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 测试Pager类
 * 
 * @author liupei
 */
public class PagerTest {
	/**
	 * 测试无参构造函数
	 */
	@Test
	public void testPagerNoArg() {
		Pager p = new Pager();
		Assert.assertEquals(p.getCurrentPage(), 1);
		Assert.assertEquals(p.getPageSize(), 1);
		Assert.assertEquals(p.getStartRow(), 0);
		Assert.assertEquals(p.getTotalPages(), 0);
		Assert.assertEquals(p.getTotalRows(), 0);
	}

	/**
	 * 测试有参构造函数
	 */
	@Test
	public void testPagerWithArg() {
		Pager p = new Pager(5);
		Assert.assertEquals(p.getCurrentPage(), 1);
		Assert.assertEquals(p.getPageSize(), 5);
		Assert.assertEquals(p.getStartRow(), 0);
		Assert.assertEquals(p.getTotalPages(), 0);
		Assert.assertEquals(p.getTotalRows(), 0);
	}

	/**
	 * 测试各种set方法
	 */
	@Test
	public void testSets() {
		Pager p = new Pager();
		p.setCurrentPage(2);
		p.setPageSize(5);
		p.setStartRow(6);
		p.setTotalRows(8);
		Assert.assertEquals(p.getCurrentPage(), 2);
		Assert.assertEquals(p.getPageSize(), 5);
		Assert.assertEquals(p.getStartRow(), 6);
		Assert.assertEquals(p.getTotalPages(), 2);
		Assert.assertEquals(p.getTotalRows(), 8);
	}
}
