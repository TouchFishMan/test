package com.telek.hemsipc.contant;

/**
 * @Auther: wll
 * @Date: 2018/9/17 09:02
 * @Description:
 */
public enum ProtocolType {
    DL645("dl645Protocol", "dl645Protocol协议"),
    //COLLECTION_CONTROL("controlProtocol", "采集控制柜协议"),
    MODBUS_RTU("modbusRTU", "modbus_RTU协议"),
    DL3761("376", "dl3761Protocol协议"),    ;

    private String code;
    private String type;

    ProtocolType(String code, String type) {
        this.code = code;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public static ProtocolType getDeviceTypeByCode(String code) {
        ProtocolType[] deviceTypes = ProtocolType.values();
        for (ProtocolType deviceType : deviceTypes) {
            if (deviceType.getCode().equals(code)) {
                return deviceType;
            }
        }
        return null;
    }
}
