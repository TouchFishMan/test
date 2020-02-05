package com.telek.hemsipc.model;

public class ControlState {
    private String deviceId;
    private int controlType;//控制类型
    
    private int onOffStatus; //开关状态
    private Integer frequence; //频率
    

    public ControlState(String deviceId, int controlType, int status) {
        this.deviceId = deviceId;
        this.controlType = controlType;
        this.onOffStatus = status;
    }
    
    public ControlState(String deviceId, int controlType, int status, Integer frequence) {
        this.deviceId = deviceId;
        this.controlType = controlType;
        this.onOffStatus = status;
        this.frequence = frequence;
    }

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getControlType() {
		return controlType;
	}

	public void setControlType(int controlType) {
		this.controlType = controlType;
	}

	public int getOnOffStatus() {
		return onOffStatus;
	}

	public void setOnOffStatus(int onOffStatus) {
		this.onOffStatus = onOffStatus;
	}

	public Integer getFrequence() {
		return frequence;
	}

	public void setFrequence(Integer frequence) {
		this.frequence = frequence;
	}

	public boolean equals(Object obj) {
		if(obj instanceof ControlState) {
			if(this.deviceId != null) {
				return this.deviceId.equals(((ControlState)obj).getDeviceId());
			}
		}
		return false;
	}
    
}
