package com.telek.hemsipc.service;

import java.util.Vector;

import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.sdmp.VarBind;

/**
 * @Auther: wll
 * @Date: 2018/9/14 14:17
 * @Description:
 */
public interface ICloudService {
    void sendDataFeedback(int actionType, Vector<VarBind> varBinds, Device device, int msgID);

    void sendHeartBeat(Vector<VarBind> varBinds, Device device);

    void sendOnline(Device device);

    void sendActive(Device device);
}
