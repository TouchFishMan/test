package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommConst;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;
import com.telek.hemsipc.protocal3761.protocal.constant.FunctionCode;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;


/**
 * 复位
 */
public class ResetRequest extends AbsRequest {

    public ResetRequest(boolean isStartStation) {
        super(isStartStation);
    }

    @Override
    protected Packet getPacket(String address2, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(Protocol3761Cache.ADDRESS2_TERMINAL_ADDRESS.get(address2));
        packet.setCommand(CommandAfn.ALL_DATA_INITIALIZATION_EXCEPT_COMM.getCommand());

        Control control = new Control();
        control.setFunctionCode(FunctionCode.PRM_START_SEND_CONFIRM);
        control.setPrm(CommConst.PRM_START_STATION);
        control.setDir(CommConst.DIR_DOWN);
        control.setFin(1);
        control.setFir(1);

        packet.setControl(control);

        return packet;
    }
}
