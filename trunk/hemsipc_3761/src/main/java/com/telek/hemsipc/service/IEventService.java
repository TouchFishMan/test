package com.telek.hemsipc.service;

import com.telek.hemsipc.model.DrEvent;

/**
 * 事件服务接口类
 * @Class Name：IEventService    
 * @Class Description：    
 * @Creater：telek    
 * @Create Time：2019年5月7日下午4:39:44    
 * @Modifier：telek    
 * @Modification Time：2019年5月7日下午4:39:44    
 * @Remarks：
 */
public interface IEventService {
    

    /**
     * 事件执行逻辑
     * @Modifier:kds 
     * @Date：2019年5月7日下午4:39:44   
     * @Describe:excuteEvent      
     * @param event
     */
    public void excuteEvent(DrEvent event);

    /**
     * 事件执行结束恢复电器原来的状态
     * @Modifier:kds 
     * @Date：2019年5月7日下午4:39:44   
     * @Describe:recoveryEvent      
     * @param event
     */
    public void recoveryEvent(DrEvent event);
    
    /**
     * 更新事件
     * @Modifier:kds 
     * @Date：2019年5月7日下午4:39:44   
     * @Describe:updateEventState      
     * @param event
     */
    public void updateEvent(DrEvent event);
    
    
}
