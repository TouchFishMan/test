package com.telek.hemsipc.protocal3761.datamodel;

import com.telek.hemsipc.protocal3761.protocal.constant.ProcotolTypeConst;
import com.telek.hemsipc.protocal3761.protocal.constant.SpeedConst;

import lombok.Data;

@Data
public class Afn4F10Data {
    /**
     * 序号
     */
    private int protectorIndex;
    /**
     * 所属测量点号
     */
    private int measuringPoint;
    /**
     * 端口 1-31
     */
    private int port;
    /**
     * 通信速率
     */
    private SpeedConst speed;
    /**
     * 通信协议类型
     */
    private ProcotolTypeConst protocalType;
    /**
     * 通信地址
     */
    private String address;
    /**
     * 通信密码
     */
    private String password;
}
