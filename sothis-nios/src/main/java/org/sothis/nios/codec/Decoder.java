package org.sothis.nios.codec;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.sothis.nios.ChannelContext;
import org.sothis.nios.MessageReceivedHandler;
import org.sothis.nios.ReadBuffer;

public abstract class Decoder implements MessageReceivedHandler {

	private final LinkedList<Object> out = new LinkedList<Object>();

	protected abstract boolean decode(ChannelContext ctx, ReadBuffer in, List<Object> out);

	private void fireNext(final ChannelContext ctx) {
		if (out.size() > 0) {
			for (Iterator<Object> i = out.iterator(); i.hasNext();) {
				ctx.fireMessageReceived(ctx, i.next());
				ctx.reset();
				i.remove();
			}
		}
		ctx.events().submit(0, new Runnable() {
			@Override
			public void run() {
				if (decode(ctx, ctx.channel().readBuffer(), out)) {
					fireNext(ctx);
				}
			}
		});
	}

	@Override
	public void messageReceived(ChannelContext ctx, Object message) {
		if (decode(ctx, ctx.channel().readBuffer(), out)) {
			fireNext(ctx.fork());
		}
	}
}
