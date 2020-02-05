package com.telek.hemsipc.protocal3761.model;

import lombok.Data;

/**
 * {time=19/10/9 14:40, totalActivePower=0.0000, totalA_ActivePower=-999999999, totalB_ActivePower=-999999999,
 * totalC_ActivePower=-999999999, totalInactivePower=-999999999, totalA_InactivePower=-999999999,
 * totalB_InactivePower=-999999999, totalC_InactivePower=-999999999, totalPowerFactor=-999999999,
 * APowerFactor=-999999999, BPowerFactor=-999999999, CPowerFactor=-999999999, voltageA=232.1,
 * voltageB=0.8, voltageC=0.5, currentA=0.000, currentB=0.000, currentC=0.000, noneCurrent=-999999999,
 * totalAppPower=-999999999, totalAppPowerA=-999999999, totalAppPowerB=-999999999, totalAppPowerC=-999999999}
 */
@Data
public class Afn0cF25RealTimeDataModel {

    private String address;
    private int line;
    private long time;
    private double totalActivePower;
    private double totalA_ActivePower;
    private double totalB_ActivePower;
    private double totalC_ActivePower;
    private double totalInactivePower;
    private double totalA_InactivePower;
    private double totalB_InactivePower;
    private double totalC_InactivePower;
    private double totalPowerFactor;
    private double APowerFactor;
    private double BPowerFactor;
    private double CPowerFactor;
    private double voltageA;
    private double voltageB;
    private double voltageC;
    private double currentA;
    private double currentB;
    private double currentC;
    private double noneCurrent;
    private double totalAppPower;
    private double totalAppPowerA;
    private double totalAppPowerB;
    private double totalAppPowerC;

}
