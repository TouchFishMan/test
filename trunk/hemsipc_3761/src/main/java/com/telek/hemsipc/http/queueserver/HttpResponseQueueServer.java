package com.telek.hemsipc.http.queueserver;

import com.alibaba.fastjson.JSONObject;
import com.telek.hemsipc.http.netty.entity.HttpTransferEntity;
import com.telek.smarthome.cloudserver.cacheManager.constantNew.rest.CommonConstant;
import com.telek.smarthome.cloudserver.cacheManager.util.StringUtil;
import com.telek.smarthome.cloudserver.hemstools.monitor.MonitorService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * Description: HttpResponseQueueServer.java Create on 2013-1-30 下午5:36:51
 * 
 * @author 吕蔡俊
 * @version 1.0 Copyright (c) 2013 telek. All Rights Reserved.
 */
public class HttpResponseQueueServer implements Runnable {
    private static final Log LOG = LogFactory.getLog(HttpResponseQueueServer.class);
    private Map<String, Object> httpResponseMap = null;

    public HttpResponseQueueServer() {
    }

    public HttpResponseQueueServer(Map<String, Object> httpResponseMap) {
        this.httpResponseMap = httpResponseMap;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
	public void run() {
        try {
            process(httpResponseMap);
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            MonitorService.sendException(e);
        }
    }

    /**
     * Description:具体业务操作 Date:2013-4-27
     * 
     * @author Administrator
     * @param httpResponseMap
     */
    public void process(Map<String, Object> httpResponseMap) {
        String channelId = StringUtil.toString(httpResponseMap.get(CommonConstant.CHANNELID));
        HttpTransferEntity transferEntity = HttpRequestQueueServer.requestMap.get(channelId);
        FullHttpRequest request = transferEntity.getRequest();
        String parameterStr = StringUtil.toString(JSONObject.toJSON(httpResponseMap.get("parameter")));
        // // Decide whether to close the connection or not.
        boolean close = HttpHeaders.Values.CLOSE.toString()
                .equals(request.headers().get(HttpHeaders.Names.CONNECTION.toString()))
                || request.getProtocolVersion().equals(HttpVersion.HTTP_1_0) && !HttpHeaders.Values.KEEP_ALIVE
                        .toString().equals(request.headers().get(HttpHeaders.Names.CONNECTION.toString()));
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        ByteBuf buffer = Unpooled.copiedBuffer(parameterStr, CharsetUtil.UTF_8);
        int bufferLength = buffer.readableBytes();
        response.content().writeBytes(buffer);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE,
                httpResponseMap.containsKey(HttpHeaders.Names.CONTENT_TYPE.toString())
                        ? httpResponseMap.get(HttpHeaders.Names.CONTENT_TYPE.toString())
                        : "text/html; charset=UTF-8");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(bufferLength));
        if (httpResponseMap.containsKey("userLimit")) {
            response.headers().set("userLimit", httpResponseMap.get("userLimit"));
        }
        String cookieString = request.headers().get(HttpHeaders.Names.COOKIE);
        if (cookieString != null) {
            Set<Cookie> cookies = CookieDecoder.decode(cookieString);
            if (!cookies.isEmpty()) {
                response.headers().add(HttpHeaders.Names.SET_COOKIE, ClientCookieEncoder.encode(cookies));
            }
        }
        response.headers().set("Access-Control-Allow-Origin", "http://192.168.0.24:9900");
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/json;charset=UTF-8");
        response.headers().set("Access-Control-Allow-Credentials", "true");// 支持cookie跨域
        response.headers().set("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
        ChannelFuture future = transferEntity.getCtx().channel().writeAndFlush(response);
        HttpRequestQueueServer.requestMap.remove(channelId);
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
