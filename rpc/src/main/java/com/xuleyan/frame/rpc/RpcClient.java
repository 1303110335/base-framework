/**
 * bianque.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.rpc;

import com.xuleyan.frame.rpc.client.Client;
import com.xuleyan.frame.rpc.client.NettyClient;

import java.io.IOException;

/**
 *
 * @author xuleyan
 * @version RpcClient.java, v 0.1 2021-07-08 10:00 下午
 */
public class RpcClient {
    private Client client;

    public RpcClient(String host, int port, String token) {
        client = new NettyClient(host, port, token);
    }

    public RpcResult invoke(String methodKey, Object data) {
        long requestUuid = 0;
        try {
            requestUuid = client.sendRequest(methodKey, data);
        } catch (IOException e) {
            return RpcResult.instance(e);
        }
        RpcResult rpcResult = client.getResponse(requestUuid);
        return rpcResult;
    }
}