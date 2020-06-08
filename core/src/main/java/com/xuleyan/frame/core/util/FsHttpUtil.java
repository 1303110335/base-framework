/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.core.util;

import cn.hutool.http.ssl.DefaultTrustManager;
import com.xuleyan.frame.core.constants.StringPool;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author xuleyan
 * @version FsHttpUtil.java, v 0.1 2020-06-07 7:38 AM xuleyan
 */
public class FsHttpUtil {

    private static final String DEFAULT_CHARSET = StringPool.UTF_8;
    private static final String METHOD_POST = "POST";
    private static final String METHOD_GET = "GET";

    private static final int DEFAULT_READ_TIMEOUT = 10 * 1000;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 3 * 1000;
    private static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String JSON_CONTENT_TYPE = "application/json;charset=utf-8";
    private static HostnameVerifier verifier = null;
    private static SSLSocketFactory socketFactory = null;

    static {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
            ctx.getClientSessionContext().setSessionTimeout(15);
            ctx.getClientSessionContext().setSessionCacheSize(1000);
            socketFactory = ctx.getSocketFactory();
        } catch (Exception e) {
        }
        verifier = (hostname, session) -> true;
    }

    /**
     * post请求
     *
     * @param url
     * @param param
     * @param timeout
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> param, int timeout) throws IOException {
        return post(url, param, DEFAULT_CHARSET, DEFAULT_CONTENT_TYPE, DEFAULT_CONNECTION_TIMEOUT, timeout, null);
    }

    public static String get(String url, int timeout) throws IOException {
        return get(url, null, DEFAULT_CHARSET, DEFAULT_CONTENT_TYPE, DEFAULT_CONNECTION_TIMEOUT, timeout, null);
    }

    /**
     * @param url
     * @param params
     * @param charset
     * @param connectTimeout 毫秒
     * @param readTimeout    毫秒
     * @param contentType
     * @param headers
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String, String> params, String contentType,
                             String charset, int connectTimeout, int readTimeout, Map<String, String> headers) throws IOException {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("url is empty");
        }
        if (StringUtils.isBlank(charset)) {
            charset = DEFAULT_CHARSET;
        }
        if (StringUtils.isBlank(contentType)) {
            contentType = DEFAULT_CONTENT_TYPE;
        }
        if (connectTimeout <= 0) {
            connectTimeout = DEFAULT_CONNECTION_TIMEOUT;
        }
        if (readTimeout <= 0) {
            readTimeout = DEFAULT_READ_TIMEOUT;
        }
        HttpURLConnection conn = null;
        String rsp = null;
        try {
            String query = buildQuery(params, charset);
            conn = getConnection(buildGetUrl(url, query), METHOD_GET, contentType, headers);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            rsp = getResponseAsString(conn);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    private static URL buildGetUrl(String strUrl, String query) throws MalformedURLException {
        URL url = new URL(strUrl);
        if (StringUtils.isEmpty(query)) {
            return url;
        }

        if (StringUtils.isEmpty(url.getQuery())) {
            if (strUrl.endsWith("?")) {
                strUrl = strUrl + query;
            } else {
                strUrl = strUrl + "?" + query;
            }
        } else {
            if (strUrl.endsWith("&")) {
                strUrl = strUrl + query;
            } else {
                strUrl = strUrl + "&" + query;
            }
        }

        return new URL(strUrl);
    }

    public static String post(String url, Map<String, String> params, String charset, String contentType, int connectTimeout, int readTimeout, Map<String, String> headers) throws IOException {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("url is empty");
        }

        if (StringUtils.isBlank(charset)) {
            charset = DEFAULT_CHARSET;
        }

        if (StringUtils.isBlank(contentType)) {
            contentType = DEFAULT_CONTENT_TYPE;
        }

        if (connectTimeout <= 0) {
            connectTimeout = DEFAULT_CONNECTION_TIMEOUT;
        }

        if (readTimeout <= 0) {
            readTimeout = DEFAULT_READ_TIMEOUT;
        }

        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        String query = buildQuery(params, charset);
        byte[] content = {};
        if (StringUtils.isNotBlank(query)) {
            content = query.getBytes(charset);
        }

        try {
            conn = getConnection(new URL(url), METHOD_POST, contentType, headers);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            out = conn.getOutputStream();
            out.write(content);
            rsp = getResponseAsString(conn);
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    private static String getResponseAsString(HttpURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        } else {
            String msg = getStreamAsString(es, charset);
            if (StringUtils.isEmpty(msg)) {
                throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
            } else {
                throw new IOException(msg);
            }
        }
    }

    /**
     * 处理stream
     *
     * @param stream
     * @param charset
     * @return
     */
    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();
            char[] chars = new char[256];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }
            return writer.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private static String getResponseCharset(String contentType) {
        String charset = DEFAULT_CHARSET;
        if (!StringUtils.isEmpty(contentType)) {
            String[] params = contentType.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        if (!StringUtils.isEmpty(pair[1])) {
                            charset = pair[1].trim();
                        }
                    }
                    break;
                }
            }
        }
        return charset;
    }

    private static HttpURLConnection getConnection(URL url, String method, String contentType, Map<String, String> headers) throws IOException {
        HttpURLConnection conn = null;
        if ("https".equals(url.getProtocol())) {
            HttpsURLConnection connHttps = null;
            connHttps = (HttpsURLConnection) url.openConnection();
            connHttps.setSSLSocketFactory(socketFactory);
            connHttps.setHostnameVerifier(verifier);
            conn = connHttps;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        conn.setRequestMethod(method);
        // 可以从URL connection读取数据
        conn.setDoInput(true);
        // 可以写入数据到URL connection
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", contentType);
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Encoding", "gzip");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36 FsHttpUtil");
        if (headers != null && headers.size() > 0) {
            headers.forEach(conn::setRequestProperty);
        }
        return conn;
    }

    private static String buildQuery(Map<String, String> params, String charset) throws UnsupportedEncodingException {
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuilder query = new StringBuilder();
        boolean hasParam = false;
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (hasParam) {
                query.append("&");
            } else {
                hasParam = true;
            }
            query.append(name).append("=").append(URLEncoder.encode(value, charset));
        }
        return query.toString();
    }

    public static void main(String[] args) throws IOException {
        // %E4%B8%AD%E5%9B%BD
        //System.out.println(URLEncoder.encode("中国", "UTF-8"));

        testPost();

        //testGet();
    }

    private static void testGet() throws IOException {
        String s = get("http://localhost:8081/ping", 10000);
        System.out.println(s);
    }

    public static void testPost() throws IOException {
        //method:com.xuleyan.consumer.hello
        //appid:20181201010101
        //sign:00caa6d43c8195ec0b3b3c46cce017fa
        //version:1.0
        //content:{"name": "xuleyan"}
        Map<String, String> data = new HashMap<>();
        data.put("method", "com.xuleyan.consumer.hello");
        data.put("appid", "20181201010101");
        data.put("sign", "00caa6d43c8195ec0b3b3c46cce017fa");
        data.put("version", "1.0");
        data.put("content", "{\"name\": \"xuleyan\"}");
        String post = post("http://localhost:8081/newGateway", data, 10000);
        System.out.println(post);
    }

}