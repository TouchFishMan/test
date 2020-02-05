package com.telek.hemsipc.service.impl;

import java.io.IOException;
import java.net.InetAddress;

import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.CauseOfTransmission;
import org.openmuc.j60870.ClientConnectionBuilder;
import org.openmuc.j60870.Connection;
import org.openmuc.j60870.ConnectionEventListener;
import org.openmuc.j60870.IeQualifierOfInterrogation;
import org.openmuc.j60870.IeShortFloat;
import org.openmuc.j60870.InformationElement;
import org.openmuc.j60870.InformationObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.context.Iec104Context;
import com.telek.hemsipc.context.SysContext;
import com.telek.hemsipc.model.PointData;
import com.telek.hemsipc.model.SysPointTableConfig;
import com.telek.hemsipc.service.IIec104DataCollectionService;
import com.telek.hemsipc.util.StringUtil;

/**
 * 按104协议采集终端数据service实现类
 * @Class Name：Iec104DataCollectionServiceImpl    
 * @Class Description：    
 * @Creater：kds    
 * @Create Time：2018年11月14日下午1:24:51    
 * @Modifier：kds    
 * @Modification Time：2018年11月14日下午1:24:51    
 * @Remarks：
 */
@Lazy
@Service
public final class Iec104DataCollectionServiceImpl implements IIec104DataCollectionService{
    //private static final Logger log = Logger.getLogger(Iec104DataCollectionServiceImpl.class);
    private static Logger log = LoggerFactory.getLogger(Iec104DataCollectionServiceImpl.class);
    private static String host = "192.168.160.242"; //默认值，如果数据库中有配置，以数据库中值为准
    private static int port = 2404; //默认值，如果数据库中有配置，以数据库中值为准
    private static int commonAddress = 1; //默认值，如果数据库中有配置，以数据库中值为准
    private static boolean initAddress = false;

    private static volatile Connection connection;
 
    private static class ClientEventListener implements ConnectionEventListener {
        @Override
        public void newASdu(ASdu aSdu) {
            try {
                log.info("==============================================" + aSdu.getTypeIdentification());
                switch (aSdu.getTypeIdentification()) {
                case C_TS_TA_1:
                    log.info("收到终端测试包，连接成功");
                    break;
                case C_IC_NA_1:
                    //总召
                    switch (aSdu.getCauseOfTransmission()) {
                        case ACTIVATION_CON: // 开始数据传输
                            //激活确认
                            log.info("收到激活确认");
                            break;
                        case ACTIVATION_TERMINATION:
                            //总召结束
                            log.info("收到总召结束");
                            break;
                    default:
                        log.info("收到未知请求---------------------: " + aSdu.toString());
                        break;
                    }
                    break;
                case M_SP_NA_1:
                    //遥信。开关等信息
                    log.info("收到遥信。开关等信息");
                    break;
                case M_ME_NC_1:
                    //遥测。电量等信息
                    log.info("收到遥测。电量等信息");
                    InformationObject[] infos = aSdu.getInformationObjects();
                    aSdu.getCommonAddress();//获取公共地址
                    for(InformationObject informationObject : infos) {
                        int infoAddress = informationObject.getInformationObjectAddress();//获取信息地址
                        for(InformationElement[] informationElement : informationObject.getInformationElements()) {
                            IeShortFloat ie= (IeShortFloat) informationElement[0];
                            //StringUtil.reversal(ie.getValue());
                            log.info("采集到的数据============================== infoAddress "+ infoAddress + " value: " + ie.getValue() + " 转码后value：" + StringUtil.reversal(ie.getValue()));
                            setDeviceValue(infoAddress, ie.getValue()); 
                            infoAddress ++;
                        }
                    }
                    break;
                case M_PS_NA_1:
                    log.info("收到变位上送"); 
                default:
                    log.info("收到未知请求: " + aSdu.toString());
                }
                log.info("接收的具体信息ASDU:" + aSdu);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        @Override
        public void connectionClosed(IOException e) {
            log.info("Received connection closed signal. Reason: " + e.getMessage());
        }

    }
    
    /**
     * 将采集到的数据设置到设备当前数据对象中
     * @Modifier:kds 
     * @Date：2018年11月15日上午11:08:17 
     * @Describe:setDeviceValue      
     * @param informationAddress
     * @param value
     */
    private static void setDeviceValue(int informationAddress, float value) {
        try{
            SysPointTableConfig pointTableConfig = Iec104Context.getSysPointTableConfigMap(informationAddress);
            if(pointTableConfig != null) {
                log.info("----------------------采集到的数据  key  " + pointTableConfig.getSdmpKey() + "  value: " + value);
                String collectionDeviceID = pointTableConfig.getCollectionDeviceId();
                DeviceContext.setCurrentPointData(collectionDeviceID, pointTableConfig.getSdmpKey(), new PointData(System.currentTimeMillis(), value * pointTableConfig.getMultiplier()));
            }
        }catch(Exception e) {
            log.error("setDeviceValue 异常", e);
        }
        
    }

    @Override
    public void sendQueryRequest() {
        if(connection == null) {
            try {
                if(!initAddress) {
                    host = SysContext.getSysConfig(Constant.IP).getConfigValue();
                    port = StringUtil.parseInt(SysContext.getSysConfig(Constant.PORT).getConfigValue());
                    commonAddress = StringUtil.parseInt(SysContext.getSysConfig(Constant.COMMON_ADDRESS).getConfigValue());
                    initAddress = true;
                }
                
                ClientConnectionBuilder clientConnectionBuilder = new ClientConnectionBuilder(InetAddress.getByName(host)).setPort(port);
                connection = clientConnectionBuilder.connect();
            } catch (IOException e) {
                log.error("Unable to connect to remote host: " + host + ":"+ port);
                connection = null;
                return;
            }
            
            try {
                connection.startDataTransfer(new ClientEventListener(), 5000);
            } catch (Exception e) {
                log.error("Starting data transfer timed out. Closing connection.", e);
                connection.close();
                connection = null;
                return;
            } 
            log.info("successfully connected" + host + ":"+port);
            
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    connection.close();
                    connection = null;
                }
            });
        }
        
        //发送总召请求
        try {
            connection.interrogation(commonAddress, CauseOfTransmission.ACTIVATION, new IeQualifierOfInterrogation(20));
        } catch (Exception e) {
            log.error("发送总召请求出错", e);
            connection.close();
            connection = null;
            return;
        }
    }
    

}
  
