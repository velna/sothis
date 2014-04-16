package org.sothis.nios;

import java.nio.channels.SelectableChannel;

public interface Channel {
	Handlers handlers();

	ReadBuffer readBuffer();

	WriteBuffer writeBuffer();

	SelectableChannel underlying();
}
