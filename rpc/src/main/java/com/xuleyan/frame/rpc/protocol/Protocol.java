/**
 * bianque.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.rpc.protocol;

/**
 *
 * @author xuleyan
 * @version Protocol.java, v 0.1 2021-07-08 9:40 下午
 */
public enum Protocol {
    hessian((byte)1),
    ;

    private byte index;

    Protocol(byte index){
        this.index = index;
    }

    public byte getIndex() {
        return index;
    }

    public static Protocol parseBy(byte index){
        for(Protocol proto : Protocol.values()){
            if(proto.getIndex() == index) {
                return proto;
            }
        }
        return null;
    }

    public static CommunicationProto getCommunicationProto(byte index) {
        switch (index) {
            case (byte)1 :
                return new HessionCommunicationProto();
            default:
                return null;
        }
    }

}
