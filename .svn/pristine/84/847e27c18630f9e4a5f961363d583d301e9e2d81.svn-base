package com.telek.hemsipc.protocal3761.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

/**
 * @Auther: wll
 * @Date: 2018/6/21 13:55
 * @Description: Netty相关上下文读取
 */
public class NettyContext {
    /**
     * 集中器对应的nettychannelId，map<集中器地址，channelid>.
     */
    public static Map<String, String> addressChannelIdMap = new ConcurrentHashMap<>();
    /**
     * 客户端连接map<ip:port,channel>.
     */
    public static Map<String, Channel> channelIdChannelMap = new ConcurrentHashMap<>();

    /**
     * 发生意外断开链接取消缓存
     * @param channelId ip:port
     * @return address
     */
    public static String accidentRemoveAddress(String channelId) {
        if (channelId == null) {
            return null;
        }
        channelIdChannelMap.remove(channelId);
        String address = null;
        for (Map.Entry<String, String> entry : addressChannelIdMap.entrySet()) {
            String value = entry.getValue();
            if (value.endsWith(channelId)) {
                address = entry.getKey();
                break;
            }
        }
        if (address != null) {
            addressChannelIdMap.remove(address);
        }
        return address;
    }
}
