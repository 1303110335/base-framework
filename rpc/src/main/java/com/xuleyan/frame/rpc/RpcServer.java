/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.rpc;

import com.xuleyan.frame.rpc.client.AbstClient;
import com.xuleyan.frame.rpc.common.Tuple;
import com.xuleyan.frame.rpc.handler.ServerEndChannelHandler;
import com.xuleyan.frame.rpc.handler.codec.End;
import com.xuleyan.frame.rpc.handler.codec.RpcDecoder;
import com.xuleyan.frame.rpc.handler.codec.RpcEncoder;
import com.xuleyan.frame.rpc.key.RpcBean;
import com.xuleyan.frame.rpc.key.RpcMethod;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author xuleyan
 * @version RpcServer.java, v 0.1 2021-07-08 9:13 下午
 */
public class RpcServer implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private static ConcurrentHashMap<String, Tuple> responseUuidMap = new ConcurrentHashMap<String, Tuple>();

    private ApplicationContext applicationContext;

    public static Tuple findService(String methodKey) {
        return responseUuidMap.get(methodKey);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, Object> rpcBeanMap = applicationContext.getBeansWithAnnotation(RpcBean.class);
        for (Object bean : rpcBeanMap.values()) {
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);
                if (rpcMethod != null) {
                    registerService(rpcMethod.key(), method, bean);
                }
            }
        }
    }

    private void registerService(String key, Method method, Object bean) {
        if (responseUuidMap.contains(key)) {
            throw new IllegalArgumentException(key + "服务key重复");
        }
        responseUuidMap.put(key, new Tuple(bean, method));
    }

    /* 线程大小 */
    private static int MAX_THREAD = Runtime.getRuntime().availableProcessors() * 2 + 2;


    private int port;
    /**
     * 是否打印channel以及加载器日志信息
     */
    private boolean printLogger = false;

    public void startRpcServer() {
        /*
        服务端需要2个线程组
            boss处理客户端连接  boss辅助客户端的tcp连接请求
            work进行客服端连接之后的处理 worker负责与客户端之前的读写操作
        */
        EventLoopGroup boss = new NioEventLoopGroup(MAX_THREAD);
        EventLoopGroup worker = new NioEventLoopGroup(MAX_THREAD * 2);
        ServerBootstrap bootstrap = new ServerBootstrap();

        /*服务器配置*/
        ServerBootstrap serverBootstrap = bootstrap.group(boss, worker)
                /* 配置客户端的channel类型 */
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, AbstClient.TIME_OUT)
                /* TCP_NODELAY算法，尽可能发送大块数据，减少充斥的小块数据 */
                .childOption(ChannelOption.TCP_NODELAY, true)
                /* 开启心跳包活机制，就是客户端、服务端建立连接处于ESTABLISHED状态，超过2小时没有交流，机制会被启动 */
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_LINGER, 1).childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        if (printLogger) {
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        }
                        ByteBuf delimiter = Unpooled.copiedBuffer("\r\n".getBytes());
                        pipeline.addLast(new DelimiterBasedFrameDecoder(10485760, delimiter));
                        // rpc编解码器
                        pipeline.addLast("encoder", new RpcEncoder(End.SERVER));
                        pipeline.addLast("decoder", new RpcDecoder(End.SERVER));
                        // 服务端数据处理器
                        pipeline.addLast("handler", new ServerEndChannelHandler());
                    }
                });

        if (printLogger) {
            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        }

        ChannelFuture channelFuture = bootstrap.bind();
        channelFuture.addListener(future -> {
            if (!future.isSuccess()) {
                future.cause().printStackTrace();
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            boss.shutdownGracefully().syncUninterruptibly();
            worker.shutdownGracefully().syncUninterruptibly();
        }));

    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPrintLogger(boolean printLogger) {
        this.printLogger = printLogger;
    }
}