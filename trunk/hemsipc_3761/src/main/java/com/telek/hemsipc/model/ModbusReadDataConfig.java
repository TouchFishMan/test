package com.telek.hemsipc.model;

import javax.persistence.Column;
import javax.persistence.Id;


/**
 * modbus协议读数据配置类
 * @Class Name：ModbusReadDataConfig    
 * @Class Description：    
 * @Creater：kds    
 * @Create Time：2019年7月2日上午10:58:24    
 * @Modifier：kds    
 * @Modification Time：2019年7月2日上午10:58:24    
 * @Remarks：
 */
//@Entity
//@Table(name = "modbus_read_data_config")
public class ModbusReadDataConfig {
    @Id
    @Column(name = "id")
    private Integer ID;

    @Column(name = "device_id")
    private String deviceID;

    /**寄存器起始位地址*/
    @Column(name = "register_start_address")
    private int registerStartAddress;

    /**寄存器读取个数*/
    @Column(name = "register_num")
    private int registerNum;

    /**测量类型*/
    @Column(name = "metering_type")
    private String meteringType;

    /**寄存器中的第几位*/
    @Column(name = "start_position")
    private int startPosition;

    /**测量数据长度*/
    @Column(name = "metering_data_length")
    private int meteringDataLength;


    /**倍数*/
    @Column(name = "multiplier")
    private float multiplier;

    /**数据类型*/
    @Column(name = "data_type")
    private String dataType;

    /**是否为bit类型*/
    @Column(name = "is_bit_type")
    private int isBitType;

    /**bit位*/
    @Column(name = "bit_position")
    private int bitPosition;

	/**是否需要倒置*/
    @Column(name = "need_invert")
    private int needInvert;

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public int getRegisterStartAddress() {
		return registerStartAddress;
	}

	public void setRegisterStartAddress(int registerStartAddress) {
		this.registerStartAddress = registerStartAddress;
	}

	public int getRegisterNum() {
		return registerNum;
	}

	public void setRegisterNum(int registerNum) {
		this.registerNum = registerNum;
	}

	public String getMeteringType() {
		return meteringType;
	}

	public void setMeteringType(String meteringType) {
		this.meteringType = meteringType;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public int getMeteringDataLength() {
		return meteringDataLength;
	}

	public void setMeteringDataLength(int meteringDataLength) {
		this.meteringDataLength = meteringDataLength;
	}

	public float getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(float multiplier) {
		this.multiplier = multiplier;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getIsBitType() {
		return isBitType;
	}

	public void setIsBitType(int isBitType) {
		this.isBitType = isBitType;
	}

	public int getBitPosition() {
		return bitPosition;
	}

	public void setBitPosition(int bitPosition) {
		this.bitPosition = bitPosition;
	}

	public int getNeedInvert() {
		return needInvert;
	}

	public void setNeedInvert(int needInvert) {
		this.needInvert = needInvert;
	}
  
   
}
