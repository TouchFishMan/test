package com.telek.hemsipc.model;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 点表配置表
 * 
 * @Class Name：SysPointTableConfig
 * @Class Description：
 * @Creater：kds
 * @Create Time：2018年11月13日下午4:59:17
 * @Modifier：kds
 * @Modification Time：2018年11月13日下午4:59:17
 * @Remarks：
 */
//@Entity
//@Table(name = "sys_point_table_config")
public class SysPointTableConfig {
    @Id
    @Column(name = "id")
    private int id;
    
    @Column(name = "information_address")
    private int informationAddress;
    
    @Column(name = "common_address")
    private String commonAddress;
    
    @Column(name = "multiplier")
    private int multiplier; // 倍率（乘数）
    
    @Column(name = "sdmp_key")
    private int sdmpKey; //SDMP协议的key
    
    @Column(name = "collection_device_id")
    private String collectionDeviceId; // 采集的设备ID（不是云端的设备ID）

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInformationAddress() {
        return informationAddress;
    }

    public void setInformationAddress(int informationAddress) {
        this.informationAddress = informationAddress;
    }

    public String getCommonAddress() {
        return commonAddress;
    }

    public void setCommonAddress(String commonAddress) {
        this.commonAddress = commonAddress;
    }

    public int getSdmpKey() {
        return sdmpKey;
    }

    public void setSdmpKey(int sdmpKey) {
        this.sdmpKey = sdmpKey;
    }

    public String getCollectionDeviceId() {
        return collectionDeviceId;
    }

    public void setCollectionDeviceId(String collectionDeviceId) {
        this.collectionDeviceId = collectionDeviceId;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    
}
