package com.telek.hemsipc.protocol.iec104.serverTest;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openmuc.j60870.Server;

/**
 * Description:启动类。测试用。（在主站-设备端的关系中，server对应的设备端使用，client对应主站使用。）
 * 
 * @author lxf
 * @created 2017年12月29日 下午2:16:03
 */
public class DataCollectionServerStart {
    private static final Logger LOG = Logger.getLogger(ServerListener.class);
    public static int connectionIdCounter = 1;
    public static final int port = 2404;
    /**
     * 公共地址固定为1
     */
    public static int commonAddress = 1;

    public static void main(String[] args) {
        new DataCollectionServerStart().start();
    }

    public void start() {
        Server server = new Server.Builder().setPort(port).build();
        try {
            server.start(new ServerListener());
            LOG.info("104协议监听服务启动成功test");
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return;
        }
    }
}
