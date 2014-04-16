package org.sothis.nios;

public interface ChannelClosedHandler extends Handler {
	void channelClosed(ChannelContext ctx);
}
