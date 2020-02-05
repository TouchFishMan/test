package com.telek.hemsipc.contant;  
public enum MeterType {
	frequency("frequency", "频率"),
 	electric("electric", "电流"),
    power("power", "功率"),
    voltage("voltage", "电压"),
    state("state", "开关状态"),
    secondOnOffState("secondOnOffState", "第二个开关状态"),
    thirdOnOffState("thirdOnOffState", "第三个开关状态"),
    applianceFaultState("applianceFaultState", "电器故障状态"),
    secondApplianceFaultState("secondApplianceFaultState", "第二个电器故障状态"),
    thirdApplianceFaultState("thirdApplianceFaultState", "第三个电器故障状态"),
    workingFaultState("workingFaultState", "运行故障状态"),
    isAutoControl("isAutoControl", "是否是自动控制"),
    isRemoteControl("isRemoteControl", "是否是本机（远程）控制"),
	temperature("temperature", "温度") , 
	humidity("humidity", "湿度") , 
	openValue("openValue", "开度"),
	faultStateCode("faultStateCode", "故障代码"),
	coolingWaterInputTemperature("coolingWaterInputTemperature", "冷却水回水温度"),
	coolingWaterOutputTemperature("coolingWaterOutputTemperature", "冷却水出水温度"),
	chilledWaterInputTemperature("chilledWaterInputTemperature", "冷冻水回水温度"),
	chilledWaterOutputTemperature("chilledWaterOutputTemperature", "冷冻水出水温度"),
	pressure("pressure","压力"),
	evaporationPressure("evaporationPressure", "蒸发压力"),
	condensePressure("condensePressure", "冷凝压力"),
	electricPercentage("electricPercentage", "电流百分比");
 

    private String code;
    private String type;

    MeterType(String code, String type) {
        this.code = code;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public static MeterType getDataTypeByCode(String code) {
    	MeterType[] dataTypes = MeterType.values();
        for (MeterType dataType : dataTypes) {
            if (dataType.getCode().equals(code)) {
                return dataType;
            }
        }
        return null;
    }
}
  
