package com.telek.hemsipc.contant;

/**
 * 控制类型
 *
 * @Class Name：ControlType
 * @Class Description：
 * @Creater：kds
 * @Create Time：2019年7月18日下午3:21:56
 * @Modifier：kds
 * @Modification Time：2019年7月18日下午3:21:56
 * @Remarks：
 */

public enum ControlType {
    ON("on", "电器开"),
    OFF("off", "电器关"),
    FREQUENCY("frequency", "频率"),
    TEMPERATURE("temperature", "温度"),
    OPEN_VALUE("openValue", "阀门开度"),
    REMOTE_CONTROL("remoteControl", "远程控制"),
    LOCAL_CONTROL("localControl", "本地控制"),
    AIR_CONDITION_MODEL("airConditionModel", "空调系统启动模式控制"),
    AUTO_CONTROL("autoControl", "自动控制"),
    MANUAL_CONTROL("manualControl", "手动控制"),
    COLD("coldMode", "制冷模式"),
    HOT("hotMode", "制热模式"),
    PRESSURE("pressure", "压力"),
    SET_OFFSET("setOffset", "设置偏差");//这个目前只给金鹰温度采集器用，临时使用，温度采集准确后要去掉

    private String name;
    private String remark;

    ControlType(String name, String remark) {
        this.name = name;
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public String getRemark() {
        return remark;
    }

//    public static ControlType getControlTypeByCode(String name) {
//        ControlType[] deviceTypes = ControlType.values();
//        for (ControlType deviceType : deviceTypes) {
//            if (name.equals(deviceType.getName())) {
//                return deviceType;
//            }
//        }
//        return null;
//    }
    
}
