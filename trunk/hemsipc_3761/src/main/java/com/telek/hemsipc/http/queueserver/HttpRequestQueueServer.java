package com.telek.hemsipc.http.queueserver;

import com.telek.hemsipc.http.HttpServerStart;
import com.telek.hemsipc.http.netty.entity.HttpTransferEntity;
import com.telek.smarthome.cloudserver.cacheManager.constantNew.rest.CommonConstant;
import com.telek.smarthome.cloudserver.hemstools.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:Http请求队列处理类 HttpRequestQueueServer.java Create on 2013-1-30
 * 下午2:41:10
 * 
 * @author 吕蔡俊
 * @version 1.0 Copyright (c) 2013 telek. All Rights Reserved.
 */

public class HttpRequestQueueServer implements Runnable {
    private static final Log LOG = LogFactory.getLog(HttpRequestQueueServer.class);
    public static Map<String, HttpTransferEntity> requestMap = new ConcurrentHashMap<String, HttpTransferEntity>();
    private FullHttpRequest request;
    private ChannelHandlerContext ctx;
    private ByteBuf bodyBuf;

    public HttpRequestQueueServer() {
    }

    public HttpRequestQueueServer(HttpTransferEntity transferEntity) {
        this.ctx = transferEntity.getCtx();
        this.request = transferEntity.getRequest();
        this.bodyBuf = transferEntity.getBodyBuf();
        requestMap.put(ctx.channel().remoteAddress().toString().substring(1), transferEntity);
    }

    /**
     * 从总HttpRequst消息队列里取数据,并进行处理
     */
    @Override
    public void run() {
        Map<String, String> requestMap = getRequestMap(request, ctx);
        // 对没有语言参数的请求默认中文请求
        if (StringUtil.isBlank(requestMap.get("languageCategory"))
                ||requestMap.get(requestMap.get("languageCategory")).indexOf("zh") >= 0) {
            requestMap.put("languageCategory", "zh_CN");
        }
        try {
            process(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description:对消息进行处理 Date:2013-1-30
     * 
     * @author 吕蔡俊
     * @param requestMap
     * @throws Exception
     */
    public void process(Map<String, String> requestMap) throws Exception {
        HttpServerStart.webThreadPool.execute(new WebQueueServer(requestMap));
    }

    /**
     * Description: Date:2014-8-13 获取请求Map
     * 
     * @author shenbf
     * @param @param e
     * @param @return
     * @return Map<String,String>
     */
    private Map<String, String> getRequestMap(FullHttpRequest request, ChannelHandlerContext ctx) {
        Map<String, String> requestMap = new HashMap<String, String>();
        Map<String, List<String>> map = getParameters(request);
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            List<String> list = map.get(key);
            requestMap.put(key, list.get(0));
        }
        requestMap.put("User-Agent", request.headers().get("User-Agent"));
        requestMap.put("channelId", ctx.channel().remoteAddress().toString().substring(1));
        // 加入真IP，lxf
        requestMap.put(CommonConstant.REALIP, request.headers().get("X-Real-IP"));
        requestMap.put("address", ctx.channel().remoteAddress().toString());
        return requestMap;
    }

    /**
     * Description:从请求中获取参数 Date:2013-1-30
     * 
     * @author 吕蔡俊
     * @param @return
     * @return Map<String,List<String>>
     */
    public Map<String, List<String>> getParameters(FullHttpRequest request) {
        if (request.getMethod() == HttpMethod.GET) {
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
            return queryStringDecoder.parameters();
        } else if (request.getMethod() == HttpMethod.POST) {
            QueryStringDecoder queryStringDecoder = null;
            try {
                queryStringDecoder = new QueryStringDecoder("/?" + bodyBuf.toString(CharsetUtil.UTF_8));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return queryStringDecoder.parameters();
        }
        return new HashMap<String, List<String>>();
    }
}
