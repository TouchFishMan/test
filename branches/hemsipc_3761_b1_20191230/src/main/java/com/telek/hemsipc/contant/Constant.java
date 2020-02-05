package com.telek.hemsipc.contant;

/**
 * @Auther: wll
 * @Date: 2018/9/17 09:11
 * @Description:
 */
public class Constant {
    /**
     * controlState:云端是否可以控制水泵
     */
    public static volatile boolean CONTROL_STATE = true;
    /**
     * 系统在线标识.
     */
    public static volatile boolean SERVER_ONLINE = false;
    /**
     * 接受云端上次恢复消息时间，单位毫秒。（用于长时间失联时重启网络）
     */
    public static long LAST_NET_TIME = 0;
    /**
     * udp离线换端口次数.
     */
    public static int CHANGE_PORT_TIMES = 0;
    /**
     * 强制重启网络次数.
     */
    public static int FORCE_RESET_NETWORK = 0;
    
    
    public final static int YES = 1; //是
    public final static int NO = -1; //否
    
    public final static int IS_ERROR = 1;

    public final static String WRITE_CONFIG_TYPE_BY_CODE = "byCode"; // 代表通过写编码和电器编号方式控制
    public final static String WRITE_CONFIG2_TYPE_BY_WRITE_REGISTER = "byWriteRegister"; //直接写寄存器方式控制
  
    
    
    public static final int OFF = 0; //电器关
    public static final int ON = 1; // 电器开
    
    public static final int MODEL_COLD = 1;// 制冷模式
    public static final int MODEL_HOT = 0;//制热模式
    
    public static final int REMOTE_CONTROL = 0;// 远程控制
    public static final int LOCAL_CONTROL = 1;// 本地控制
    
    public static final int HAND_CONTROL = 0; //手动控制
    public static final int AUTO_CONTROL = 1; //自动控制
    
    public static final String SPLIT_SIGN_COMMA = ","; //逗号
    
    // 下面对应配置表中配置名称
    public final static String IP = "ip_104";
    public final static String PORT = "port_104";
    public final static String COMMON_ADDRESS= "common_address_104";
    public final static String OFF_LINE_TIME = "off_line_time"; // 多长时间没有采集到数据算离线（单位：秒）
    public final static String NEED_COMBINE_KEY = "need_combine_key"; // 需要合并的数据对应SDMP协议的key
    public final static String CREATE_POWER_APPARENT_WITH_POWER_ACTIVE = "create_apparentPower_with_activePower";//是否用有功功率生成视在功率,是：1,否：-1

    public static final String SIMPLE_SINGAL_0 = "simple_singal_0"; // 配置表中键----simple信号等于0时削减负荷到原来百分之几
    public static final String SIMPLE_SINGAL_1 = "simple_singal_1"; //配置表中键----simple信号等于0时削减负荷到原来百分之几
    public static final String SIMPLE_SINGAL_2 = "simple_singal_2"; //配置表中键----simple信号等于0时削减负荷到原来百分之几
    public static final String SIMPLE_SINGAL_3 = "simple_singal_3"; //配置表中键----simple信号等于0时削减负荷到原来百分之几
    
    public static final String CONTROL_REGISTER_START_ADDRESS = "control_register_start_address";//控制数据写到从哪个寄存器开始的寄存器中
    public static final String NEED_EXECUTE_CUSTOMIZED_TASK = "need_execute_customized_task";//是否需要执行个性化任务，1：是，-1：否
    
}
