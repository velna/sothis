package org.sothis.web.example.controllers;

import java.util.List;

import org.sothis.dal.query.Cnd;
import org.sothis.mvc.Request;
import org.sothis.mvc.Response;
import org.sothis.web.example.dao.sql.UsersDao;
import org.sothis.web.example.domains.Users;
import org.sothis.web.example.models.HelloWorldModel;

public class Controller {

	private UsersDao usersDao;

	public Object indexAction(Request request, Response response) {
		HelloWorldModel model = new HelloWorldModel();
		model.setMessage(request.getString("message"));
		List<Users> users = usersDao.find(Cnd.make("username", "sothis"));
		for (Users user : users) {
			System.out.println(user.getUsername());
		}
		System.out.println(usersDao.count());
		return model;
	}

	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}

}
