/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.rpc.handler;

import com.xuleyan.frame.rpc.RpcServer;
import com.xuleyan.frame.rpc.common.Tuple;
import com.xuleyan.frame.rpc.protocol.CommunicationProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xuleyan
 * @version ServerEndChannelHandler.java, v 0.1 2021-07-08 9:51 下午
 */
public class ServerEndChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ServerEndChannelHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CommunicationProto reqProto = (CommunicationProto) msg;
        Tuple beanTuple = RpcServer.findService(reqProto.getKey());

        Object result = null;
        try {
            result = beanTuple.getMethod().invoke(beanTuple.getinstance(), new Object[]{reqProto.body2Obj()});
            reqProto.setBody(result);
        } catch (Exception e) {
            logger.error("invoke {}.{} has exception", beanTuple.getinstance().getClass(), beanTuple.getMethod().getName(), e);
            Map<String, Object> wrapper = new HashMap<>(5);
            wrapper.put("success", false);
            wrapper.put("errCode", "2000");
            wrapper.put("errMsg", "系统错误");
            try {
                reqProto.setBody(wrapper);
            } catch (IOException ex) {

            }
            reqProto.setE(e);
        }

        ctx.write(reqProto);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 断连
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channel id:[{}] inactive", ctx.channel().id());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        logger.error("channel[{}] has exception", ctx.channel().id(), cause);
        ctx.close();
    }
}