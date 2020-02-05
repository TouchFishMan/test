package com.telek.hemsipc.protocal3761.service.request;

import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;

import java.util.HashMap;


/**
 ** 设置终端主站ip
 */
public class SetAfn4F240TerminalAddress extends AbsRequest {

    public SetAfn4F240TerminalAddress(boolean isStartStation) {
        super(isStartStation);
    }

    @Override
    protected Packet getPacket(String address, Packet ordinaryPacket) {
        Packet packet = new Packet();
        packet.setTerminalAddress(address);
        packet.setCommand(CommandAfn.TERMINAL_ADDRESS.getCommand());
        if (data == null || data.size() == 0) {
            data = new HashMap<>();
            //0x55开启，0xAA关闭
            data.put("code","1234");
            data.put("address","5678");
        }
        packet.setData(data);
        packet.setControl(getCommonControl());
        System.out.println("发送F240");
        return packet;
    }
}
