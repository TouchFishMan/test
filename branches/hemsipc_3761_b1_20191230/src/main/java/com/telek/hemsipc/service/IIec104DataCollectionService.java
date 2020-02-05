package com.telek.hemsipc.service;  

/**
 * 按104协议采集终端数据service接口类
 * @Class Name：IIec104DataCollectionService    
 * @Class Description：    
 * @Creater：kds    
 * @Create Time：2018年11月14日下午4:46:50    
 * @Modifier：kds    
 * @Modification Time：2018年11月14日下午4:46:50    
 * @Remarks：
 */
public interface IIec104DataCollectionService {
    
    /**
     * 发送查询请求
     * @Modifier:kds 
     * @Date：2018年11月14日下午4:50:10 
     * @Describe:sendQueryRequest
     */
    public void sendQueryRequest();
}
  
