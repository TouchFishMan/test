package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommConst;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;
import com.telek.hemsipc.protocal3761.protocal.constant.FunctionCode;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;


/**
 * 回复确认包
 */
public class ConfirmRequest extends AbsRequest {

    public ConfirmRequest() {
        super(false);
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.CONFIRM.getCommand());

        Control control = new Control();
        control.setFunctionCode(FunctionCode.PRM_SLAVE_CONFIRM);
        control.setPrm(CommConst.PRM_SLAVE_STATION);
        control.setDir(CommConst.DIR_DOWN);
        control.setIsNeedConfirm(0);
        control.setFin(1);
        control.setFir(1);

        packet.setControl(control);

        return packet;
    }
}
