package com.telek.hemsipc.sdmp;

/**
 * @Auther: wll
 * @Date: 2018/9/14 13:17
 * @Description:
 */
public class SdmpConstant {
	/**
     * 工控机版本号.
     */
    public static int DEVICE_VERSION = 2;
    /**
     * SDMP键值对中的值是数值型
     */
    public static final int PDU_TYPE_NUMBER = 1; 
    
    /**
     * SDMP键值对中的值是字符串型
     */
    public static final int PDU_TYPE_STRING = 2; 
	
	/**------------------------设备到云端开始---------------------------------**/
	public static final int PDU_FAULT_STATE_CODE = 269; //故障代码
    public static final int PDU_COOLING_WATER_INPUT_TEMPERATURE = 266;  // 冷却水回水温度
    public static final int PDU_COOLING_WATER_OUTPUT_TEMPERATURE = 265;  // 冷却水出水温度
    public static final int PDU_CHILLED_WATER_INPUT_TEMPERATURE = 268;  //  冷冻水回水温度
    public static final int PDU_CHILLED_WATER_OUTPUT_TEMPERATURE = 267;  // 冷冻水出水温度
    public static final int PDU_ELEC = 1; //电量
    public static final int PDU_ELEC_POSITIVE_REACTIVE_POWER = 204; // 采集器组合无功总电能(正向无功)
    public static final int PDU_ELEC_NEGATIVE_REACTIVE_POWER = 205; //采集器组合无功总电能(反向无功)
    public static final int PDU_APPARENT_POWER = 2; // 视在功率
    public final static int PDU_ACTIVE_POWER = 3;  //有功功率
    public static final int PDU_U = 5; // 电压
    public static final int PDU_U_A = 51; //A相电压
    public static final int PDU_U_B = 52; //B相电压
    public static final int PDU_U_C = 53; // C相电压
    public static final int PDU_I = 6; //电流
    public static final int PDU_I_A = 54; //A相电流
    public static final int PDU_I_B = 55; //B相电流
    public static final int PDU_I_C = 56; // C相电流
    public static final int PDU_S_ISACTIVE = 9; //设备是否激活
    public static final int PDU_MAC_ADD = 25; //MAC地址
    public static final int PDU_STATE = 100; //变频器启停状态
    public static final int PDU_APPLIANCE_FAULT_STATE= 254; //电器故障状态
    public static final int PDU_WORKING_FAULT_STATE = 253; //运行故障状
    public static final int PDU_ELEC_ACTIVE_POWER = 202;// 正相有功总电能
    public final static int PDU_ACTIVE_POWER_TOTAL = 226;  //总有功功率
    public final static int PDU_ACTIVE_POWER_A = 227;  //A相有功功率
    public final static int PDU_ACTIVE_POWER_B = 228;  //B相有功功率
    public final static int PDU_ACTIVE_POWER_C = 229; // C相有功功率
    public static final int PDU_APPARENT_POWER_TOTAL = 234;
    public static final int PDU_APPARENT_POWER_A = 235;
    public static final int PDU_APPARENT_POWER_B = 236;
    public static final int PDU_APPARENT_POWER_C = 237;
    public static final int PDU_EVAPORATION_PRESSURE = 270; // 蒸发压力
    public static final int PDU_CONDENSE_PRESSURE = 271; //冷凝压力
    public static final int PDU_ELECTRIC_PERCENTAGE = 272; //电流百分比
    public static final int PDU_IS_AUTO_CONTROL = 256; // 是否是自动控制
 
    
	/**------------------------设备到云端结束---------------------------------**/
	
	
	/**------------------------云端到设备开始---------------------------------**/
    public static final int PDU_AIR_MODEL = 264; //中央空调启动模式(模式1，模式2，模式3等等)
    public static final int PDU_AIR_TERMINAL_COLD_HOT_MODEL = 262; //空调末端制冷制热切换的key
    public static final int PDU_PROGRAM_DOWN_FIRMWAREVERSION = 71; //：固件版本号
    public static final int PDU_IPC_FIRMWARE_MD5 = 299; //固件包MD5校验值.
    public static final int PDU_IPC_FIRMWARE_DOWNLOAD_KEY = 298; //固件包下载密钥.
    public static final int PDU_ONE_KEY_ON_OFF = 257; //中央空调一键启动或停止
	/**------------------------云端到设备结束---------------------------------**/
	
	
	/**------------------------云端到设备和设备到云端开始---------------------------------**/
    public static final int PDU_C_STATE = 7;// 状态值（0：关，1：开）的键值
    public static final int PDU_TEMPERATURE = 15; //温度
    public static final int PDU_HUMIDITY = 85; //湿度
    public static final int PDU_OPEN_VALUE = 261; //水阀开度
    public static final int PDU_POWER_PERCENT = 101; //变频器功率百分比
    public static final int PDU_FREQUENC = 102; //变频器频率
    public static final int PDU_IS_REMOTE_CONTROL = 255; //是否是远程（手动）控制
	/**------------------------云端到设备和设备到云端结束---------------------------------**/
    
    
    /********************************* requestType开始 ********************************************************/
    public static final int REQUESTID_TRAP = 2; // Trap
    public static final int REQUESTID_REPORT = 3; //REPORT
    public static final int REQUESTID_COMMAND = 4;  //COMMAND
    public static final int REQUESTID_RESPONSE = 6; //RESPONSE
    /********************************* requestType结束 ********************************************************/

    /**------------------------actiontype开始---------------------------------**/
    public static final int SDMP_RESPONSE_ACTIVE = 2; //云端激活响应
    public static final int SDMP_RESPONSE_HEARTBEAT = 101; //心跳包反馈
    public static final int SDMP_COMMAND_RELAY = 3; //继电器打开/关闭指令的ActionType值
    public static final int SDMP_COMMAND_POWER_PERCENT_CONTROL = 34; // 变频柜频率控制 ActionType值
    public static final int SDMP_COMMAND_FREQUENCY_CONVERTER_CONTROL = 35; //变频柜 启停控制 ActionType值
    public static final int SDMP_COMMNAD_IPC_UPDATE = 36; // 工控机固件升级
    public static final int SDMP_COMMNAD_CONVERT_LOCAL_REMOTE_CONTROL = 37; //本地控制与远程控制切换
    public static final int SDMP_COMMNAD_CONVERT_MANUAL_AUTO_CONTROL = 38; //自动控制与手动控制切换
    public static final int SDMP_COMMNAD_ONE_KEY_ON_OFF = 39; //一键启动停止
    public static final int SDMP_COMMNAD_AIR_MODEL_CONTROL = 42; //空调系统运行模式
    public static final int SDMP_COMMNAD_AIR_TERMINAL_MODEL_CONTROL = 43; //空调末端制冷制热切换
    public static final int SDMP_COMMNAD_TEMPERATURE = 41; //温度设置
    public static final int SDMP_COMMNAD_OPEN_VALUE = 40; //水阀开度设置
    public static final int SDMP_RESPONSE_SUCCESS = 0; // 设备正常执行指令
    public static final int SDMP_RESPONSE_ERROR = 1; //设备执行指令错误
    public static final int DEVICE_REQUEST_ACTIVE = 0; //设备请求激活
    public static final int DEVICE_ONLINE_NOTIFY = 2; //设备上线通知
    /**------------------------actiontype结束---------------------------------**/
    
}
