package org.sothis.dal;

public interface Field {
	@Column
	String getName();

	Object getValue();
}
