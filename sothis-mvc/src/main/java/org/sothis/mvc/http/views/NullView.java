package org.sothis.mvc.http.views;

import java.io.IOException;

import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ModelAndView;
import org.sothis.mvc.View;
import org.sothis.mvc.ViewRenderException;

@Bean(scope = Scope.SINGLETON)
public class NullView implements View {

	@Override
	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ViewRenderException {

	}

}
