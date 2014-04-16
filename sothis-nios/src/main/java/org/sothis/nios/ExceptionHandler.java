package org.sothis.nios;

public interface ExceptionHandler extends Handler {
	void exceptionCaught(ChannelContext ctx, Throwable e);
}
