package com.telek.hemsipc.protocol.iec104.serverTest;

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.CauseOfTransmission;
import org.openmuc.j60870.Connection;
import org.openmuc.j60870.ConnectionEventListener;
import org.openmuc.j60870.IeQualifierOfInterrogation;
import org.openmuc.j60870.IeShortFloat;
import org.openmuc.j60870.ServerEventListener;

import com.telek.hemsipc.util.StringUtil;

/**
 * Description:业务实现。总召。
 * 
 * @author lxf
 * @created 2017年12月29日 下午2:16:23
 */
public class ServerListener implements ServerEventListener {
    private static final Logger LOG = Logger.getLogger(ServerListener.class);
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
                case C_TS_TA_1:
                    LOG.info("收到终端测试包");
                   //发送总召请求
                    connection.interrogation(2, CauseOfTransmission.ACTIVATION,
                            new IeQualifierOfInterrogation(20));
                    break;
                case C_IC_NA_1:
                    //总召
                    switch (aSdu.getCauseOfTransmission()) {
                        case ACTIVATION_CON:
                            //激活确认
                            LOG.info("收到激活确认");
                            break;
                        case ACTIVATION_TERMINATION:
                            //总召结束
                            LOG.info("收到总召结束");
                            connection.sendConfirmation(aSdu);
                            //
                            //connection.send(aSdu);
                            break;
                    }
                    break;
                case M_SP_NA_1:
                    //遥信。开关等信息
                    LOG.info("收到遥信。开关等信息");
                    //TODO
                    break;
                case M_ME_NC_1:
                    //遥测。电量等信息
                    LOG.info("收到遥测。电量等信息");
                    //TODO
                    break;
                default:
                    LOG.info("收到未知请求: " + aSdu.toString());
                }
                LOG.info("接收的具体信息ASDU:" + aSdu);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }

        void sendASdu(ASdu aSdu) {
            try {
                LOG.info("主动发送给终端的aSdu=" + aSdu.toString());
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
            connectionListenerMap.put(myConnectionId, connectionListener);
            connection.waitForStartDT(connectionListener, 0);
            //connection.startDataTransfer(connectionListener, 5000);
           
            connection.interrogation(2, CauseOfTransmission.ACTIVATION,
                    new IeQualifierOfInterrogation(20));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return;
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
    
    public static void main(String[] args) {
        float f = Float.intBitsToFloat(16450);
        int b1 = StringUtil.hexStringToInt("42");
        int b2 = StringUtil.hexStringToInt("40");
        int b3 = StringUtil.hexStringToInt("00");
        int b4 = StringUtil.hexStringToInt("00");
        int int1 = (b1 & 0xff) | ((b2 & 0xff) << 8)
                | ((b3 & 0xff) << 16) | ((b4 & 0xff) << 24);
        
        int int2 = (b4 & 0xff) | ((b3 & 0xff) << 8)
                | ((b2 & 0xff) << 16) | ((b1 & 0xff) << 24);
        
        int int3 = ((b1 & 0xff) << 24 ) | ((b2 & 0xff) << 16)
                | ((b3 & 0xff) << 8) | ((b4 & 0xff) );
        System.out.println("电量等信息int1=="+int1);
        System.out.println("电量等信息int2=="+int2);
        System.out.println("电量等信息int3=="+int3);
        float f2 = Float.intBitsToFloat(int1);
        float f3 = Float.intBitsToFloat(int2);
        float f4 = Float.intBitsToFloat(int3);

        System.out.println("电量等信息1=="+f);
        System.out.println("电量等信息2=="+f2);
        System.out.println("电量等信息3=="+f3);
        System.out.println("电量等信息4=="+f4);
        int newInt = Float.floatToIntBits(f2);
        String hexStr = Integer.toHexString(newInt);
        System.out.println("电量等信息hexStr=="+hexStr);
        int b11 = StringUtil.hexStringToInt(""+newInt/1000000);
        int b21 = StringUtil.hexStringToInt(""+newInt/10000%100);
        int b31 = StringUtil.hexStringToInt(""+newInt/100%10000);
        int b41 = StringUtil.hexStringToInt(""+newInt%1000000);
        int int5 = (b11 & 0xff) | ((b21 & 0xff) << 8)
                | ((b31 & 0xff) << 16) | ((b41 & 0xff) << 24);
        float f5 = Float.intBitsToFloat(int5);
        System.out.println("电量等信息5=="+f5);
    }
}
