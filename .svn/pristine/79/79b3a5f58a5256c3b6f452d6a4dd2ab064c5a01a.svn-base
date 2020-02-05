package com.telek.hemsipc.model;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 模拟数据对象
 * @Class Name：SimulationData    
 * @Class Description：    
 * @Creater：kds    
 * @Create Time：2019年7月18日上午9:23:19    
 * @Modifier：kds    
 * @Modification Time：2019年7月18日上午9:23:19    
 * @Remarks：
 */
//@Entity
//@Table(name = "simulation_data")
public class SimulationData {
    
    @Id
    @Column(name = "device_id")
    private String deviceID;
    
    @Column(name = "power")
    private int power;
    
    @Column(name = "voltage")
    private int voltage;
 
    @Column(name = "sum_elec")
    private float sumElec;
    
    @Column(name = "temperature")
    public float temperature;
    
    @Column(name = "state")
    public int state;
    
    @Column(name = "is_remote_control")
    public int isRemoteControl;
    
    @Column(name = "model")
    public int model;  //1：制冷，0：制热
    
    @Column(name = "open_value")
    public int openValue;  
    
    public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceId) {
		this.deviceID = deviceId;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public float getSumElec() {
		return sumElec;
	}

	public void setSumElec(float sumElec) {
		this.sumElec = sumElec;
	}

	public int getVoltage() {
		return voltage;
	}

	public void setVoltage(int voltage) {
		this.voltage = voltage;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getIsRemoteControl() {
		return isRemoteControl;
	}

	public void setIsRemoteControl(int isRemoteControl) {
		this.isRemoteControl = isRemoteControl;
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public int getOpenValue() {
		return openValue;
	}

	public void setOpenValue(int openValue) {
		this.openValue = openValue;
	}

    
}
