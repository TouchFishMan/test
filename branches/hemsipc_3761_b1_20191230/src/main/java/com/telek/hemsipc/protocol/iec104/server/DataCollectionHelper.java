package com.telek.hemsipc.protocol.iec104.server;

import java.util.Properties;

import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.CauseOfTransmission;
import org.openmuc.j60870.IeQuality;
import org.openmuc.j60870.IeShortFloat;
import org.openmuc.j60870.IeSinglePointWithQuality;
import org.openmuc.j60870.InformationElement;
import org.openmuc.j60870.InformationObject;
import org.openmuc.j60870.TypeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telek.hemsipc.util.ProperitesUtil;
import com.telek.hemsipc.util.StringUtil;


/**
 * Description: 数据采集帮助类。用于生成asdu对象。
 * 
 * @author lxf
 * @created 2017年12月29日 下午2:17:12
 */
public class DataCollectionHelper {
    private static Logger LOG = LoggerFactory.getLogger(DataCollectionHelper.class);
    /****************************** 点表地址信息 开始 ******************************/
    // 说明！！！！
    // 地址位必须连续且顺序不可改变。
    // 在总召上报的时候只会读取第一位drSwitch，后面默认依次递增。
    /**
     * 信息地址位：需求响应开关：1打开，0未打开 。也是遥信信息体开始地址，我们设备信息的开始地址点号(应读取配置文件)。
     */
    private static int drSwitch = 3000;
    /**
     * 信息地址位：收到需求响应指令：1收到，0未收到
     */
    private static int ifReceiveCommand = 3001;
    /**
     * 需求响应运行：1开始，0未开始
     */
    private static int drOperation = 3002;
    /**
     * 保电状态：1投入，0未投入
     */
    private static int electricState = 3003;
    /**
     * 第一轮轮控执行：1执行，0未执行
     */
    private static int wheelControlFirst = 3004;
    /**
     * 第二轮轮控执行：1执行，0未执行
     */
    private static int wheelControlSecond = 3005;
    /**
     * 第三轮轮控执行：1执行，0未执行
     */
    private static int wheelControlThrid = 3006;
    /**
     * 第四轮轮控执行：1执行，0未执行
     */
    private static int wheelControlFourth = 3007;
    /**
     * 需求响应状态：0正常状态/需求响应结束1收到需求响应2需求响应开始3轮控开始
     */
    private static int drState = 3008;
    /**
     * 响应策略编号：
     */
    private static int drPolicyId = 3009;
    /**
     * 空调#().启停状态:1开0关.
     */
    private static int airConditionState = 3010;
    // 分割线--------------上面是遥信，下面是遥测
    /**
     * 空调#().电流:单位A 。也是遥测信息体开始地址
     */
    private static int airConditionCurrent = 720000;
    /**
     * 空调#().电压:单位V
     */
    private static int airConditionVoltage = 720001;
    /**
     * 空调#().有功功率:单位kw
     */
    private static int airConditionActivepower = 720002;
    /**
     * 空调#().无功功率:单位kw
     */
    private static int airConditionReactivepower = 720003;
    /**
     * 空调#().视在功率:单位kw
     */
    private static int airConditionApparentpower = 720004;
    /******************************** 点表地址信息 结束 ******************************/
    static String dataCollectionFileName = "config.properties";
    /**
     * TODO 信息体开始地址，我们设备信息开关状态的开始地址点号(应读取配置文件)
     */
    private static int stateStartNum = 2055;
    /**
     * 储存上次的电压信息值。用于变位上数校验数据。
     */
    public static float lastUValue = 0;
    /**
     * 储存上次的电流信息值。用于变位上数校验数据。
     */
    public static float lastIValue = 0;
    /**
     * 储存上次的有功功率信息值。用于变位上数校验数据。
     */
    public static float lastActivePowere = 0;
    /**
     * 储存上次的无功功率信息值。用于变位上数校验数据。
     */
    public static float lastReactivePower = 0;
    /**
     * 储存上次的无功功率信息值。用于变位上数校验数据。
     */
    public static float lastApparentPower = 0;

