package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommConst;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;


public class TestRequest extends AbsRequest {

    public TestRequest(boolean isStartStation) {
        super(isStartStation);
    }

    @Override
    protected Packet getPacket(String address2, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(Protocol3761Cache.ADDRESS2_TERMINAL_ADDRESS.get(address2));
        packet.setCommand("protectorStatus");

        Control control = new Control();
        control.setFunctionCode(1);
        control.setPrm(CommConst.PRM_START_STATION);
        control.setDir(CommConst.DIR_DOWN);
        control.setFin(1);
        control.setFir(1);

        packet.setLine(1);

        packet.setControl(control);

        return packet;
    }
}
