package com.telek.hemsipc.util;

/**
 * @Auther: wll
 * @Date: 2018/9/17 11:26
 * @Description:
 */
public class ByteUtil {
    public static byte[] integer2Bytes(Integer intValue) {
        byte[] result = new byte[4];
        result[0] = (byte) (intValue >> 24 & 0xff);
        result[1] = (byte) (intValue >> 16 & 0xff);
        result[2] = (byte) (intValue >> 8 & 0xff);
        result[3] = (byte) (intValue & 0xff);
        return result;
    }
}
