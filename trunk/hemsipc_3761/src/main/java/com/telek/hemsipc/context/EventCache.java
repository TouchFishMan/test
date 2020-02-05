package com.telek.hemsipc.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.telek.hemsipc.model.ControlState;

public class EventCache {
    
    /**  待恢复的设备被列表 结构是<事件ID，<设备ID，EventRecovery对象>>   */
    public static Map<String, List<ControlState>> eventRecoverys = new ConcurrentHashMap<String, List<ControlState>>();
    
    /** 频率控制执行了几次 (key：事件ID， value：次数)*/
    public static Map<String, Integer> frequenceControlNumMap = new ConcurrentHashMap<String, Integer>();
    

    public static void putRecovery(String eventID, ControlState eventRecovery) {
    	List<ControlState>  recoveryStates = eventRecoverys.get(eventID);
        if (recoveryStates == null) {
        	recoveryStates = new ArrayList<ControlState>();
            eventRecoverys.put(eventID, recoveryStates);
        }
        if(!recoveryStates.contains(eventRecovery)) { // 只有第一次有效
        	recoveryStates.add(eventRecovery);
        }
    }
    
    public static void clearEventCache(String eventID) {
    	eventRecoverys.remove(eventID);
    	frequenceControlNumMap.remove(eventID);
    }

}
