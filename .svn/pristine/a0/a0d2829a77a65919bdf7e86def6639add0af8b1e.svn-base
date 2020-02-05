package com.telek.hemsipc.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放最近采集到的电量等数据
 * 
 * @Class CurrentDeviceData
 * @Class Description：
 * @Creater：kds
 * @Create Time：2018年11月13日下午2:47:05
 * @Modifier：kds
 * @Modification Time：2018年11月13日下午2:47:05
 * @Remarks：
 */
public class CurrentDeviceData {
 
    private Map<Integer, PointData> dataMap = new ConcurrentHashMap<Integer, PointData>(); // 存放采集到的数据，key是SDMP协议的key，value是实际采集到的数据
    
    public void setValue(Integer key, PointData value) {
        dataMap.put(key, value);
    }
    
    public PointData getValue(Integer key) {
        return dataMap.get(key);
    }

    public Map<Integer, PointData> getDataMap() {
        return dataMap;
    }
   
}
