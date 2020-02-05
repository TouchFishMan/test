package com.telek.hemsipc.context;  

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.telek.hemsipc.model.SysPointTableConfig;

/**
 * 104采集协议相关公共数据
 * @Class Name：Iec104Context    
 * @Class Description：    
 * @Creater：kds    
 * @Create Time：2018年11月14日下午3:28:46    
 * @Modifier：kds    
 * @Modification Time：2018年11月14日下午3:28:46    
 * @Remarks：
 */
public class Iec104Context {
 
    /**
     * 保存系统点表配置数据 ( key是 informationAddress+":"+commonAddress  来源是device.channelId )
     */ 
    private static Map<Integer,SysPointTableConfig> sysPointTableConfigMap = new ConcurrentHashMap<Integer, SysPointTableConfig>();
    
 
    
    public static SysPointTableConfig getSysPointTableConfigMap(Integer id) {
        return sysPointTableConfigMap.get(id);
    }
    
    public static void setSysPointTableConfig(SysPointTableConfig sysPointTableConfig) {
        if(sysPointTableConfig != null) {
            sysPointTableConfigMap.put(sysPointTableConfig.getInformationAddress(), sysPointTableConfig);
        }
    }
    
}
  
