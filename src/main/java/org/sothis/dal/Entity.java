package org.sothis.dal;

import java.io.Serializable;
import java.util.Date;

public interface Entity extends Serializable {
	public long getId();

	public Date getCreationDate();

	public Date getModificationDate();

	public void setCreationDate(Date date);

	public void setModificationDate(Date date);
}
