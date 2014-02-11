package org.sothis.web.example.controllers;

import java.io.IOException;
import java.util.List;

import org.sothis.core.util.Trivalent;
import org.sothis.dal.query.Chain;
import org.sothis.dal.query.Cnd;
import org.sothis.mvc.AsyncContext;
import org.sothis.mvc.Sothis;
import org.sothis.web.example.dao.sql.UsersDao;
import org.sothis.web.example.domains.Users;
import org.sothis.web.example.models.HelloWorldModel;
import org.sothis.web.mvc.WebActionContext;
import org.sothis.web.mvc.interceptors.param.Param;

public class Controller {

	private UsersDao usersDao;

	public Object indexAction(HelloWorldModel model, @Param(name = "message") String message) {
		List<Users> users = usersDao.find(Cnd.make("name", "velna__"));
		for (Users user : users) {
			System.out.println(user.getName());
			usersDao.updateById(user.getId(), Chain.make("name", user.getName() + "_"));
		}
		System.out.println(usersDao.count());
		return model;
	}

	public void testAction() {
		final AsyncContext async = WebActionContext.getContext().startAsync();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					async.complete("abc");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		System.out.println("abc");
	}

	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}

}
