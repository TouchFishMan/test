package com.telek.hemsipc.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.telek.hemsipc.netty.future.SyncWriteFuture;

import io.netty.channel.Channel;

/**
 * @Auther: wll
 * @Date: 2018/9/10 13:55
 * @Description: Netty相关上下文读取
 */
public class NettyContext {
    /**
     * 客户端连接map<channelId,channel>.
     */
    public static Map<String, Channel> clientChannel = new ConcurrentHashMap<String, Channel>();
    /**
     * 每次电表请求用于同步的回调缓存map<address-command,SyncWriteFuture>.
     */
    public static Map<String, SyncWriteFuture> syncKey = new ConcurrentHashMap<String, SyncWriteFuture>();
    /**
     * channel和解码协议对应map.Map<channelId,decodeProtocolCode>
     */
    public static Map<String, String> channelDeviceMapping = new ConcurrentHashMap<>();
}
