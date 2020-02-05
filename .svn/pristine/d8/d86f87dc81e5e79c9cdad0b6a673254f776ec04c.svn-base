package com.telek.hemsipc.protocol.dl645.response;


import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.telek.hemsipc.protocol.dl645.DL645Constant;
import com.telek.hemsipc.util.MessageUtils;

/**
 * @Auther: wll
 * @Date: 2018/7/8 16:01
 * @Description:
 */
public class ReadAddressResponse extends AbstractDL645Response{
    public ReadAddressResponse(byte[] slaveArr,byte[] data) {
        super(slaveArr, DL645Constant.RESPONSE_READ_ADDR,6,data);
        byte[] dataReal = MessageUtils.dectryData(data);
        //数据位地址和地址位地址是否一致
        assert slave.equals(HexBin.encode(dataReal));
    }

    @Override
    public String getSyncKey(){
        return slave + "-" + DL645Constant.REQUEST_READ_ADDR;
    }
}
