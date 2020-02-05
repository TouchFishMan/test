package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;

import java.util.HashMap;


/**
 * 获取 任务参数
 */
public class GetAfc4F65ReadTaskParams extends AbsRequest {

    private int pn = 0;

    public GetAfc4F65ReadTaskParams(boolean isStartStation, int pn) {
        super(isStartStation);
        this.pn = pn;
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.GET_READ_TASK_PARAMS.getCommand());
        packet.setLine(pn);
        packet.setData(new HashMap<String, Object>());
        packet.setControl(getCommonControl());
        log.info("准备发送 AFN0A F65 帧" + packet);
        return packet;
    }
}
