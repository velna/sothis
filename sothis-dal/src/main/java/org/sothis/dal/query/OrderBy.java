package org.sothis.dal.query;

import java.util.List;

public interface OrderBy {

	OrderBy asc(String field);

	OrderBy desc(String field);

	List<Sort> getSorts();

}
