package org.sothis.mvc.http.netty;

import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ActionInvocationHelper;
import org.sothis.mvc.ApplicationContext;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class NettyHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private final ApplicationContext applicationContext;

	public NettyHttpRequestHandler(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		FullHttpResponse respMsg = new DefaultFullHttpResponse(msg.getProtocolVersion(), HttpResponseStatus.OK, ctx.alloc()
				.buffer(512));
		NettyHttpRequest request = new NettyHttpRequest(msg, ctx.channel());
		NettyHttpResponse response = new NettyHttpResponse(respMsg);
		ActionInvocationHelper.invoke(ActionContext.getContext(), applicationContext, request, response);
		response.commit();
		ctx.writeAndFlush(respMsg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ActionContext context = ActionContext.getContext();
		if (cause instanceof Exception) {
			context.getExceptionHandler().exceptionCaught(context, (Exception) cause);
		} else {
			super.exceptionCaught(ctx, cause);
		}
		ctx.close();
	}

}
