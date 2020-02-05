package com.telek.hemsipc.protocal3761.service.request;

import java.util.HashMap;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommConst;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;
import com.telek.hemsipc.protocal3761.protocal.constant.FunctionCode;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;


/**
 ** 设置终端主站ip
 */
public class SetAfn4F4MainStationIp extends AbsRequest {

    public SetAfn4F4MainStationIp(boolean isStartStation) {
        super(isStartStation);
    }

    @Override
    protected Packet getPacket(String address2, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(Protocol3761Cache.ADDRESS2_TERMINAL_ADDRESS.get(address2));
        packet.setCommand(CommandAfn.MAIN_STATION_IP.getCommand());
        if (data == null) {
            data = new HashMap<>();
            data.put("mainIp", "192.168.0.200:10000");
            data.put("subIp", "127.0.0.1:10000");
            data.put("APN", "CMNET");
        }

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
