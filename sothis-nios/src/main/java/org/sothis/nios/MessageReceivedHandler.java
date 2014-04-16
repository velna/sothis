package org.sothis.nios;

public interface MessageReceivedHandler extends Handler {

	void messageReceived(ChannelContext ctx, Object message);

}
