package org.sothis.dal;

public class SimpleField implements Field {

	private final String name;
	private final Object value;

	public SimpleField(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getValue() {
		return value;
	}

}
