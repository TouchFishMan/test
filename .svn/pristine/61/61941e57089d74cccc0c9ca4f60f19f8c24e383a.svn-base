package com.telek.hemsipc.protocal3761.service.request;

import java.util.HashMap;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.datamodel.Afn4F7Data;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommConst;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;
import com.telek.hemsipc.protocal3761.protocal.constant.FunctionCode;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;


/**
 ** 设置终端ip
 */
public class SetAfn4F7TerminalIp extends AbsRequest {

    public SetAfn4F7TerminalIp(boolean isStartStation, Afn4F7Data data) {
        super(isStartStation);

        if (data != null) {
            this.data = new HashMap<>();
            String mainIp = data.getMainIp();
            if (mainIp == null) {
                mainIp = "192.168.0.200";
            }
            String mask = data.getMask();
            if (mask == null) {
                mask = "255.255.255.0";
            }
            String gateway = data.getGateway();
            if (gateway == null) {
                gateway = "192.168.0.1";
            }
            this.data.put("mainIp", mainIp);
            this.data.put("mask", mask);
            this.data.put("gateway", gateway);
            this.data.put("agent", "0.0.0.0");
        } else {
            throw new RuntimeException("SetAfn4F10Params中data不能为null");
        }
    }

    @Override
    protected Packet getPacket(String address2, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(Protocol3761Cache.ADDRESS2_TERMINAL_ADDRESS.get(address2));
        packet.setCommand(CommandAfn.TERMINAL_IP.getCommand());

        packet.setData(data);

        Control control = new Control();
        control.setFunctionCode(FunctionCode.PRM_START_REQUEST_RESPONSE_1);
        control.setPrm(CommConst.PRM_START_STATION);
        control.setDir(CommConst.DIR_DOWN);
        control.setFin(1);
        control.setFir(1);

        packet.setControl(control);
        return packet;
    }
}
