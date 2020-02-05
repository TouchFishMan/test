package com.telek.hemsipc.model;

import com.telek.hemsipc.contant.MeterType;

public class CollectedData {
		private Float frequency;
		private Float electric;
		private Float power;
		private Integer voltage;
		private Integer state;
		private Integer secondOnOffState;
		private Integer thirdOnOffState;

		private Integer applianceFaultState; // 电器故障状态 1:故障，0：无故障
		private Integer secondApplianceFaultState; // 电器故障状态 1:故障，0：无故障
		private Integer thirdApplianceFaultState; // 电器故障状态 1:故障，0：无故障
		private Integer workingFaultState; // 运行故障状态 1:故障，0：无故障
		
		private Integer isAutoControl;//是否是自动控制 1:否：0：是
		private Integer isRemoteControl;//是否可远程控制 1：远程，0：本地 
		
		private Float temperature;//温度
		private Float openValue;//开度 
		private Float humidity;//湿度
		
		private Integer faultStateCode; //故障状态码
		private Float coolingWaterInputTemperature;// 冷却水回水温度
		private Float coolingWaterOutputTemperature;// 冷却水出水温度
		private Float chilledWaterInputTemperature;// 冷冻水回水温度
		private Float chilledWaterOutputTemperature;// 冷冻谁出水温度
		
		private Float pressure;//压力
		private Float evaporationPressure;//蒸发压力
		private Float condensePressure;//冷凝压力
		
		private Float electricPercentage;//电流百分比
		
		
		public Float getFrequency() {
			return frequency;
		}

		public void setFrequency(Float frequency) {
			this.frequency = frequency;
		}

		public Float getElectric() {
			return electric;
		}

		public void setElectric(Float electric) {
			this.electric = electric;
		}

		public Float getPower() {
			return power;
		}

		public void setPower(Float power) {
			this.power = power;
		}

		public Integer getVoltage() {
			return voltage;
		}

		public void setVoltage(Integer voltage) {
			this.voltage = voltage;
		}

		public Integer getState() {
			return state;
		}

		public void setState(Integer state) {
			this.state = state;
		}

		public Integer getSecondOnOffState() {
			return secondOnOffState;
		}

		public void setSecondOnOffState(Integer secondOnOffState) {
			this.secondOnOffState = secondOnOffState;
		}

		public Integer getThirdOnOffState() {
			return thirdOnOffState;
		}

		public void setThirdOnOffState(Integer thirdOnOffState) {
			this.thirdOnOffState = thirdOnOffState;
		}

		public Integer getApplianceFaultState() {
			return applianceFaultState;
		}

		public void setApplianceFaultState(Integer applianceFaultState) {
			this.applianceFaultState = applianceFaultState;
		}

		public Integer getSecondApplianceFaultState() {
			return secondApplianceFaultState;
		}

		public void setSecondApplianceFaultState(Integer secondApplianceFaultState) {
			this.secondApplianceFaultState = secondApplianceFaultState;
		}

		public Integer getThirdApplianceFaultState() {
			return thirdApplianceFaultState;
		}

		public void setThirdApplianceFaultState(Integer thirdApplianceFaultState) {
			this.thirdApplianceFaultState = thirdApplianceFaultState;
		}

		public Integer getWorkingFaultState() {
			return workingFaultState;
		}

		public void setWorkingFaultState(Integer workingFaultState) {
			this.workingFaultState = workingFaultState;
		}

		public Integer getIsAutoControl() {
			return isAutoControl;
		}

		public void setIsAutoControl(Integer isAutoControl) {
			this.isAutoControl = isAutoControl;
		}

		public Integer getIsRemoteControl() {
			return isRemoteControl;
		}

		public void setIsRemoteControl(Integer isRemoteControl) {
			this.isRemoteControl = isRemoteControl;
		}

		public Float getTemperature() {
			return temperature;
		}

		public void setTemperature(Float temperature) {
			this.temperature = temperature;
		}

		public Float getOpenValue() {
			return openValue;
		}

		public void setOpenValue(Float openValue) {
			this.openValue = openValue;
		}

		public Integer getFaultStateCode() {
			return faultStateCode;
		}

		public void setFaultStateCode(Integer faultStateCode) {
			this.faultStateCode = faultStateCode;
		}

		public Float getCoolingWaterInputTemperature() {
			return coolingWaterInputTemperature;
		}

		public void setCoolingWaterInputTemperature(Float coolingWaterInputTemperature) {
			this.coolingWaterInputTemperature = coolingWaterInputTemperature;
		}

		public Float getCoolingWaterOutputTemperature() {
			return coolingWaterOutputTemperature;
		}

