package org.sothis.nios;

import java.io.IOException;

public interface ChannelInitializer<C extends Channel> {
	void initialize(C channel) throws IOException;
}
