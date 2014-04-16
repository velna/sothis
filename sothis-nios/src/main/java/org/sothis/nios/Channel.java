package org.sothis.nios;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.channels.SelectableChannel;

public interface Channel {
	Handlers handlers();

	ReadBuffer readBuffer();

	WriteBuffer writeBuffer();

	Channel bind(SocketAddress local) throws IOException;

	SocketAddress localAddress() throws IOException;

	<T> Channel setOption(SocketOption<T> name, T value) throws IOException;

	<T> T getOption(SocketOption<T> name) throws IOException;

	SelectableChannel underlying();
}
