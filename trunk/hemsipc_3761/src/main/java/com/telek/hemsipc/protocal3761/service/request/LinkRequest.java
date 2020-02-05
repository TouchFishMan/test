package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommConst;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;
import com.telek.hemsipc.protocal3761.protocal.constant.FunctionCode;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;


public class LinkRequest extends AbsRequest {

    public LinkRequest(boolean isStartStation) {
        super(isStartStation);
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = getCommPacket(address);
        Control control = getCommonControl();
        packet.setControl(control);
        return packet;
    }

    private Packet getCommPacket(String address) {
        Packet encodePacket = new Packet();
        encodePacket.setTerminalAddress(address);
        encodePacket.setCommand(CommandAfn.CONFIRM.getCommand());
        return encodePacket;
    }

}
