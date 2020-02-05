/**
 *
 */
package com.telek.hemsipc.http;

import com.telek.hemsipc.http.netty.HttpServer;
import com.telek.smarthome.cloudserver.cacheManager.CacheModelManager;
import com.telek.smarthome.cloudserver.hemstools.monitor.handler.MonitorUdpHandler;
import com.telek.smarthome.cloudserver.hemstools.netty.UdpServerNetty;
import com.telek.smarthome.cloudserver.hemstools.util.PortFileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description: Create on Jun 23, 2014 10:50:56 AM
 *
 * @author 俞胜杰
 * @version 1.0 Copyright (c) 2014 telek. All Rights Reserved.
 */
public class HttpServerStart {
    private static final Log log = LogFactory.getLog(HttpServerStart.class);

    /**
     * http请求线程池
     */
    public static ExecutorService httpRequestThreadPool;
    /**
     * http响应处理线程池
     */
    public static ExecutorService httpResponseThreadPool;
    /**
     * web请求处理线程
     */
    public static ExecutorService webThreadPool;

    public static void start() {
        initThreadPool();
        initHostServerHttpPort();
//        initHttpMethodResolver();
        log.info("------------http server start success!------------");
    }

    private static void initThreadPool() {
        httpRequestThreadPool = Executors.newCachedThreadPool();
        httpResponseThreadPool = Executors.newCachedThreadPool();
        webThreadPool = Executors.newCachedThreadPool();
    }

    private static void initHostServerHttpPort() {
        int httpPort = 8088;
        // 设置主机地址及绑定的tcp端口
        HttpServer.setHttpPort(httpPort);
        HttpServer.init();
    }

//    private static void initHttpMethodResolver() {
//        MethodResolver.init();
//    }

}
