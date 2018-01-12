package org.sothis.mvc.controllers;

import org.sothis.mvc.echoapp.BaseModel;

public abstract class BaseController<M extends BaseModel> {
	public Object indexAction(M model) {
		return model.getClass().toString();
	}
}
