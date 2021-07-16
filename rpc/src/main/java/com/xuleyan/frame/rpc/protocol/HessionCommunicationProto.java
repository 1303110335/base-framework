/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.rpc.protocol;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author xuleyan
 * @version HessionCommunicationProto.java, v 0.1 2021-07-08 9:42 下午
 */
public class HessionCommunicationProto extends CommunicationProto {

    public static int HEAD_SIZE = 0;
    static {
        try {
            String data = "AaBbCc12";
            HEAD_SIZE = toBytes(data).length;
        } catch (IOException e) {
            throw new IllegalStateException("toBytes Error");
        }
    }

    @Override
    public Object body2Obj() throws IOException {
        return toObj(getBody());
    }

    @Override
    public void setBody(Object bodyObj) throws IOException {
        byte[] bytes = toBytes(bodyObj);
        setBody(bytes);
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.hessian;
    }

    @Override
    public int getStr8BitLen() {
        return HEAD_SIZE;
    }

    @Override
    public byte[] obj2Bytes(Object obj) throws IOException {
        return toBytes(obj);
    }

    @Override
    public Object bytes2Obj(byte[] bytes) throws IOException {
        return toObj(bytes);
    }

    private static Object toObj(byte[] bytes) throws IOException {
        Hessian2Input hessian2Input = null;
        try {
            hessian2Input = new Hessian2Input(new ByteArrayInputStream(bytes));
            return hessian2Input.readObject();
        } finally {
            if(hessian2Input != null) {
                hessian2Input.close();
            }
        }
    }

    private static byte[] toBytes(Object data) throws IOException {
        Hessian2Output hessian2Output = null;
        ByteArrayOutputStream byteArray = null;
        try {
            byteArray = new ByteArrayOutputStream();
            hessian2Output = new Hessian2Output(byteArray);
            hessian2Output.writeObject(data);
            hessian2Output.flush();
            byte[] bytes = byteArray.toByteArray();
            return bytes;
        } finally {
            if(byteArray != null){
                byteArray.close();
            }
            if(hessian2Output != null) {
                hessian2Output.close();
            }
        }
    }
}
