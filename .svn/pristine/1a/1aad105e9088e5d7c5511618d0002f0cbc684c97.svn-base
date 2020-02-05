package com.telek.hemsipc.service;

import com.telek.hemsipc.model.Device;

public interface IControlService {

    /**
     * 控制锅炉
     *
     * @Author:cyf
     * @Date:2019年6月18日
     * @param deviceID
     */
    public void controlBoiler(String deviceID, int status);
    

    /**
     * 根据配置发送控制指令，对于没有占位符的指令，参数value无用
     * @Author:kds 
     * @Date：2019年7月21日下午11:31:52 
     * @Describe:controlByConfig  
     * @throws    
     * @param controlType
     * @param device
     * @param value
     */
    public void control(String controlType, Device device, Float value);
    
}