    /**
     * Description:初始化信息地址位
     * 
     * @author lxf
     * @created 2018年1月19日 上午9:23:47
     */
    public static void initMessageAdress() {
        String pathString = ProperitesUtil.getFilePath(dataCollectionFileName);
        Properties config = ProperitesUtil.getProperties(pathString);
        if (config != null) {
            // 加载基本配置文件
            drSwitch = StringUtil.parseInt(config.getProperty("drSwitch", "" + drSwitch));
            ifReceiveCommand = StringUtil
                    .parseInt(config.getProperty("ifReceiveCommand", "" + ifReceiveCommand));
            drOperation = StringUtil.parseInt(config.getProperty("drOperation", "" + drOperation));
            electricState = StringUtil.parseInt(config.getProperty("electricState", "" + electricState));
            wheelControlFirst = StringUtil
                    .parseInt(config.getProperty("wheelControlFirst", "" + wheelControlFirst));
            wheelControlSecond = StringUtil
                    .parseInt(config.getProperty("wheelControlSecond", "" + wheelControlSecond));
            wheelControlThrid = StringUtil
                    .parseInt(config.getProperty("wheelControlThrid", "" + wheelControlThrid));
            wheelControlFourth = StringUtil
                    .parseInt(config.getProperty("wheelControlFourth", "" + wheelControlFourth));
            drState = StringUtil.parseInt(config.getProperty("drState", "" + drState));
            drPolicyId = StringUtil.parseInt(config.getProperty("drPolicyId", "" + drPolicyId));
            airConditionState = StringUtil
                    .parseInt(config.getProperty("airConditionState", "" + airConditionState));
            // 遥测
            airConditionCurrent = StringUtil
                    .parseInt(config.getProperty("airConditionCurrent", "" + airConditionCurrent));
            airConditionVoltage = StringUtil
                    .parseInt(config.getProperty("airConditionVoltage", "" + airConditionVoltage));
            airConditionActivepower = StringUtil
                    .parseInt(config.getProperty("airConditionActivepower", "" + airConditionActivepower));
            airConditionReactivepower = StringUtil.parseInt(
                    config.getProperty("airConditionReactivepower", "" + airConditionReactivepower));
            airConditionApparentpower = StringUtil.parseInt(
                    config.getProperty("airConditionApparentpower", "" + airConditionApparentpower));
        } else {
            LOG.info("点表配置文件无法加载。使用默认信息地址位。遥信开始编号是=" + drSwitch + ";遥测开始编号是=" + airConditionCurrent);
        }
    }

    /**
     * Description:总召命令回复-获取单点信息ASdu
     * 
     * @author lxf
     * @created 2017年12月26日 上午11:00:53
     * @param commonAddress
     * @param isOpen 整体设备开关状态1/0。一般都为开
     * @return
     */
    public static ASdu getSinglePointASduBy(int commonAddress, CauseOfTransmission cot, boolean isOpen) {
        ASdu aSdu = new ASdu(TypeId.M_SP_NA_1, true, cot, false, false, 0, commonAddress,
                new InformationObject[] { new InformationObject(stateStartNum, new InformationElement[][] {
                        { new IeSinglePointWithQuality(isOpen, false, false, false, false) } }) });
        return aSdu;
    }

