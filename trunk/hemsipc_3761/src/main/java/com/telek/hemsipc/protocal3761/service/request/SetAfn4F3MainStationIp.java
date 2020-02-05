package com.telek.hemsipc.protocal3761.service.request;

import java.util.HashMap;
import java.util.Map;

import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F3Dto;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;
import org.apache.commons.collections.map.HashedMap;


/**
 ** 设置终端主站ip
 */
public class SetAfn4F3MainStationIp extends AbsRequest {

    public SetAfn4F3MainStationIp(boolean isStartStation, CommandAfn4F3Dto afn4F3Dto) {
        super(isStartStation);
        data = new HashMap<>();
        String mainIp = afn4F3Dto.getMainIp() + Constant.SPLIT_SIGN_COLON + afn4F3Dto.getMainPort();
        String subIp = afn4F3Dto.getSubIp() + Constant.SPLIT_SIGN_COLON + afn4F3Dto.getSubPort();
        data.put("mainIp", mainIp);
        data.put("subIp", subIp);
        data.put("APN", "CMNET");
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.MAIN_STATION_IP.getCommand());
        packet.setData(data);
        packet.setControl(getCommonControl());
        log.info("发送 AFN4 F3 帧");
        return packet;
    }

    private Map getTestData(){
        Map<String, Object> data = new HashedMap();
        data.put("mainIp", "192.168.0.100:10001");
        data.put("subIp", "127.0.0.1:10000");
        data.put("APN", "CMNET");
        return data;
    }
}
