package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.datamodel.Afn4F25Data;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;

import java.util.HashMap;


/**
 ** 设置测量点基本参数
 */
public class SetAfn4F25MeasurePointBaseParams extends AbsRequest {

    public SetAfn4F25MeasurePointBaseParams(boolean isStartStation, Afn4F25Data data) {
        super(isStartStation);
        if (data != null) {
            this.data.put("rateU", data.getRateU());
            this.data.put("rateI", data.getRateI());
            this.data.put("baseU", data.getBaseU());
            this.data.put("baseI", data.getBaseI());
            this.data.put("baseLoad", data.getBaseLoad());
            this.data.put("powerConnectType",data.getPowerConnectType()==null?3:data.getPowerConnectType());
            this.data.put("meterConnectType",data.getMeterConnectType()==null?3:data.getMeterConnectType());
        } else {
            throw new RuntimeException("SetAfn4F25MeasurePointBaseParams中data不能为null");
        }
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.MEASURE_POINT_BASE_PARAMS.getCommand());
        packet.setData(data);
        packet.setControl(getCommonControl());
        System.out.println("发送设置测量点基本参数");
        return packet;
    }
}
