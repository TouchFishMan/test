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
     * @return address2
     */
    public static String accidentRemoveAddress2(String channelId) {
        if (channelId == null) {
            return null;
        }
        channelIdChannelMap.remove(channelId);
        String address2 = null;
        for (Map.Entry<String, String> entry : addressChannelIdMap.entrySet()) {
            String value = entry.getValue();
            if (value.endsWith(channelId)) {
                address2 = entry.getKey();
                break;
            }
        }
        if (address2 != null) {
            addressChannelIdMap.remove(address2);
        }
        return address2;
    }
}
