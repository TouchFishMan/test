package com.telek.hemsipc.protocol.dl645.request;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.telek.hemsipc.protocol.IRequest;
import com.telek.hemsipc.protocol.dl645.DL645Constant;
import com.telek.hemsipc.util.MessageUtils;

import java.io.ByteArrayOutputStream;

public abstract class AbstractDL645Request implements IRequest{
    protected ByteArrayOutputStream baos = new ByteArrayOutputStream();
    protected String slave;
    protected Integer funCode;

    public AbstractDL645Request(String slave, Integer funCode) {
        assert slave.length() == 12;
        int len = 0;
        switch (funCode) {
            case DL645Constant.REQUEST_READ_ADDR:// 读取电表通讯地址
                len = 0;
                break;
            case DL645Constant.REQUEST_READ_DATA: //读取电表数据
                len = 4;
                break;
            case DL645Constant.REQUEST_ADJUST_CLOCK: //广播校时
                len = 6;
                break;
            default:
                break;
        }
        baos.write(0x68);
        byte[] slaveAddr = HexBin.decode(slave);
        baos.write(MessageUtils.reverse(slaveAddr),0,6);
        baos.write(0x68);

        baos.write(funCode);
        baos.write(len);
        this.slave = slave;
        this.funCode = funCode;
    }

    public byte[] getMessageData() {
        return baos.toByteArray();
    }

    public String getSyncKey(){
        return slave + "-" + funCode;
    }
}
