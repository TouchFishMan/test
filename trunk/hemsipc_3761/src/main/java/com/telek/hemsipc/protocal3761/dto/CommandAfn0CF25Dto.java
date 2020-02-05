package com.telek.hemsipc.protocal3761.dto;

import lombok.Data;

/**
 * @author wangxb
 * @date 20-1-9 上午9:06
 */
@Data
public class CommandAfn0CF25Dto {

    /**
     * 抄表时间
     */
    private String terminalTime;

    /**
     * 当前总有功功率
     */
    private String totalActivePower;

    /**
     * 当前A向有功功率
     */
    private String totalA_ActivePower;

    /**
     * 当前B向有功功率
     */
    private String totalB_ActivePower;

    /**
     * 当前C向有功功率
     */
    private String totalC_ActivePower;

    /**
     * 当前总无功功率
     */
    private String totalInactivePower;

    /**
     * 当前A向无功功率
     */
    private String totalA_InactivePower;

    /**
     * 当前B向无功功率
     */
    private String totalB_InactivePower;

    /**
     * 当前C向无功功率
     */
    private String totalC_InactivePower;

    /**
     * 当前总功率因素
     */
    private String totalPowerFactor;

    /**
     * 当前A相功率因素
     */
    private String APowerFactor;

    /**
     * 当前B相功率因素
     */
    private String BPowerFactor;

    /**
     * 当前C相功率因素
     */
    private String CPowerFactor;

    /**
     * 当前A相电压
     */
    private String voltageA;

    /**
     * 当前B相电压
     */
    private String voltageB;

    /**
     * 当前C相电压
     */
    private String voltageC;

    /**
     * 当前A相电流
     */
    private String currentA;

    /**
     * 当前B相电流
     */
    private String currentB;

    /**
     * 当前C相电流
     */
    private String currentC;


    /**
     * 当前零序电流
     */
    private String noneCurrent;

    /**
     * 当前总视载功率
     */
    private String totalAppPower;

    /**
     * 当前A相总视载功率
     */
    private String totalAppPowerA;

    /**
     * 当前B相总视载功率
     */
    private String totalAppPowerB;

    /**
     * 当前C相总视载功率
     */
    private String totalAppPowerC;
}
