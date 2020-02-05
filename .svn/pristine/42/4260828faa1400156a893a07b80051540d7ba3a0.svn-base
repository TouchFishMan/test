package com.telek.hemsipc.util;

import com.telek.hemsipc.contant.ProtocolType;
import com.telek.hemsipc.model.Device;

public class Protocol376Util {

    /**
     * 判断一个设备是否为376协议的设备
     * @param device
     * @return
     */
    public static boolean is376Device(Device device) {
        if (device == null) {
            return false;
        }
        String decodeProtocol = device.getDecodeProtocol();
        if (ProtocolType.DL3761.getCode().equals(decodeProtocol)) {
            return true;
        }
        return false;
    }

}
