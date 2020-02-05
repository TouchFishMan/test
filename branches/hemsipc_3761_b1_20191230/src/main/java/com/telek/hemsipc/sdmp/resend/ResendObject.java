package com.telek.hemsipc.sdmp.resend;


import com.telek.hemsipc.sdmp.SDMPv1;

public class ResendObject {
    // 重发次数
    public final static int resendNum = 3;
    //每次重发间隔
    public final static int intervalTime = 15000;
    //重发sdmp对象
    private SDMPv1 sdmpData;
    // 当前发送的次数
    private int resendIndex = 0;
    // 发送时间
    private long sendTime = 0;

    public ResendObject(SDMPv1 sdmpData) {
        this.sdmpData = sdmpData;
        refreshResend();
    }

    public void refreshResend() {
        this.resendIndex++;
        this.sendTime = System.currentTimeMillis() + intervalTime;
    }

    public SDMPv1 getSdmpData() {
        return sdmpData;
    }

    public int getResendIndex() {
        return resendIndex;
    }

    public long getSendTime() {
        return sendTime;
    }
}
