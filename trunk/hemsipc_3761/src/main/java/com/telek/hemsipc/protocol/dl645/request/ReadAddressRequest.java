package com.telek.hemsipc.protocol.dl645.request;


import com.telek.hemsipc.protocol.dl645.DL645Constant;
import com.telek.hemsipc.util.MessageUtils;

/**
 * @Auther: wll
 * @Date: 2018/7/8 11:05
 * @Description: 读取通讯地址
 */
public class ReadAddressRequest extends  AbstractDL645Request{
    public ReadAddressRequest() {
        super("AAAAAAAAAAAA", DL645Constant.REQUEST_READ_ADDR);
        baos.write(MessageUtils.calculateCS(getMessageData()));
        baos.write(0x16);
    }
}
