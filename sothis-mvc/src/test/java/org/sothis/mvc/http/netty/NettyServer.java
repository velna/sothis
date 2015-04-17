package org.sothis.mvc.http.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sothis.core.beans.BeanFactory;
import org.sothis.core.beans.BeanInstantiationException;
import org.sothis.core.beans.SimpleBeanFactory;
import org.sothis.mvc.ActionInvocationHelper;
import org.sothis.mvc.ApplicationContext;
import org.sothis.mvc.Configuration;
import org.sothis.mvc.ConfigurationException;
import org.sothis.mvc.DefaultApplicationContext;

public class NettyServer extends SimpleChannelInboundHandler<FullHttpRequest> {
	private final static Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

	private final ApplicationContext applicationContext;

	public NettyServer() throws ConfigurationException, BeanInstantiationException, ClassNotFoundException, IOException {
		Properties properties = new Properties();
		properties.put("sothis.controller.packages", "org.sothis.mvc.controllers");
		properties.put("sothis.interceptors.echoapp.class", "org.sothis.mvc.echoapp.EchoAppInterceptor");
		properties.put("sothis.interceptors.stack.default", "echoapp");
		Configuration config = new Configuration(properties);

		BeanFactory beanFactory = new SimpleBeanFactory();

		applicationContext = new DefaultApplicationContext(beanFactory, config);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		FullHttpResponse respMsg = new DefaultFullHttpResponse(msg.getProtocolVersion(), HttpResponseStatus.OK, ctx.alloc()
				.buffer(512));
		NettyHttpRequest request = new NettyHttpRequest(msg);
		NettyHttpResponse response = new NettyHttpResponse(respMsg);
		ActionInvocationHelper.invoke(applicationContext, request, response);
		response.commit();
		ctx.writeAndFlush(respMsg);
	}

	public static void main(String[] args) throws Exception {

		EventLoopGroup master = new NioEventLoopGroup(1);
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(master, worker);
			b.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					final ChannelPipeline p = ch.pipeline();
					p.addLast("httpCodec", new HttpServerCodec());
					p.addLast("aggregator", new HttpObjectAggregator(1048576));
					p.addLast("server", new NettyServer());
				}
			});
			b.channel(NioServerSocketChannel.class);
			b.option(ChannelOption.SO_BACKLOG, 128);
			b.option(ChannelOption.SO_REUSEADDR, true);
			b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			b.childOption(ChannelOption.SO_KEEPALIVE, true);
			// b.childOption(ChannelOption.MAX_MESSAGES_PER_READ,
			// config.getYeeapiMaxMessagePerRead());
			// b.childOption(ChannelOption.SO_RCVBUF, 512);
			// b.childOption(ChannelOption.SO_SNDBUF, 512);
			b.childOption(ChannelOption.TCP_NODELAY, true);
			// b.childOption(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK,
			// config.getYeeapiWriteBufferLowWaterMark());
			// b.childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK,
			// config.getYeeapiWriteBufferHighWaterMark());
			b.childOption(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);
			// b.childOption(ChannelOption.ALLOW_HALF_CLOSURE, false);
			// c.setPerformancePreferences(0, 10, 1);
			// b.childOption(ChannelOption.WRITE_SPIN_COUNT, 1);

			ChannelFuture f = b.bind(8080);
			LOGGER.info("listend at {}", 8080);
			f.sync();
			f.channel().closeFuture().sync();
		} finally {
			worker.shutdownGracefully().sync();
			master.shutdownGracefully().sync();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error("", cause);
	}

}
