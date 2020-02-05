package com.telek.hemsipc.protocal3761.protocal.constant;

/**
 * 波特率
 */
public enum ProcotolTypeConst {
    NONE(0, "无需抄表"),
    DLT645_1997(1, "DL/T 645-1997"),
    A(2, "交流采样装置通信协议"),
    DLT645_2007(30, "DL/T 645-2007"),
    B(31, "串行接口连接窄带低压载波通信模块"),
    C(40, "传统漏保"),
    D(41, "标准漏保规约(07版)"),
    E(42, "乾龙漏保规约"),
    F(43, "安徽明坤漏保规约");

    int code;
    String msg;

    ProcotolTypeConst(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static ProcotolTypeConst getByCode(int code) {
        for (ProcotolTypeConst value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
