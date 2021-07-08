package com.xuleyan.frame.rpc.client;



import com.xuleyan.frame.rpc.RpcResult;

import java.io.IOException;

/**
 *
 * @author xuleyan
 * @version Tuple.java, v 0.1 2021-07-08 9:17 下午
 */
public interface Client {

	Long sendRequest(String methodKey, Object data) throws IOException;

	RpcResult getResponse(Long requestIndex);

}
