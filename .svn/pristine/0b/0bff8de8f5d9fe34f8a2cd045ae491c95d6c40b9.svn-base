package com.telek.hemsipc.protocal3761.protocal;

import lombok.Data;

/**
 * Created by PETER on 2015/3/24.
 */
@Data
public class CodecConfig implements Cloneable{
    /**
     * 主站编号
     */
    private int msa;

    /**
     * 终端地址
     * 16进制字符串格式
     */
    private String terminalAddress;
    /**
     * 终端通信密码
     */
    private String password;

    /**
     * 是否添加时标
     * 默认添加
     */

    private boolean haveTp;

    /**
     * 时标的超时时间
     * 默认5分钟
     */
    private int timeout;

    /**
     * 是否打开流程控制
     */
    private boolean processControl;

    /**
     * 规约版本
     */
    private int protocalVersion;

    /**
     * 地区码长度
     */
    private int distinctLength;

    /**
     * 终端地址吗长度
     */

    private int terminalAddressLength;

    public CodecConfig() {
    }

    @Override
    public Object clone() {
        CodecConfig cloned=null;
        try {
            cloned = (CodecConfig) super.clone();
            cloned.terminalAddress = new String(terminalAddress);
            cloned.password = new String(password);
        }catch (Exception e){}
        return cloned;
    }
}
