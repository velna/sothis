package org.sothis.web.mvc.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.sothis.web.mvc.ActionContext;
import org.sothis.web.mvc.ActionInvocation;
import org.sothis.web.mvc.Flash;

/**
 * Mvc工具类
 */
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

	@SuppressWarnings("unchecked")
	public static void addRequestModels(HttpServletRequest request, Map attrMap) {
		// request params
		attrMap.put("params", request.getParameterMap());

		// request attributes
		Enumeration<String> enumeration = request.getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String attrName = enumeration.nextElement();
			attrMap.put(attrName, request.getAttribute(attrName));
		}

		// session attributes
		HttpSession session = request.getSession(false);
		if (null != session) {
			Map<String, Object> sessionMap = new HashMap<String, Object>();
			enumeration = session.getAttributeNames();
			while (enumeration.hasMoreElements()) {
				String attrName = enumeration.nextElement();
				sessionMap.put(attrName, session.getAttribute(attrName));
			}
			attrMap.put("session", sessionMap);

			// flash attributes
			Flash flash = (Flash) session.getAttribute(ActionContext.FLASH);
			if (null != flash) {
				Map<String, Object> flashMap = new HashMap<String, Object>();
				for (String key : flash) {
					flashMap.put(key, flash.getAttribute(key));
				}
				attrMap.put("flash", flashMap);
			} else {
				attrMap.put("flash", Collections.EMPTY_MAP);
			}
		} else {
			attrMap.put("flash", Collections.EMPTY_MAP);
			attrMap.put("session", Collections.EMPTY_MAP);
		}

	}
}
