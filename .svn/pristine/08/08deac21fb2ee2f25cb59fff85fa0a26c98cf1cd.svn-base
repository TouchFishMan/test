package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommConst;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;
import com.telek.hemsipc.protocal3761.protocal.constant.FunctionCode;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 获取终端抄读表参数
 */
public class GetAfc4F33ReadParams extends AbsRequest {

    private List<Integer> portList;

    public GetAfc4F33ReadParams(boolean isStartStation, List<Integer> portList) {
        super(isStartStation);
        this.portList = portList;
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.GET_READ_RUNNING_PARAMS_SEND_PARAMS.getCommand());
        data = new HashMap<>();
        data = new HashMap<>();
        if (portList != null && portList.size() > 0) {
            data.put("queryNum", portList.size());
            List list = new ArrayList();
            for (int index : portList) {
                Map<String, Integer> map = new HashMap<>();
                map.put("port", index);
                list.add(map);
            }
            data.put("list", list);
        }
        packet.setData(data);
        packet.setControl(getCommonControl());
        log.info("发送 AFN0A F33 帧");
        return packet;
    }
}
