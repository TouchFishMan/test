package com.telek.hemsipc.protocol.dl645.response;


import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.telek.hemsipc.protocol.IResponse;
import com.telek.hemsipc.util.MessageUtils;

import java.io.ByteArrayOutputStream;

public abstract class AbstractDL645Response implements IResponse {
    protected ByteArrayOutputStream baos = new ByteArrayOutputStream();
    protected String slave;
    private Integer cs;

    public String getSlave() {
        return slave;
    }

    public Integer getCs() {
        return cs;
    }

    public AbstractDL645Response(byte[] slaveArr, int funCode, int len, byte[] data) {
        assert slaveArr.length == 6;
        baos.write(0x68);
        baos.write(slaveArr, 0, 6);
        baos.write(0x68);
        baos.write(funCode);
        baos.write(len);
        // slave转BCD
        byte[] slaveArrReal = MessageUtils.reverse(slaveArr);
        this.slave = HexBin.encode(slaveArrReal);
        if (len > 0) {
            baos.write(data, 0, data.length);
        }
        // cs校验
        this.cs = MessageUtils.calculateCS(baos.toByteArray());
        baos.write(cs);
        // 结束
        baos.write(0x16);
    }

    public String getSyncKey(){
        return "";
    }
}