		public void setCoolingWaterOutputTemperature(Float coolingWaterOutputTemperature) {
			this.coolingWaterOutputTemperature = coolingWaterOutputTemperature;
		}

		public Float getChilledWaterInputTemperature() {
			return chilledWaterInputTemperature;
		}

		public void setChilledWaterInputTemperature(Float chilledWaterInputTemperature) {
			this.chilledWaterInputTemperature = chilledWaterInputTemperature;
		}

		public Float getChilledWaterOutputTemperature() {
			return chilledWaterOutputTemperature;
		}

		public void setChilledWaterOutputTemperature(Float chilledWaterOutputTemperature) {
			this.chilledWaterOutputTemperature = chilledWaterOutputTemperature;
		}
		
		public Float getHumidity() {
			return humidity;
		}

		public void setHumidity(Float humidity) {
			this.humidity = humidity;
		}

		public Float getPressure() {
			return pressure;
		}

		public void setPressure(Float pressure) {
			this.pressure = pressure;
		}

		public Float getEvaporationPressure() {
			return evaporationPressure;
		}

		public void setEvaporationPressure(Float evaporationPressure) {
			this.evaporationPressure = evaporationPressure;
		}

		public Float getCondensePressure() {
			return condensePressure;
		}

		public void setCondensePressure(Float condensePressure) {
			this.condensePressure = condensePressure;
		}

		public Float getElectricPercentage() {
			return electricPercentage;
		}

		public void setElectricPercentage(Float electricPercentage) {
			this.electricPercentage = electricPercentage;
		}

		public void setValue(String meterName, float value) {
			if(meterName.equals(MeterType.frequency.getCode())) {
				this.setFrequency(value);
			}else if(meterName.equals(MeterType.electric.getCode())) {
				this.setElectric(value);
			}else if(meterName.equals(MeterType.power.getCode())) {
				this.setPower(value);
			}else if(meterName.equals(MeterType.voltage.getCode())) {
				this.setVoltage((int)value);
			}else if(meterName.equals(MeterType.state.getCode())) {
				this.setState((int)value);
			}else if(meterName.equals(MeterType.secondOnOffState.getCode())) {
				this.setSecondOnOffState((int)value);
			}else if(meterName.equals(MeterType.thirdOnOffState.getCode())) {
				this.setThirdOnOffState((int)value);
			}else if(meterName.equals(MeterType.applianceFaultState.getCode())) {
				this.setApplianceFaultState((int)value);
			}else if(meterName.equals(MeterType.secondApplianceFaultState.getCode())) {
				this.setSecondApplianceFaultState((int)value);
			}else if(meterName.equals(MeterType.thirdApplianceFaultState.getCode())) {
				this.setThirdApplianceFaultState((int)value);
			}else if(meterName.equals(MeterType.workingFaultState.getCode())) {
				this.setWorkingFaultState((int)value);
			}else if(meterName.equals(MeterType.isAutoControl.getCode())) {
				this.setIsAutoControl((int)value);
			}else if(meterName.equals(MeterType.isRemoteControl.getCode())) {
				this.setIsRemoteControl((int)value);
			}else if(meterName.equals(MeterType.temperature.getCode())) {
				this.setTemperature(value);
			}else if(meterName.equals(MeterType.humidity.getCode())) {
				this.setHumidity(value);
			}else if(meterName.equals(MeterType.openValue.getCode())) {
				this.setOpenValue(value);
			}else if(meterName.equals(MeterType.faultStateCode.getCode())) {
				this.setFaultStateCode((int)value);
			}else if(meterName.equals(MeterType.coolingWaterInputTemperature.getCode())) {
				this.setCoolingWaterInputTemperature(value);
			}else if(meterName.equals(MeterType.coolingWaterOutputTemperature.getCode())) {
				this.setCoolingWaterOutputTemperature(value);
			}else if(meterName.equals(MeterType.chilledWaterInputTemperature.getCode())) {
				this.setChilledWaterInputTemperature(value);
			}else if(meterName.equals(MeterType.chilledWaterOutputTemperature.getCode())) {
				this.setChilledWaterOutputTemperature(value);
			}else if(meterName.equals(MeterType.pressure.getCode())) {
				this.setPressure(value);
			}else if(meterName.equals(MeterType.evaporationPressure.getCode())) {
				this.setEvaporationPressure(value);
			}else if(meterName.equals(MeterType.condensePressure.getCode())) {
				this.setCondensePressure(value);
			}else if(meterName.equals(MeterType.electricPercentage.getCode())) {
				this.setElectricPercentage(value);
			}
		}
		
	}