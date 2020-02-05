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
    protected Packet getPacket(String address2, Packet ordinaryPacket) {
        Packet packet = getCommPacket(address2);
        Control control = getControl();
        packet.setControl(control);
        return packet;
    }

    private Packet getCommPacket(String address2) {
        Packet encodePacket = new Packet();
        encodePacket.setTerminalAddress(Protocol3761Cache.ADDRESS2_TERMINAL_ADDRESS.get(address2));
        encodePacket.setCommand(CommandAfn.CONFIRM.getCommand());
        return encodePacket;
    }

    private Control getControl() {
        Control control = new Control();
        control.setFunctionCode(FunctionCode.PRM_SLAVE_RESPONSE_LINK);
        control.setPrm(CommConst.PRM_SLAVE_STATION);
        control.setDir(CommConst.DIR_DOWN);
        control.setAfn(CommandAfn.CONFIRM.getAfn());
        control.setFin(1);
        control.setFir(1);
        control.setTpV(0);
        return control;
    }
}
