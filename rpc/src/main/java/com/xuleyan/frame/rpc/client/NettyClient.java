package com.xuleyan.frame.rpc.client;


import com.xuleyan.frame.rpc.handler.ClientEndChannelHandler;
import com.xuleyan.frame.rpc.handler.codec.End;
import com.xuleyan.frame.rpc.handler.codec.RpcDecoder;
import com.xuleyan.frame.rpc.handler.codec.RpcEncoder;
import com.xuleyan.frame.rpc.protocol.CommunicationProto;
import com.xuleyan.frame.rpc.protocol.Protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Netty实现的客户端
 * 
 * @author xuleyan
 */
public class NettyClient extends AbstClient {

	/**
	 * 请求协议
	 */
	private static Protocol protocol = Protocol.hessian;

	private ChannelFuture channelFuture;

	private EventLoopGroup group;

	/**
	 * @param host 服务器地址
	 * @param port 端口
	 * @param token 请求加密token
	 */
	public NettyClient(String host, int port, String token) {
		super(host, port, token);
	}

	@Override
	public Long sendRequest(String methodKey, Object data) throws IOException {
		Long requestUuid = getUuid();

		/* 防止获取数据为空 */
		setResponse(requestUuid, null);

		/* 构建请求原型 */
		CommunicationProto proto = Protocol.getCommunicationProto(protocol.getIndex());
		proto.setUuid(requestUuid);
		proto.setToken(getToken());
		proto.setKey(methodKey);
		proto.setBody(data);

		channelFuture = connect(proto).addListener((ChannelFutureListener) future -> {
			if (!future.isSuccess()) {
				if (future.channel().isActive()) {
					future.channel().close();
				}
			}
		});

		return requestUuid;
	}

	private ChannelFuture connect(CommunicationProto proto) {
		final CommunicationProto frequest = proto;
		Bootstrap b = new Bootstrap();
		if(group != null){
			group.shutdownGracefully();
			group = null;
		}
		group = new NioEventLoopGroup();
		try {
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.SO_REUSEADDR, true)
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIME_OUT)
					.remoteAddress(new InetSocketAddress(host, port))
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) {
							ChannelPipeline pipeline = ch.pipeline();
							/*  自定义分割符，解决大数据传输下沾包拆包问题 */
							ByteBuf delimiter = Unpooled.copiedBuffer("\r\n".getBytes());
							pipeline.addLast(new DelimiterBasedFrameDecoder(10485760, delimiter));
							pipeline.addLast("encoder", new RpcEncoder(End.CLIENT));
							pipeline.addLast("decoder", new RpcDecoder(End.CLIENT));
							pipeline.addLast("handler", new ClientEndChannelHandler(frequest));
						}
					});

			ChannelFuture future = b.connect().syncUninterruptibly();
			future.awaitUninterruptibly(TIME_OUT);
			return future;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void close(){
		if(channelFuture == null){
			return;
		}

		Channel channel = channelFuture.channel();

		if(channel.isOpen() || channel.isActive()){
			channel.close();
			channelFuture = null;
		}

		if(group != null){
			group.shutdownGracefully();
		}
	}
}
