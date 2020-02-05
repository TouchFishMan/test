package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;

import java.util.HashMap;

/**
 * @author wangxb
 * @date 20-1-8 下午4:49
 */
public class GetAfc0CF25RealTimeData extends AbsRequest {

    private int pn = 0;

    public GetAfc0CF25RealTimeData(boolean isStartStation, int pn) {
        super(isStartStation);
        this.pn = pn;
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.GET_REAL_TIME_DATA.getCommand());
        packet.setData(new HashMap<String, Object>());
        packet.setLine(pn);
        packet.setControl(getCommonControl());
        log.info("发送 AFN0C F25 帧");
        return packet;
    }
}
