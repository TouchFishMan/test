package com.telek.hemsipc.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Auther: wll
 * @Date: 2018/9/12 11:04
 * @Description:
 */
//@Entity
//@Table(name = "elec_data")
public class ElecData {
    public ElecData(String deviceId, String time) {
        this.deviceId = deviceId;
        this.time = time;
    }

    public ElecData() {
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "device_id")
    private String deviceId;
    
    @Column(name = "time")
    private String time;

    @Column(name = "collection_data")
    private String collectionData;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCollectionData() {
        return collectionData;
    }

    public void setCollectionData(String collectionData) {
        this.collectionData = collectionData;
    }

    @Override
    public String toString() {
        return "ElecData{" +
                "id=" + id +
                ", deviceId='" + deviceId + '\'' +
                ", time='" + time + '\'' +
                ", collectionData='" + collectionData + '\'' +
                '}';
    }
}
