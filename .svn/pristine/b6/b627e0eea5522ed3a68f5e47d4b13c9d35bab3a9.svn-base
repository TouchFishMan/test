package com.telek.hemsipc.protocol.dl645.response;


import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.telek.hemsipc.protocol.dl645.DL645Constant;
import com.telek.hemsipc.util.MessageUtils;

import java.util.Arrays;

/**
 * @Auther: wll
 * @Date: 2018/7/8 15:52
 * @Description: 读请求请求回复
 */
public class ReadResponse extends AbstractDL645Response {
    private byte[] dataid;
    private byte[] datas;

    public ReadResponse(byte[] slaveArr, Integer len, byte[] data) {
        super(slaveArr, DL645Constant.RESPONSE_READ_DATA, len, data);
        byte[] dataReal = MessageUtils.dectryData(data);
        // 数据值
        datas = Arrays.copyOfRange(dataReal, 0, dataReal.length - 4);
        // 数据类型标识
        dataid = Arrays.copyOfRange(dataReal, dataReal.length - 4, dataReal.length);
    }

    @Override
    public String getSyncKey(){
        return slave + "-" + DL645Constant.REQUEST_READ_DATA + "-" + HexBin.encode(dataid);
    }

    public byte[] getDataid() {
        return dataid;
    }

    public byte[] getDatas() {
        return datas;
    }
}
