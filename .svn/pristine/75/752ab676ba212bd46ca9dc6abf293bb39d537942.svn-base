package com.telek.hemsipc.protocol.iec104.server;

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.CauseOfTransmission;
import org.openmuc.j60870.Connection;
import org.openmuc.j60870.ConnectionEventListener;
import org.openmuc.j60870.ServerEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:业务实现。总召。
 * 
 * @author lxf
 * @created 2017年12月29日 下午2:16:23
 */
public class ServerListener implements ServerEventListener {
    //private static final Logger LOG = Logger.getLogger(ServerListener.class);
    private Logger LOG = LoggerFactory.getLogger(ServerListener.class);
    
    static Map<Integer, ConnectionListener> connectionListenerMap = new HashMap<Integer, ServerListener.ConnectionListener>();

    public class ConnectionListener implements ConnectionEventListener {
        private final Connection connection;
        private final int connectionId;

        public ConnectionListener(Connection connection, int connectionId) {
            this.connection = connection;
            this.connectionId = connectionId;
        }

        @Override
        public void newASdu(ASdu aSdu) {
            try {
                switch (aSdu.getTypeIdentification()) {
                case C_IC_NA_1:
                    LOG.info("收到总召唤命令");
                    // 先确认激活
                    connection.sendConfirmation(aSdu);
                    // 再回复 带时标的单点遥信信息(开关)、测值浮点数(电流电压功率等)
                    ASdu singlePointAsdu = DataCollectionHelper.getSinglePointASduBy(
                            DataCollectionServerStart.commonAddress, CauseOfTransmission.PERIODIC, true);
                    connection.send(singlePointAsdu);
                    ASdu shortFloatAsdu = DataCollectionHelper
                            .getShortFloatASduBy(DataCollectionServerStart.commonAddress);
                    connection.send(shortFloatAsdu);
                    // 最后停止激活
                    ASdu activationTerminationaAsdu = DataCollectionHelper
                            .getActivationTerminationaSduBy(aSdu);
                    connection.send(activationTerminationaAsdu);
                    LOG.info("总召唤命令回复遥测值" + shortFloatAsdu.toString());
                    break;
                default:
                    LOG.info("收到未知请求: " + aSdu.toString());
                }
            } catch (EOFException e) {
                LOG.info("socket 关闭.connectionId=" + connectionId);
                connectionListenerMap.remove(connectionId);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        void sendASdu(ASdu aSdu) {
            try {
                LOG.info("主动发送给主站的aSdu=" + aSdu.toString());
                connection.send(aSdu);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        @Override
        public void connectionClosed(IOException e) {
            LOG.info("socket 关闭connectionId=" + connectionId);
            LOG.info(e.getMessage(), e);
        }
    }

    @Override
    public void connectionIndication(Connection connection) {
        int myConnectionId = DataCollectionServerStart.connectionIdCounter++;
        LOG.info("收到一个新连接。Connection=" + myConnectionId);
        try {
            ConnectionListener connectionListener = new ConnectionListener(connection, myConnectionId);
            connection.waitForStartDT(connectionListener, 5000);
            connectionListenerMap.put(myConnectionId, connectionListener);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return;
        } catch (TimeoutException e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("新连接监听建立成功。Connection=" + myConnectionId);
    }

    @Override
    public void serverStoppedListeningIndication(IOException e) {
        LOG.error(e.getMessage(), e);
    }

    @Override
    public void connectionAttemptFailed(IOException e) {
        LOG.error(e.getMessage(), e);
    }

    /**
     * Description: 给所有主站发送aSdu数据包
     * 
     * @author lxf
     * @created 2017年12月26日 下午1:57:00
     * @param aSdu
     */
    public static void sendASdu(ASdu aSdu) {
        if (connectionListenerMap.size() > 0) {
            for (ConnectionListener connectionListener : connectionListenerMap.values()) {
                if (connectionListener != null) {
                    connectionListener.sendASdu(aSdu);
                }
            }
        }
    }
}
