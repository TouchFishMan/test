package com.telek.hemsipc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @Auther: wll
 * @Date: 2018/9/14 15:21
 * @Description:
 */
@Entity
@Table(name = "device")
@Data
public class Device {
    /**
     * 设备id.
     */
    @Id
    @Column(name = "device_id")
    private String deviceId;
    
    /**
     * 设备名称.
     */
    @Column(name = "device_name")
    private String deviceName;
    /**
     * 设备key.
     */
    @Column(name = "genkey")
    private String genkey;
    /**
     * 设备msgId.
     */
    @Column(name = "msg_id")
    private int msgId;
    /**
     * 终端地址.
     * 3761协议该地址对应集中器地址
     */
    @Column(name = "slave_add")
    private String slaveAdd;
    /**
     * 设备类型.
     */
    @Column(name = "type")
    private int type;
 
    /**
     * 电压倍率.
     */
    @Column(name = "urate")
    private Integer urate;
    /**
     * 电流倍率.
     */
    @Column(name = "irate")
    private Integer irate;
    
    /**
     * 功率倍率.
     */
    @Column(name = "power_rate")
    private Integer powerRate;
    
    /**
     * 电量倍率.
     */
    @Column(name = "elec_rate")
    private Integer elecRate;
    
    /**
     * 查询channelId.
     */
    @Column(name = "channel_id")
    private String channelId;
 
    /**
     * 支持的采集指令.
     */
    @Column(name = "support_commands")
    private String supportCommands;

    /**
     *  最大功率.
     */
    @Column(name = "max_power_limit")
    private int maxPowerLimit;
    /**
     * 对应采集设备id.
     */
    @Column(name = "collection_device_id")
    private String collectionDeviceId;
    
    @Column(name = "slave_machine_num")
    private int slaveMachineNum; // modbus协议中从机地址， 3761协议代表电表的地址1-31
    
    @Column(name = "decode_protocol")
    private String decodeProtocol; // 解码协议
    
    @Column(name = "control_type")
    private String controlType;//控制类型（byCode代表通过写编码和电器编号方式控制，byWriteRegister代表用直接写寄存器方式控制
    
    @Column(name = "send_heart_bit")
    private int sendHeartBit; // 是否向云端发送心跳 1：是，-1 否
}
