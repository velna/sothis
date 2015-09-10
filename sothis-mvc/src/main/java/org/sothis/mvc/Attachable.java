package org.sothis.mvc;

import java.io.IOException;

public interface Attachable {
	Attachments attachments() throws IOException;
}
