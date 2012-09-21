package org.sothis.dal;

import java.io.Serializable;

public interface Entity<K> extends Serializable {
	K getId();

	void setId(K id);
}
