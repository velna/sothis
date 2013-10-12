package org.sothis.mvc;

import java.util.Map;

/**
 * Action的返回值如果实现了这个接口，那么实际的model、viewType和viewParams都使用这个接口中定义的方法来获取，
 * 否则根据不同的view实现来决定<br>
 * 这个类的所有方法之所以不以getXXX命名，是为了不和bean的get方法混淆，以免影响部分程序的反射
 * 
 * @author velna
 * 
 */
public interface ModelAndView {

	/**
	 * 返回实际的model<br>
	 * 这个返回值最后会传给{@link View#render(Object, Map, ActionInvocation)}方法
	 * 
	 * @return
	 * @see View#render(Object, Map, ActionInvocation)
	 */
	Object model();

	/**
	 * 返回实际的viewType
	 * 
	 * @return
	 */
	String viewType();

	/**
	 * 返回实际的viewParams<br>
	 * 这个返回值最后会传给{@link View#render(Object, Map, ActionInvocation)}方法
	 * 
	 * @return
	 * @see View#render(Object, Map, ActionInvocation)
	 */
	Map<String, Object> viewParams();
}
