package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;


/**
 * 复位
 */
public class ResetRequest extends AbsRequest {

    public ResetRequest(boolean isStartStation) {
        super(isStartStation);
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.ALL_DATA_INITIALIZATION_EXCEPT_COMM.getCommand());
        packet.setControl(getCommonControl());
        return packet;
    }
}
