package com.telek.hemsipc.protocol.iec104.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.CauseOfTransmission;
import org.openmuc.j60870.ClientConnectionBuilder;
import org.openmuc.j60870.Connection;
import org.openmuc.j60870.ConnectionEventListener;
import org.openmuc.j60870.IeQualifierOfInterrogation;
import org.openmuc.j60870.IeShortFloat;
import org.openmuc.j60870.IeTime56;
import org.openmuc.j60870.InformationElement;
import org.openmuc.j60870.InformationObject;
import org.openmuc.j60870.internal.cli.Action;
import org.openmuc.j60870.internal.cli.ActionException;
import org.openmuc.j60870.internal.cli.ActionListener;
import org.openmuc.j60870.internal.cli.ActionProcessor;
import org.openmuc.j60870.internal.cli.CliParameter;
import org.openmuc.j60870.internal.cli.CliParameterBuilder;
import org.openmuc.j60870.internal.cli.CliParseException;
import org.openmuc.j60870.internal.cli.CliParser;
import org.openmuc.j60870.internal.cli.IntCliParameter;
import org.openmuc.j60870.internal.cli.StringCliParameter;

/**       
 * @Class Name：ControlClient    （在主站-设备端的关系中，server对应的设备端使用，client对应主站使用。）
 * @Class Description：    
 * @Creater：lxf    
 * @Create Time：2018年10月23日下午1:15:59  
 * @Modification Time：2018年10月23日下午1:15:59    
 * @Remarks：        
 */
public final class ControlClientTestLocal {
    private static final Logger LOG = Logger.getLogger(ControlClientTestLocal.class);
    private static String host = "localhost";
    private static int port = 2404;
    private static final String INTERROGATION_ACTION_KEY = "i";
    private static final String CLOCK_SYNC_ACTION_KEY = "c";

    private static final StringCliParameter hostParam = new CliParameterBuilder("-h")
            .setDescription("The IP/domain address of the server you want to access.")
            .setMandatory()
            .buildStringParameter(host);
    private static final IntCliParameter portParam = new CliParameterBuilder("-p")
            .setDescription("The port to connect to.")
            .buildIntParameter("port", port);
    private static final IntCliParameter commonAddrParam = new CliParameterBuilder("-ca")
            .setDescription("The address of the target station or the broad cast address.")
            .buildIntParameter("common_address", 1);

    private static volatile Connection connection;
    private static final ActionProcessor actionProcessor = new ActionProcessor(new ActionExecutor());

    private static class ClientEventListener implements ConnectionEventListener {

        @Override
        public void newASdu(ASdu aSdu) {
            try {
                switch (aSdu.getTypeIdentification()) {
                case C_TS_TA_1:
                    LOG.info("收到终端测试包");
                   //发送总召请求
                   
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
                    default:
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
                    InformationObject[] infos = aSdu.getInformationObjects();
//                    for(InformationObject informationObject : infos) {
//                        //LOG.info("收到遥测。电量等信息"+ informationObject.toString());
//                        for(InformationElement[] informationElement : informationObject.getInformationElements()) {
//                            IeShortFloat ie= (IeShortFloat) informationElement[0];
//                            //IeShortFloat ie2= (IeShortFloat) informationElement[1];
//                            System.out.println("电量等信息=="+ie.getValue());
//                            System.out.println("电量等信息int=="+Float.floatToRawIntBits(ie.getValue()));
//                            //System.out.println("电量等信息22=="+ie2.getValue());
//                        }
//                    }
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

        @Override
        public void connectionClosed(IOException e) {
            System.out.print("Received connection closed signal. Reason: ");
            if (!e.getMessage().isEmpty()) {
                System.out.println(e.getMessage());
            }
            else {
                System.out.println("unknown");
            }
            actionProcessor.close();
        }

    }

    private static class ActionExecutor implements ActionListener {

        @Override
        public void actionCalled(String actionKey) throws ActionException {
            try {
                switch (actionKey) {
                case INTERROGATION_ACTION_KEY:
                    System.out.println("** Sending general interrogation command.");
                    connection.interrogation(commonAddrParam.getValue(), CauseOfTransmission.ACTIVATION,
                            new IeQualifierOfInterrogation(20));
                    Thread.sleep(2000);
                    break;
                case CLOCK_SYNC_ACTION_KEY:
                    System.out.println("** Sending synchronize clocks command.");
                    connection.synchronizeClocks(commonAddrParam.getValue(), new IeTime56(System.currentTimeMillis()));
                    break;
                default:
                    break;
                }
            } catch (Exception e) {
                throw new ActionException(e);
            }
        }

        @Override
        public void quit() {
            System.out.println("** Closing connection.");
            connection.close();
            return;
        }
    }

    public static void main(String[] args) {

        List<CliParameter> cliParameters = new ArrayList<>();
        cliParameters.add(hostParam);
        cliParameters.add(portParam);
        cliParameters.add(commonAddrParam);

        CliParser cliParser = new CliParser("j60870-console-client",
                "A client/master application to access IEC 60870-5-104 servers/slaves.");
        cliParser.addParameters(cliParameters);

        try {
            cliParser.parseArguments(new String[] {"-h",host});
        } catch (CliParseException e1) {
            System.err.println("Error parsing command line parameters: " + e1.getMessage());
            System.out.println(cliParser.getUsageString());
            System.exit(1);
        }

        InetAddress address;
        try {
            address = InetAddress.getByName(hostParam.getValue());
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + hostParam.getValue());
            return;
        }

        ClientConnectionBuilder clientConnectionBuilder = new ClientConnectionBuilder(address)
                .setPort(portParam.getValue());

        try {
            connection = clientConnectionBuilder.connect();
        } catch (IOException e) {
            System.out.println("Unable to connect to remote host: " + hostParam.getValue() + ":"+portParam.getValue());
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
            System.out.println("Starting data transfer timed out. Closing connection.");
            connection.close();
            return;
        } catch (IOException e) {
            System.out.println("Connection closed for the following reason: " + e.getMessage());
            return;
        }
        System.out.println("successfully connected" + hostParam.getValue() + ":"+portParam.getValue());
        actionProcessor.addAction(new Action(INTERROGATION_ACTION_KEY, "interrogation C_IC_NA_1"));
        actionProcessor.addAction(new Action(CLOCK_SYNC_ACTION_KEY, "synchronize clocks C_CS_NA_1"));
        actionProcessor.start();
    }

}
  
