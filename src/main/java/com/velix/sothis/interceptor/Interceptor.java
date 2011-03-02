package com.velix.sothis.interceptor;

import com.velix.sothis.ActionInvocation;

public interface Interceptor {
	Object intercept(ActionInvocation invocation) throws Exception;
}
