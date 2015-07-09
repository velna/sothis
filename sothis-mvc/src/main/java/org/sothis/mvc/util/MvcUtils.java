package org.sothis.mvc.util;

import org.sothis.core.util.StringUtils;
import org.sothis.mvc.ActionInvocation;

public class MvcUtils {

	/**
	 * <p>
	 * 根据ActionInvocation及提供的path，解析出对应的路径。
	 * </p>
	 * 
	 * <pre>
	 * MockActionInvocation invocation = new MockActionInvocation();
	 * Controller controller = new DefaultController(&quot;&quot;, &quot;test&quot;, TestController.class);
	 * invocation.setAction(controller.getAction(&quot;test&quot;));
	 * 
	 * MvcUtils.resolvePath(null, invocation)    = &quot;/test/test&quot;
	 * MvcUtils.resolvePath(&quot;&quot;, invocation)      = &quot;/test/test&quot;
	 * MvcUtils.resolvePath(&quot;abcd&quot;, invocation)  = &quot;/test/abcd&quot;
	 * MvcUtils.resolvePath(&quot;/abcd&quot;, invocation) = &quot;/abcd&quot;
	 * MvcUtils.resolvePath(&quot;/&quot;, invocation)     = &quot;/&quot;
	 * </pre>
	 * 
	 * @param path
	 * @param invocation
	 * @return
	 */
	public static String resolvePath(String path, ActionInvocation invocation) {
		if (StringUtils.isEmpty(path)) {
			return invocation.getAction().getFullName();
		} else {
			if (path.charAt(0) == '/') {
				return path;
			} else {
				return new StringBuilder().append(invocation.getAction().getController().getFullName()).append(path).toString();
			}
		}
	}

}
