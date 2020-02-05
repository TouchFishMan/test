package com.telek.hemsipc.protocal3761.service.response;


import com.telek.hemsipc.protocal3761.netty.NettyStarter;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseServer implements Runnable {

    private Packet decodedPacket;

    public ResponseServer(Packet decodedPacket) {
        this.decodedPacket = decodedPacket;
    }

    @Override
    public void run() {
        String command = decodedPacket.getCommand();
        if (command == null) {
            log.error("command is null");
        }
        IResponse response = null;
        switch (CommandAfn.get(command)) {
            case LOGIN:
            case HEARTBEAT:
            case LOGOUT:
                response = new LinkResponse();
                break;
            case TERMINAL_TIME:
                System.out.println("终端时间为:" + decodedPacket.getData());
                break;
            case REAL_TIME_DATA:
                response = new Afn0cF25RealTimeData();
                break;
            case ELEC_REAL_DATA:
                response = new Afn0cF129ElecData();
                break;
            default:
                System.out.println("解码packet:" + decodedPacket);
                break;
        }
        if (response != null) {
            response.dealReceiveFromTerminal(decodedPacket);
        }
    }

    private void testSendStr() {

        String hex = "68 42 00 42 00 68 7B 01 01 BA 07 02 0C 68 00 00 04 01 00 00 10 01 CA 16  ";

        NettyStarter.sendTest(hex);
    }

    private void testSendStr2() {

        String[] ss = {

                "68 76 00 76 00 68 5A 01 01 BA 07 02 04 61 00 00 80 02 01 67 89 00 00 00 00 00 00 00 00 00 00 00 00 00 00 F7 16",
                "68 A2 00 A2 00 68 7A 01 01 BA 07 02 04 62 00 00 10 03 31 32 33 34 35 36 00 00 00 00 00 00 67 89 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DD 16",
                "68 BE 00 BE 00 68 5A 01 01 BA 07 02 04 63 00 00 01 04 01 01 3F 00 FF FF FF FF 00 00 01 00 00 00 01 00 00 00 23 67 89 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DD 16",
                "68 86 00 86 00 68 7A 01 01 BA 07 02 04 64 00 00 20 04 01 01 01 01 FF 67 89 00 00 00 00 00 00 00 00 00 00 00 00 00 00 BE 16",
                "68 E6 00 E6 00 68 5A 01 01 BA 07 02 04 65 00 00 02 01 01 00 01 00 01 00 21 00 66 55 44 33 22 11 22 22 22 11 11 11 00 05 66 66 66 33 22 11 31 67 89 00 00 00 00 00 00 00 00 00 00 00 00 00 00 6B 16"
        };
        for (String s : ss) {
            NettyStarter.sendTest(s);
        }
    }
}
