package org.sothis.web.mvc;

import java.io.Serializable;

/**
 * 存在Flash中的对象生命周期只有一次flash，在第二次flash后被删除<br>
 * 在sothis中，每一次http请求相当于一次flash
 * 
 * @author velna
 * 
 */
public interface Flash extends Serializable, Iterable<String> {
	Object setAttribute(String key, Object value);

	Object getAttribute(String key);

	Object removeAttribute(String key);

	boolean containsAttribute(String key);

	/**
	 * 执行一次flash
	 */
	void flash();
}
