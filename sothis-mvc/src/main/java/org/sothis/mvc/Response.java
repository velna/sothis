package org.sothis.mvc;

import java.io.IOException;
import java.io.OutputStream;

public interface Response {

	OutputStream getOutputStream() throws IOException;

	boolean isCommitted();
}
