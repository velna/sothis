package org.sothis.mvc.http.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SothisServlet extends HttpServlet {

	private ServletApplication app;

	@Override
	public void init() throws ServletException {
		app = new ServletApplication(this.getServletConfig());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			if (!app.execute(req, resp)) {
				app.getServletContext().getNamedDispatcher("default").forward(req, resp);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
