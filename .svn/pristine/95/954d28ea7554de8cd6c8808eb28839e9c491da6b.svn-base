package com.telek.hemsipc.sdmp.resend;

import com.telek.hemsipc.sdmp.SDMPv1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhanxf 2015-11-18
 * @Descrption 数据发送队列处理
 */
public class SendMap {
    // 重发队列 Map<DeviceId,Map<msgId,ResendObject>>
    public static Map<String, Map<Integer, ResendObject>> resendMap = new ConcurrentHashMap<String, Map<Integer, ResendObject>>();

    /**
     * 将sdmp包放入从发队列
     *
     * @param sdmpData
     */
    public static void addRendMap(SDMPv1 sdmpData) {
        ResendObject resendObject = new ResendObject(sdmpData);
        String deviceId = sdmpData.getMsgAuthoritativeEngineID().toString();
        Integer msgId = sdmpData.getMsgData().getMsgID().toInt();
        // 添加重发
        if (resendMap.containsKey(deviceId) && resendMap.get(deviceId).containsKey(msgId)) {
            //重发队列中已存在该重发对象，则不需要再次重发
            return;
        } else if (resendMap.containsKey(deviceId) && !resendMap.get(deviceId).containsKey(msgId)) {
            resendMap.get(deviceId).put(msgId, resendObject);
        } else {
            Map<Integer, ResendObject> tempMap = new HashMap<Integer, ResendObject>();
            tempMap.put(msgId, resendObject);
            resendMap.put(deviceId, tempMap);
        }
    }
}
