package com.telek.hemsipc.protocol.iec104.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.CauseOfTransmission;
import org.openmuc.j60870.ClientConnectionBuilder;
import org.openmuc.j60870.Connection;
import org.openmuc.j60870.ConnectionEventListener;
import org.openmuc.j60870.IeQualifierOfInterrogation;
import org.openmuc.j60870.IeTime56;
import org.openmuc.j60870.InformationObject;
import org.openmuc.j60870.internal.cli.CliParameter;
import org.openmuc.j60870.internal.cli.CliParameterBuilder;
import org.openmuc.j60870.internal.cli.CliParseException;
import org.openmuc.j60870.internal.cli.CliParser;
import org.openmuc.j60870.internal.cli.IntCliParameter;
import org.openmuc.j60870.internal.cli.StringCliParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**       
 * @Class Name：ControlClient    （在主站-设备端的关系中，server对应的设备端使用，client对应主站使用。）
 * @Class Description：    
 * @Creater：lxf    
 * @Create Time：2018年10月23日下午1:15:59  
 * @Modification Time：2018年10月23日下午1:15:59    
 * @Remarks：        
 */
public final class ControlClient {
    private static Logger LOG = LoggerFactory.getLogger(ControlClient.class);
   
    private final String INTERROGATION_ACTION_KEY = "i";
    private final String CLOCK_SYNC_ACTION_KEY = "c";
    
    private StringCliParameter hostParam = null;
    private IntCliParameter portParam = null;
    private IntCliParameter commonAddrParam = null;
    private volatile Connection connection = null;
    
    /**    
     * isInterrogationEnd:总召是否结束    
     */    
    private volatile Boolean isInterrogationEnd = null;
    
    private class ClientEventListener implements ConnectionEventListener {

        @Override
        public void newASdu(ASdu aSdu) {
            LOG.info("接收 ASDU:" + aSdu);
            int commonAddress = aSdu.getCommonAddress();
            InformationObject[] informationObject = aSdu.getInformationObjects();
            switch (aSdu.getTypeIdentification()) {
                case C_IC_NA_1:
                    //总召
                    switch (aSdu.getCauseOfTransmission()) {
                        case ACTIVATION_CON:
                            //激活确认
                            isInterrogationEnd = false;
                            break;
                        case ACTIVATION_TERMINATION:
                            //总召结束
                            isInterrogationEnd = true;
                            break;
                    }
                    break;
                case M_SP_NA_1:
                    //遥信。开关等信息
                    //TODO
                    break;
                case M_ME_NC_1:
                    //遥测。电量等信息
                    //TODO
                    break;
                default:
                    break;
            }
        }

        @Override
        public void connectionClosed(IOException e) {
            System.out.print("Received connection closed signal. Reason: ");
            if (!e.getMessage().isEmpty()) {
                LOG.info(e.getMessage());
            }
            else {
                LOG.info("unknown");
            }
        }

    }
    
    public ControlClient(String host,int port,int commonAddress) {
        hostParam = new CliParameterBuilder("-h")
            .setDescription("The IP/domain address of the server you want to access.")
            .setMandatory()
            .buildStringParameter(host);
        portParam = new CliParameterBuilder("-p")
                .setDescription("The port to connect to.")
                .buildIntParameter("port", port);
        commonAddrParam = new CliParameterBuilder("-ca")
                .setDescription("The address of the target station or the broad cast address.")
                .buildIntParameter("common_address", commonAddress);
        init();
    }

    public void init() {
        List<CliParameter> cliParameters = new ArrayList<>();
        cliParameters.add(hostParam);
        cliParameters.add(portParam);
        cliParameters.add(commonAddrParam);
        CliParser cliParser = new CliParser("j60870-console-client",
                "A client/master application to access IEC 60870-5-104 servers/slaves.");
        cliParser.addParameters(cliParameters);
        try {
            cliParser.parseArguments(new String[] {"-h","10.10.11.67"});
        } catch (CliParseException e1) {
            LOG.error("Error parsing command line parameters: " + e1.getMessage());
            LOG.info(cliParser.getUsageString());
            return;
        }
        InetAddress address;
        try {
            address = InetAddress.getByName(hostParam.getValue());
        } catch (UnknownHostException e) {
            LOG.info("Unknown host: " + hostParam.getValue());
            return;
        }
        ClientConnectionBuilder clientConnectionBuilder = new ClientConnectionBuilder(address)
                .setPort(portParam.getValue());
        try {
            connection = clientConnectionBuilder.connect();
        } catch (IOException e) {
            LOG.info("Unable to connect to remote host: " + hostParam.getValue() + ".");
            return;
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                connection.close();
            }
        });
        try {
            connection.startDataTransfer(new ClientEventListener(), 5000);
        } catch (TimeoutException e2) {
            LOG.info("Starting data transfer timed out. Closing connection.");
            connection.close();
            return;
        } catch (IOException e) {
            LOG.info("Connection closed for the following reason: " + e.getMessage());
            return;
        }
        LOG.info("successfully connected。host="+hostParam.getValue());
    }
    
    
    
    public boolean isConnectionEffective() {
        if(connection == null) {
            return false;
        }
        return true;
    }
    
    /**   
     * @Modifier:lxf 
     * @Date：2018年10月23日下午5:35:44 
     * @Describe:isInterrogationEnd  总召是否结束     
     * @return                 
    */
    public boolean isInterrogationEnd() {
        if(isInterrogationEnd == null) {
            return false;
        }
        return isInterrogationEnd;
    }
    /**   
     * @Modifier:lxf 
     * @Date：2018年10月23日下午5:14:02 
     * @Describe:interrogation  总召                     
    */
    public void interrogation() {
        if(!isConnectionEffective()) {
            LOG.info("连接失效，无法发送总召请求。host="+hostParam.getValue());
            return ;
        }
        try {
            isInterrogationEnd = null;
            connection.interrogation(commonAddrParam.getValue(), CauseOfTransmission.ACTIVATION,
                    new IeQualifierOfInterrogation(20));
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        }
    }
    
    /**   
     * @Modifier:lxf 
     * @Date：2018年10月23日下午5:27:24 
     * @Describe:synchronizeClocks   时钟同步                    
    */
    public void synchronizeClocks() {
        if(!isConnectionEffective()) {
            LOG.info("连接失效，无法发送时钟同步请求。host="+hostParam.getValue());
            return ;
        }
        try {
            connection.synchronizeClocks(commonAddrParam.getValue(), new IeTime56(System.currentTimeMillis()));
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        }
    }
   
}
  
