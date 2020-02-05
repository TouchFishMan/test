package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.datamodel.Afn4F25Data;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommConst;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;
import com.telek.hemsipc.protocal3761.protocal.constant.FunctionCode;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;

import java.util.HashMap;
import java.util.Map;


/**
 ** 设置测量点基本参数
 */
public class SetAfn0CF129ReadElec extends AbsRequest {

    public SetAfn0CF129ReadElec(boolean isStartStation) {super(isStartStation);
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.MEASURE_POINT_BASE_PARAMS.getCommand());

        Map<String,Object> dateMap = new HashMap<>();
        dateMap.put("time","19/12/31 14:53");
        dateMap.put("rate",1);
//        dateMap.put("")

        packet.setData(data);

        packet.setControl(getCommonControl());
        System.out.println("发送设置测量点基本参数");
        return packet;
    }
}
