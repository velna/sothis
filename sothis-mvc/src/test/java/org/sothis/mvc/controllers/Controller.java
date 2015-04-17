package org.sothis.mvc.controllers;

import org.sothis.mvc.http.HttpRequest;
import org.sothis.mvc.http.HttpResponse;

public class Controller {
	public Object indexAction(HttpRequest req, HttpResponse resp) throws Exception {
		return req.parameters().getString("message");
	}
}
