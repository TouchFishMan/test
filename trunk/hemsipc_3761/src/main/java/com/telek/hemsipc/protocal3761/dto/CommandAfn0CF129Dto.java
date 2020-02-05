package com.telek.hemsipc.protocal3761.dto;

import lombok.Data;

import java.util.List;

/**
 * @author wangxb
 * @date 20-1-9 上午9:06
 */
@Data
public class CommandAfn0CF129Dto {

    /**
     * 抄表时间
     */
    private String terminalTime;

    /**
     * 费率数
     */
    private int rate;

    /**
     * 当前正向有功总电能
     */
    private String totalActiveElec;

    /**
     * 费率n正向有功总电能示值
     */
    private List<Double> list;
}
