package com.telek.hemsipc.protocal3761.dto;

import com.telek.hemsipc.http.Pojo;
import lombok.Data;

/**
 * @author wangxb
 * @date 20-1-7 下午4:12
 */
@Data
public class CommandAfn4F10Dto {

    /**
     * 电表id
     */
    private String deviceId;
    /**
     * 序号
     */
    @Pojo(type = Integer.class, empty = false, maxValue = 2040)
    private Integer protectorIndex;
    /**
     * 所属测量点号
     */
    @Pojo(type = Integer.class, empty = false, maxValue = 2040)
    private Integer measuringPoint;
    /**
     * 端口 1-31
     */
    @Pojo(type = Integer.class, empty = false, maxValue = 31, minValue = 1)
    private Integer port;
    /**
     * 通信速率
     */
    private Integer speed;
    /**
     * 通信协议类型
     */
    private Integer protocalType;
    /**
     * 通信地址
     */
    @Pojo(empty = false, maxLength = 12)
    private String address;
    /**
     * 通信密码
     */
    @Pojo(empty = false, maxLength = 6)
    private String password = "0";
    /**
     * 所属采集器地址
     */
    @Pojo(empty = false, maxLength = 12)
    private String cAddress = "0";
    /**
     * 电能费率小数数位个数
     */
    private Integer feilvA = 3;
    /**
     * 电能费率整数位个数
     */
    private Integer feilvB = 3;
    /**
     * 电能费率数量
     */
    private Integer feilvNum = 4;
    /**
     * 大类号
     */
    private Integer max;
    /**
     * 小类号
     */
    private Integer min;

    /**
     * 电表名称
     */
    private String name;

    /**
     * 电表安装地址，描述
     */
    private String location;
}
