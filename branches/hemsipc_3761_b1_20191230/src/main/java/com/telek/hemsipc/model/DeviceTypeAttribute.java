package com.telek.hemsipc.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
//@Table(name = "device_type_attribute")
public class DeviceTypeAttribute {
    @Id
    @Column(name = "device_type") //设备类型编码
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String deviceType;
    
    @Column(name = "type_name")
    private String typeName; //设备类型名称
    
    @Column(name = "type_attribute")
    private String typeAttribute; //设备类型属性
    
    @Column(name = "is_support_percent_control")
    private int isSupportPercentControl; //是否支持百分比控制
    
    @Column(name = "is_support_onoff_control")
    private int isSupportOnoffControl; //是否支持开关控制

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getTypeAttribute() {
        return typeAttribute;
    }

    public void setTypeAttribute(String deviceAttribute) {
        this.typeAttribute = deviceAttribute;
    }

    public int getIsSupportPercentControl() {
        return isSupportPercentControl;
    }

    public void setIsSupportPercentControl(int isSupportPercentControl) {
        this.isSupportPercentControl = isSupportPercentControl;
    }

    public int getIsSupportOnoffControl() {
        return isSupportOnoffControl;
    }

    public void setIsSupportOnoffControl(int isSupportOnoffControl) {
        this.isSupportOnoffControl = isSupportOnoffControl;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    
    
}
