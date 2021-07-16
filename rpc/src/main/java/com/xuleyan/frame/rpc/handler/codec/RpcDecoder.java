/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.rpc.handler.codec;

import com.xuleyan.frame.rpc.common.RpcContants;
import com.xuleyan.frame.rpc.protocol.CommunicationProto;
import com.xuleyan.frame.rpc.protocol.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;

/**
 * 解码器 ( 把字节数组反序列化请求实体供处理器(handler使用) )
 * @author xuleyan
 * @version RpcDecoder.java, v 0.1 2021-07-08 9:48 下午
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private End end;

    public RpcDecoder(End end) {
        this.end = end;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> msgList) throws Exception {

        byte protocolBit = byteBuf.readByte();
        CommunicationProto communicationProto = Protocol.getCommunicationProto(protocolBit);

        if (communicationProto == null) {
            return;
        }

        if (byteBuf.readableBytes() < communicationProto.getHeaderBitLen()) {
            return;
        }

        try {
            /* 读取uuid */
            long uuid = byteBuf.readLong();
            communicationProto.setUuid(uuid);
            /* 读取key */
            String key = byteBufLen2Str(byteBuf, communicationProto);
            communicationProto.setKey(key);
            /* 读取token */
            String token = byteBufLen2Str(byteBuf, communicationProto);
            communicationProto.setToken(token);


            // 请求体/返回体标志
            byte bodyFlagBit = byteBuf.readByte();
            // 异常标志
            byte exceptionFlagBit = -1;

            /* 读取body byte array */
            if (bodyFlagBit == RpcContants.body_flag_bit) {
                int bodyBitLength = byteBuf.readInt();
                byte[] bodyBytes = new byte[bodyBitLength];
                byteBuf.readBytes(bodyBytes);
                communicationProto.setBody(bodyBytes);

                if (byteBuf.isReadable(1)) {
                    exceptionFlagBit = byteBuf.readByte();
                }
            } else {
                exceptionFlagBit = bodyFlagBit;
            }

            /* 读取exception byte array */
            if(exceptionFlagBit == RpcContants.exception_flag_bit) {
                int exceptionBitLength = byteBuf.readInt();
                byte[] exceptionBytes = new byte[exceptionBitLength];
                byteBuf.readBytes(exceptionBytes);
                Throwable exception = (Throwable) communicationProto.bytes2Obj(exceptionBytes);
                communicationProto.setE(exception);
            }
        } catch (Exception e) {
            communicationProto.setE(e);
        }

        msgList.add(communicationProto);

    }

    private String byteBufLen2Str(ByteBuf byteBuf, CommunicationProto communicationProto) throws IOException {
        byte[] bytes = new byte[communicationProto.getStr8BitLen()];
        byteBuf.readBytes(bytes);
        return (String) communicationProto.bytes2Obj(bytes);
    }
}
