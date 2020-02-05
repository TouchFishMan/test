package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;

import java.util.HashMap;


/**
 * * 设置终端主站ip
 */
public class SetAfn4F67ReadTaskOnOff extends AbsRequest {

    public SetAfn4F67ReadTaskOnOff(boolean isStartStation, int taskId, boolean onOff) {
        super(isStartStation);
        data = new HashMap<>();
        data.put("taskId", taskId);
        if (onOff) {
            data.put("taskOnOff", 85);
        } else {
            data.put("taskOnOff", 170);
        }
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.TASK_ON_OFF.getCommand());
        packet.setLine((Integer) data.get("taskId"));
        packet.setData(data);
        packet.setControl(getCommonControl());
        log.info("准备发送F67帧：" + packet);
        return packet;
    }
}
