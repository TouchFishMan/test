package com.telek.hemsipc.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.telek.hemsipc.contant.DeviceType;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.service.IIec104DataCollectionService;
import com.telek.hemsipc.util.StringUtil;

/**       
 * @Class Name：Iec104QuartzManager    
 * @Class Description：    
 * @Creater：lxf    
 * @Create Time：2018年10月23日下午4:40:57  
 * @Modification Time：2018年10月23日下午4:40:57    
 * @Remarks：        
 */
//@Component
public class Iec104QuartzManager {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IIec104DataCollectionService iec104DataCollectionService;
    
    /**
     * 定时每分钟从设备采集数据
     * @Modifier:kds 
     * @Date：2018年11月13日下午4:54:42 
     * @Describe:hearbeat
     */
    @Scheduled(cron = "0 * * * * ?")
    public void collectData() {
        try {
            List<Device> deviceList = DeviceContext.getDeviceByType(DeviceType.DEVICE_TYPE_IEC_104.getCode());
            if(StringUtil.isBlank(deviceList)) {
                return;
            }
            
            iec104DataCollectionService.sendQueryRequest();
        } catch (Exception e) {
            log.error("iec104设备心跳采集异常", e);
        }
         
    }
    
}

