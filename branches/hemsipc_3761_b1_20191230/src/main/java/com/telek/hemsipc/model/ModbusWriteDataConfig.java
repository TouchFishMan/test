package com.telek.hemsipc.model;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * modbus协议写数据配置类
 * @Class Name：ModbusWriteDataConfig    
 * @Class Description：    
 * @Creater：kds    
 * @Create Time：2019年7月18日下午2:50:14    
 * @Modifier：kds    
 * @Modification Time：2019年7月18日下午2:50:14    
 * @Remarks：
 */
//@Entity
//@Table(name = "modbus_write_data_config")
public class ModbusWriteDataConfig implements Comparable<Object>{
    @Id
    @Column(name = "id")
    private Integer ID;
    
    @Column(name = "device_id")
    private String deviceID;
    
    ////开：on，关：off，频率：frequency，温度：temperature，阀门开度：openValue，远程控制：remoteControl，本地控制:localControl，
  //空调系统启动模式控制：airConditionModel，自动控制：autoControl，手动控制：manualControl，制冷模式：coldMode，制热模式：hotMode
 // 开：1，关：2，频率：3，温度：4，阀门开度：5，远程控制：6，本地控制:7，空调系统启动模式控制：8，自动控制：9，手动控制：10，制冷模式：11，制热模式：12
    
    @Column(name = "control_type")
    private String controlType; 
    
    @Column(name = "command")
    private String command; //指令
    
    @Column(name = "exists_placeholder")
    private int existsPlaceholder; //是否有占位符，有1，没有-1
    
    @Column(name = "placeholder_type")
    private String placeholderType; // 占位符类型：整数：int，浮点数：float
    
    @Column(name = "placeholder_length")
    private int placeholderLength; // 占位符长度（多少字节）
    
    @Column(name = "delay_time")
    private int delayTime; // 延迟时间(秒)
  
    @Column(name = "sort")
    private int sort; // 指令发送顺序，小的先发
    
    @Column(name = "multiplier")
    private int multiplier;
    
    @Column(name = "default_value")
    private float defaultValue;  //占位符默认值（如果存在占位符，但占位符没传值，则用此默认值）
    
    @Column(name = "is_only_modify_bit")
    private int isOnlyModifyBit; // 要修改的寄存器是否除了被修改位，要保留其他未值不变
    
    @Column(name = "modify_bit_register_add")
    private int modifyBitRegisterAdd; // 旧值的寄存器的位置
    
    @Column(name = "modify_bit_position")
    private int modifyBitPosition; // 要修改的位的位置
    
    @Column(name = "new_bit_value")
    private int newBitValue; // 要修改成什么值，0 还是1
    
    
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

	public String getControlType() {
		return controlType;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public int getExistsPlaceholder() {
		return existsPlaceholder;
	}

	public void setExistsPlaceholder(int existsPlaceholder) {
		this.existsPlaceholder = existsPlaceholder;
	}

	public String getPlaceholderType() {
		return placeholderType;
	}

	public void setPlaceholderType(String placeholderType) {
		this.placeholderType = placeholderType;
	}

 

	public int getPlaceholderLength() {
		return placeholderLength;
	}

	public void setPlaceholderLength(int placeholderLength) {
		this.placeholderLength = placeholderLength;
	}

	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}

	public float getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(float defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getIsOnlyModifyBit() {
		return isOnlyModifyBit;
	}

	public void setIsOnlyModifyBit(int isOnlyModifyBit) {
		this.isOnlyModifyBit = isOnlyModifyBit;
	}

	public int getModifyBitRegisterAdd() {
		return modifyBitRegisterAdd;
	}

	public void setModifyBitRegisterAdd(int modifyBitRegisterAdd) {
		this.modifyBitRegisterAdd = modifyBitRegisterAdd;
	}

	public int getModifyBitPosition() {
		return modifyBitPosition;
	}

	public void setModifyBitPosition(int modifyBitPosition) {
		this.modifyBitPosition = modifyBitPosition;
	}

	public int getNewBitValue() {
		return newBitValue;
	}

	public void setNewBitValue(int newBitValue) {
		this.newBitValue = newBitValue;
	}

	@Override
	public int compareTo(Object o) {
		ModbusWriteDataConfig confg = (ModbusWriteDataConfig)o;
		if (this.sort > confg.getSort() ) {
			return 1;
		}else if(this.sort == confg.getSort() ){
			return 0;
		}
		return -1;
	}
    
   
}
