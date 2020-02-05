package com.telek.hemsipc.model;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 控制配置类
 * @Class Name：ControlConfig    
 * @Class Description：    
 * @Creater：kds    
 * @Create Time：2019年10月18日上午11:08:45    
 * @Modifier：kds    
 * @Modification Time：2019年10月18日上午11:08:45    
 * @Remarks：
 */

//@Entity
// @Table(name = "control_config")
public class ControlConfig {
 
	@Id
	@Column(name = "device_id")
	private String deviceID;

	@Column(name = "control_type")
	private String controlType; // 控制类型，开、关、调频率等
	
	@Column(name = "control_code")
	private int controlCode; //功能码
	
	@Column(name = "register_num") //寄存器编号
	private int registerNum;
	
	@Column(name = "multiplier") //乘数
	private float multiplier;
	
	@Column(name = "exists_placeholder")//  是否存在占位符，1：存在， -1：不存在
	private int existsPlaceholder;
	
	
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

	public int getRegisterNum() {
		return registerNum;
	}

	public void setRegisterNum(int registerNum) {
		this.registerNum = registerNum;
	}

	public float getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(float multiplier) {
		this.multiplier = multiplier;
	}

	public int getExistsPlaceholder() {
		return existsPlaceholder;
	}

	public void setExistsPlaceholder(int existsPlaceholder) {
		this.existsPlaceholder = existsPlaceholder;
	}

	public int getControlCode() {
		return controlCode;
	}

	public void setControlCode(int controlCode) {
		this.controlCode = controlCode;
	}
 
}
