package com.telek.hemsipc.protocal3761.dto;

import com.telek.hemsipc.http.Pojo;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangxb
 * @date 20-1-7 上午10:56
 */
@Data
public class CommandAfn4F33Dto {

    /**
     * 终端通信端口号
     */
    @Pojo(type = Integer.class, empty = false, maxValue = 31, minValue = 1)
    private Integer terminalPort = 3;

    /**
     * 集中抄表运行控制字
     */
    private F33ReadSettingParams readSetting;

    /**
     * 抄表日期
     */
//    private String readDate = "7fffffff";//小端
    /**
     * 抄表时间
     */
    private String readTime = "15:20";
    /**
     * 抄表时间间隔
     */
    private Integer intervalTime = 1;
    /**
     * 对电表广播校时定时时间
     */
    private String checkTime = "00 01:00"; //每天零点，天为0时表示每天
    /**
     * 允许抄表的时间段数
     */
//    private Integer allowReadTimeNum = 1;
    /**
     * 允许抄表的时间段
     */
//    private List<Map> allowReadTimePeriod;
//
//    {
//        Map readTimeMap = new HashMap();
//        readTimeMap.put("allowReadTimeStart","00:00");
//        readTimeMap.put("allowReadTimeEnd","24:00");
//        this.allowReadTimePeriod = new ArrayList<>();
//        this.allowReadTimePeriod.add(readTimeMap);
//    }

}

