package org.sothis.web.example.controllers;

import org.sothis.web.example.models.HelloWorldModel;
import org.sothis.web.mvc.annotation.Param;

public class Controller {

	public Object indexAction(HelloWorldModel model, @Param(name="message") String message) {
		return model;
	}

}
