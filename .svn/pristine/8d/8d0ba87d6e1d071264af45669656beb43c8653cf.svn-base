package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;


/**
 ** 获取 终端时间测试
 */
public class GetAfc0CF2TerminalTime extends AbsRequest {

    public GetAfc0CF2TerminalTime(boolean isStartStation) {
        super(isStartStation);
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.GET_TERMINAL_TIME.getCommand());

        packet.setControl(getCommonControl());

        return packet;
    }
}
