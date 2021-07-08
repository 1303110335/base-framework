/**
 * bianque.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.rpc.handler.codec;

import com.xuleyan.frame.rpc.common.RpcContants;
import com.xuleyan.frame.rpc.protocol.CommunicationProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 *
 * @author xuleyan
 * @version RpcEncoder.java, v 0.1 2021-07-08 9:44 下午
 */
public class RpcEncoder extends MessageToByteEncoder<CommunicationProto> {

    private End end;

    public RpcEncoder(End end) {
        this.end = end;
    }

    /**
     * 注意写入顺序，和{@link RpcDecoder#decode }读取顺序要一致
     * @param channelHandlerContext
     * @param message
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, CommunicationProto message, ByteBuf byteBuf) throws Exception {
        byteBuf.writeByte(message.getProtocol().getIndex());
        byteBuf.writeLong(message.getUuid());

        // 固定大小不需要字节长度位
        byteBuf.writeBytes(message.obj2Bytes(message.getKey()));
        byteBuf.writeBytes(message.obj2Bytes(message.getToken()));

        // 请求体 标志位|长度|字节数组
        if(message.getBody() != null) {
            byteBuf.writeByte(RpcContants.body_flag_bit);
            byteBuf.writeInt(message.getBody().length);
            byteBuf.writeBytes(message.getBody());
        }

        // 异常信息输出 标志|长度|请求体
        if(message.getE() != null) {
            byteBuf.writeByte(RpcContants.exception_flag_bit);
            byte[] throwableBytes = message.obj2Bytes(message.getE());
            byteBuf.writeInt(throwableBytes.length);
            byteBuf.writeBytes(throwableBytes);
        }
        byteBuf.writeBytes(new byte[]{'\r','\n'});
    }
}