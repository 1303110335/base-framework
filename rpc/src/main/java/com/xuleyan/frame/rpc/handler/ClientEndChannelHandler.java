/**
 * bianque.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.rpc.handler;

import com.xuleyan.frame.rpc.RpcResult;
import com.xuleyan.frame.rpc.client.AbstClient;
import com.xuleyan.frame.rpc.protocol.CommunicationProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *
 * @author xuleyan
 * @version ClientEndChannelHandler.java, v 0.1 2021-07-08 10:02 下午
 */
public class ClientEndChannelHandler extends ChannelInboundHandlerAdapter {

    private CommunicationProto requestProto;


    public ClientEndChannelHandler(CommunicationProto requestProto) {
        this.requestProto = requestProto;
    }

    /**
     * 发送请求数据
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(requestProto);
    }

    /**
     * 获取结果
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        CommunicationProto responseProto = (CommunicationProto)msg;
        try {
            AbstClient.setResponse(responseProto.getUuid(), RpcResult.instance(responseProto.body2Obj()));
        } catch (Exception e) {
            AbstClient.setResponse(requestProto.getUuid(), RpcResult.instance(e));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        AbstClient.setResponse(requestProto.getUuid(), RpcResult.instance(cause));
        // Close the connection when an exception is raised.
        ctx.close();
    }
}