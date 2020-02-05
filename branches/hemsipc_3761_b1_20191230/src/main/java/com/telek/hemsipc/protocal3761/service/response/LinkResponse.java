package com.telek.hemsipc.protocal3761.service.response;

import java.util.Map;

import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;
import com.telek.hemsipc.protocal3761.service.request.AbsRequest;
import com.telek.hemsipc.protocal3761.service.request.LinkRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * 链路检测
 */
@Slf4j
public class LinkResponse implements IResponse {

    @Override
    public Object dealReceiveFromTerminal(Packet receivedPacket) {
        try {

            if (CommandAfn.LOGIN.equals(CommandAfn.get(receivedPacket.getCommand()))) {
                Protocol3761Cache.TERMINAL_ADDRESS2_LOGIN.put(receivedPacket.getAddress2(), new Object());
                Protocol3761Cache.ADDRESS2_TERMINAL_ADDRESS.put(receivedPacket.getAddress2(), receivedPacket.getTerminalAddress());
            } else if (CommandAfn.HEARTBEAT.equals(CommandAfn.get(receivedPacket.getCommand()))) {
                Map<String, Object> data = receivedPacket.getData();
                // 19/9/20 9:56:21 5  --秒、分、时、日、星期-月、年
                Object terminalTime = data.get("terminalTime");
                if (terminalTime != null) {
                    Protocol3761Cache.TERMINAL_ADDRESS2_HEARTBEAT.put(receivedPacket.getAddress2(), terminalTime.toString());
                }
            } else if (CommandAfn.LOGOUT.equals(CommandAfn.get(receivedPacket.getCommand()))) {
                Protocol3761Cache.TERMINAL_ADDRESS2_LOGIN.remove(receivedPacket.getAddress2());
                Protocol3761Cache.TERMINAL_ADDRESS2_HEARTBEAT.remove(receivedPacket.getAddress2());
            }

            AbsRequest request = new LinkRequest(false);
            request.dealSendToTerminal(receivedPacket.getAddress2(), receivedPacket);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("编码失败");
        }
        return null;
    }
}
