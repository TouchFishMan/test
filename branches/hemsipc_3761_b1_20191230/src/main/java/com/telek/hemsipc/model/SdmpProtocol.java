package com.telek.hemsipc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: wll
 * @Date: 2018/9/14 10:32
 * @Description:
 */
@Entity
@Table(name = "sdmp_protocol")
public class SdmpProtocol {
    @Id
    @Column(name = "protocol_key")
    private int protocolKey;
    
    @Column(name = "protocol_remark")
    private String protocolRemark;

    @Column(name = "protocol_multiplier")
    private float protocolMultiplier;

    @Column(name = "protocol_data_type")
    private int protocolDataType;

    @Column(name = "value_offset")
    private float valueOffset;

    @Column(name = "protocol_key")
    public int getProtocolKey() {
        return protocolKey;
    }

    public void setProtocolKey(int protocolKey) {
        this.protocolKey = protocolKey;
    }

    public String getProtocolRemark() {
        return protocolRemark;
    }

    public void setProtocolRemark(String protocolRemark) {
        this.protocolRemark = protocolRemark;
    }

    public float getProtocolMultiplier() {
        return protocolMultiplier;
    }

    public void setProtocolMultiplier(float protocolMultiplier) {
        this.protocolMultiplier = protocolMultiplier;
    }

    public int getProtocolDataType() {
        return protocolDataType;
    }

    public void setProtocolDataType(int protocolDataType) {
        this.protocolDataType = protocolDataType;
    }

    public float getValueOffset() {
        return valueOffset;
    }

    public void setValueOffset(float valueOffset) {
        this.valueOffset = valueOffset;
    }

    @Override
    public String toString() {
        return "SdmpProtocol{" +
                "protocolKey=" + protocolKey +
                ", protocolRemark='" + protocolRemark + '\'' +
                ", protocolMultiplier=" + protocolMultiplier +
                ", protocolDataType=" + protocolDataType +
                ", valueOffset=" + valueOffset +
                '}';
    }
}
