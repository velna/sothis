package org.sothis.mvc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public interface Response {
	OutputStream getOutputStream() throws IOException;

	PrintWriter getWriter() throws IOException;
}