    /**
     * Description:总召命令回复-获取短浮点数ASdu
     * 
     * @author lxf
     * @created 2017年12月26日 上午11:01:39
     * @param commonAddress * @param iValue 电流
     * @param uValue 电压
     * @param activpower 有功功率
     * @param reactivePower 无功功率
     * @param apparentpower 视在功率
     * @return
     */
    public static ASdu getShortFloatASduBy(int commonAddress, CauseOfTransmission cot, float iValue,
            float uValue, float activpower, float reactivePower, float apparentpower) {
        ASdu aSdu = new ASdu(TypeId.M_ME_NC_1, true, CauseOfTransmission.PERIODIC, false, false, 0,
                commonAddress,
                new InformationObject[] { new InformationObject(airConditionCurrent,
                        new InformationElement[][] {
                                { new IeShortFloat(iValue),
                                        new IeQuality(false, false, false, false, false) },
                                { new IeShortFloat(uValue),
                                        new IeQuality(false, false, false, false, false) },
                                { new IeShortFloat(activpower),
                                        new IeQuality(false, false, false, false, false) },
                                { new IeShortFloat(reactivePower),
                                        new IeQuality(false, false, false, false, false) },
                                { new IeShortFloat(apparentpower),
                                        new IeQuality(false, false, false, false, false) },
                                { new IeShortFloat(65),
                                            new IeQuality(false, false, false, false, false) } }) });
        return aSdu;
    }

    /**
     * Description:总召命令回复-获取短浮点数ASdu
     * 
     * @author lxf
     * @created 2017年12月26日 下午3:40:47
     * @param commonAddress
     * @return
     */
    public static ASdu getShortFloatASduBy(int commonAddress) {
        float uValue = DataCollectionHelper.getSystemUValue();
        float iValue = DataCollectionHelper.getSystemIValue();
        float activepower = DataCollectionHelper.getSystemActivepower();
        float reactivePower = DataCollectionHelper.getSystemReactivePower();
        float apparentpower = DataCollectionHelper.getSystemApparentpower();
        ASdu aSdu = getShortFloatASduBy(commonAddress, CauseOfTransmission.PERIODIC, iValue, uValue,
                activepower, reactivePower, apparentpower);
        return aSdu;
    }

    /**
     * Description:总召命令回复-激活终止
     * 
     * @author lxf
     * @created 2017年12月26日 上午11:28:27
     * @param aSdu 主站发的召唤指令。注意公共地址在协议上是写0？？
     * @return
     */
    public static ASdu getActivationTerminationaSduBy(ASdu aSdu) {
        // 注意公共地址 理应统一用DataCollectionServerStart.commonAddress。但是协议上写0，这里暂且用0.
        int commonAddress = DataCollectionServerStart.commonAddress;
        ASdu returnAsdu = new ASdu(aSdu.getTypeIdentification(), aSdu.isSequenceOfElements(),
                CauseOfTransmission.ACTIVATION_TERMINATION, aSdu.isTestFrame(), aSdu.isNegativeConfirm(),
                aSdu.getOriginatorAddress(), commonAddress, aSdu.getInformationObjects());
        return returnAsdu;
    }

    /**
     * Description:获取系统电压
     * 
     * @author lxf
     * @created 2017年12月26日 下午1:10:12
     * @return
     */
    public static float getSystemUValue() {
        float v = (float) 11.11;
        lastUValue = v;
        return v;
    }

    /**
     * Description:获取系统电流
     * 
     * @author lxf
     * @created 2017年12月26日 下午1:10:26
     * @return
     */
    public static float getSystemIValue() {
        float v = (float) 48.0;
        lastIValue = v;
        return v;
    }

    /**
     * Description:获取系统有功功率
     * 
     * @author lxf
     * @created 2017年12月26日 下午1:10:34
     * @return
     */
    public static float getSystemActivepower() {
        float v = (float) 33.33;
        lastActivePowere = v;
        return v;
    }

    /**
     * Description:获取系统无功功率
     * 
     * @author lxf
     * @created 2017年12月26日 下午1:10:49
     * @return
     */
    public static float getSystemReactivePower() {
        float v =  44.44f;
        lastReactivePower = v;
        return v;
    }

    /**
     * Description:获取系统无功功率
     * 
     * @author lxf
     * @created 2017年12月26日 下午1:10:49
     * @return
     */
    public static float getSystemApparentpower() {
        float v =  44.44f;
        lastApparentPower = v;
        return v;
    }
}
