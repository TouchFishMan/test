package com.telek.hemsipc.contant;

/**
 * @Auther: wll
 * @Date: 2018/9/17 09:02
 * @Description:
 */
public enum DeviceType {
    DEVICE_TYPE_COLLECTION(1, "采集柜"),
   // DEVICE_TYPE_COLLECTION_CONTROL(2, "采集控制柜"),
    DEVICE_TYPE_YIRUI_PLC_COOLING_WATER_PUMP(3, "冷冻水泵"),
    DEVICE_TYPE_YIRUI_PLC_CHILLED_WATER_PUM(4, "冷却水泵"),
    DEVICE_TYPE_IEC_104(5, "采集终端，IEC-104协议设备"),
    DEVICE_TYPE_BOILER(6, "锅炉"),
    DEVICE_TYPE_FREQUENCY_CONVERSION(7, "风机变频"),
    DEVICE_TYPE_SWITCH_OF_WATER(8, "水阀"),
    DEVICE_TYPE_COOLING_TOWER(9, "冷却塔"),
    DEVICE_TYPE_CENTRAL_AIR_CONDITIONING_SYSTEM(10, "中央空调系统"),
    //DEVICE_TYPE_BOILER_CABINET(11, "锅炉实证柜"),
    DEVICE_TYPE_TEMPERATURE_SENSOR(12, "温度传感器"),
    DEVICE_TYPE_FREEZING_MACHINE(13, "冷水机组"),
    //DEVICE_TYPE_TEMPERATURE_SENSOR(14, "温度传感器"),
    DEVICE_TYPE_AIR_CONDITIONER_TERMINAL(15, "空调末端");
	
    private int code;
    private String type;

    DeviceType(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public static DeviceType getDeviceTypeByCode(int code) {
        DeviceType[] deviceTypes = DeviceType.values();
        for (DeviceType deviceType : deviceTypes) {
            if (deviceType.getCode() == code) {
                return deviceType;
            }
        }
        return null;
    }
}
