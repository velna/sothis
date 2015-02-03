package org.sothis.web.example.domains;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sothis.dal.sql.SqlEntity;

@Table(name = "users")
@javax.persistence.Entity
public class Users implements SqlEntity {

	private static final long serialVersionUID = -7013732700961112303L;
	private int id;
	private String username;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
