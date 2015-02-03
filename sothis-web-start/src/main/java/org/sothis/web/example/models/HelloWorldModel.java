package org.sothis.web.example.models;

public class HelloWorldModel extends BaseModel {
	private static final long serialVersionUID = -2785739089171984550L;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
