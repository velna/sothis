package org.sothis.web.mvc.views;

import java.util.Collections;
import java.util.Map;

import org.sothis.web.mvc.View;

public abstract class AbstractView implements View {

	protected Map<String, Object> params = Collections.emptyMap();

	@Override
	public void setParams(Map<String, Object> params) {
		if (null != params) {
			this.params = params;
		}
	}

}
