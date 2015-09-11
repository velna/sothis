package org.sothis.mvc.controllers;

import org.sothis.mvc.Request;
import org.sothis.mvc.Response;

public class Controller {
	public Object indexAction(Request req, Response resp) throws Exception {
		return req.parameters().getString("message");
	}
}
