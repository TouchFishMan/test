package com.telek.hemsipc.protocal3761.dto;

import com.telek.hemsipc.http.Pojo;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * @author wangxb
 * @date 20-1-7 下午4:26
 */
@Data
public class CommandAfn4F65Dto {

    /**
     * 任务号
     */
    @Max(value = 1, message = "error")
    private Integer taskId;

    /**
     * 定时上报周期单位，0-分，1-时，2-日，3-月
     */
    private Integer timingCycleUnit = 0;

    /**
     * 定时上报周期
     */
    private Integer timingCycle = 1;

    /**
     * 上报基准时间,"19/12/28 01:05:00 6"
     */
    private String baseTime;

    /**
     * 曲线数据抽取倍率R，1-96
     */
    @Pojo(type = Integer.class, empty = false, maxValue = 96, minValue = 1)
    private Integer rate = 3;

    /**
     * 任务列表
     */
    private List<Map> taskList;
}
