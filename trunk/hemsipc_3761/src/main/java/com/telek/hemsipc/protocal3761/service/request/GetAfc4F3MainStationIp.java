package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;

import java.util.HashMap;


/**
 * 获取 主站ip地址
 */
public class GetAfc4F3MainStationIp extends AbsRequest {

    public GetAfc4F3MainStationIp(boolean isStartStation) {
        super(isStartStation);
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.GET_MAIN_STATION_IP.getCommand());
        packet.setData(new HashMap<String, Object>());
        packet.setControl(getCommonControl());
        log.info("发送 AFN0A F3 帧");
        return packet;
    }
}
