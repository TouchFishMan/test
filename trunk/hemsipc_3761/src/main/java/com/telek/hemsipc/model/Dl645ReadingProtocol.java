package com.telek.hemsipc.model;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @Auther: wll
 * @Date: 2018/9/14 10:37
 * @Description:
 */
//@Entity
//@Table(name = "dl645reading_protocol")
public class Dl645ReadingProtocol {
    @Id
    @Column(name = "protocol_id")
    private String protocolId;
    
    @Column(name = "command")
    private String command;

    @Column(name = "remark")
    private String remark;

    @Column(name = "sdmp_protocol_key")
    private Integer sdmpProtocolKey;

    @Column(name = "data_pattern")
    private String dataPattern;

    @Column(name = "data_length")
    private Integer dataLength;

    @Column(name = "first_is_postive")
    private boolean firstIsPostive;

    @Column(name = "decode_type")
    private int decodeType;

    @Column(name = "block_datas")
    private String blockDatas;

    public String getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSdmpProtocolKey() {
        return sdmpProtocolKey;
    }

    public void setSdmpProtocolKey(Integer sdmpProtocolKey) {
        this.sdmpProtocolKey = sdmpProtocolKey;
    }

    public String getDataPattern() {
        return dataPattern;
    }

    public void setDataPattern(String dataPattern) {
        this.dataPattern = dataPattern;
    }

    public Integer getDataLength() {
        return dataLength;
    }

    public void setDataLength(Integer dataLength) {
        this.dataLength = dataLength;
    }

    public boolean isFirstIsPostive() {
        return firstIsPostive;
    }

    public void setFirstIsPostive(boolean firstIsPostive) {
        this.firstIsPostive = firstIsPostive;
    }

    public int getDecodeType() {
        return decodeType;
    }

    public void setDecodeType(int decodeType) {
        this.decodeType = decodeType;
    }

    public String getBlockDatas() {
        return blockDatas;
    }

    public void setBlockDatas(String blockDatas) {
        this.blockDatas = blockDatas;
    }

    @Override
    public String toString() {
        return "Dl645ReadingProtocol{" +
                "protocolId='" + protocolId + '\'' +
                ", command='" + command + '\'' +
                ", remark='" + remark + '\'' +
                ", sdmpProtocolKey='" + sdmpProtocolKey + '\'' +
                ", dataPattern='" + dataPattern + '\'' +
                ", dataLength=" + dataLength +
                ", firstIsPostive=" + firstIsPostive +
                ", decodeType=" + decodeType +
                ", blockDatas='" + blockDatas + '\'' +
                '}';
    }
}
