package com.telek.hemsipc.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.telek.hemsipc.model.SysConfig;
import com.telek.hemsipc.util.StringUtil;

/**
 * 保存系统公用数据
 * @Class Name：SysContext    
 * @Class Description：    
 * @Creater：kds    
 * @Create Time：2018年11月14日下午5:33:00    
 * @Modifier：kds    
 * @Modification Time：2018年11月14日下午5:33:00    
 * @Remarks：
 */
public class SysContext {
    /**
     * 系统配置数据
     */
    private static Map<String, SysConfig> sysConfigMap = new ConcurrentHashMap<String, SysConfig>();
     
    public static SysConfig getSysConfig(String key) {
        return sysConfigMap.get(key);
    }
    
    public static void setSysConfig(String key, SysConfig sysConfig) {
        if(!StringUtil.isBlank(key)) {
            sysConfigMap.put(key, sysConfig);
        }
    }
    
}
