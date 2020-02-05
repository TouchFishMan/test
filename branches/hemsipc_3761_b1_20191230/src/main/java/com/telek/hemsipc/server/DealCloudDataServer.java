package com.telek.hemsipc.server;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.contant.ControlType;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.context.HemsipcSpringContext;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.sdmp.Integer32;
import com.telek.hemsipc.sdmp.PDU;
import com.telek.hemsipc.sdmp.SDMPv1;
import com.telek.hemsipc.sdmp.SdmpConstant;
import com.telek.hemsipc.sdmp.VarBind;
import com.telek.hemsipc.service.ICloudService;
import com.telek.hemsipc.service.ServiceFactory;
import com.telek.hemsipc.service.impl.CloudServiceImpl;
import com.telek.hemsipc.util.StringUtil;
import com.telek.hemsipc.util.ThreadPoolUtil;

public class DealCloudDataServer implements Runnable {
    private Logger log = LoggerFactory.getLogger(DealCloudDataServer.class);
    private SDMPv1 sdmPv1;

    public DealCloudDataServer(SDMPv1 sdmPv1) {
        this.sdmPv1 = sdmPv1;
    }

    private ICloudService cloudService = HemsipcSpringContext.getBean(CloudServiceImpl.class);

    private volatile Map<String, Integer> cloudMsgIdMap = new ConcurrentHashMap<String, Integer>();

    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            log.error("接收云端反馈出错", e);
        }
    }

    private void process() throws Exception {
        String deviceId = sdmPv1.getMsgAuthoritativeEngineID().toString();
        if (!sdmPv1.isFlag()) {
            log.error("云端报文包解码失败");
            return;
        }
        PDU msgData = sdmPv1.getMsgData();
        int requestType = msgData.getRequestType().toInt();
        int actionType = msgData.getActionType().toInt();
        int msgId = msgData.getMsgID().toInt();
        log.info("deviceId:" + deviceId + ";接收云端包:requestType:" + requestType + " actionType:" + actionType + " msgId:"
                + msgId);
        switch (requestType) {
            case SdmpConstant.REQUESTID_RESPONSE:
                netWorkInit();
                // 响应
                if (actionType == SdmpConstant.SDMP_RESPONSE_ACTIVE) {
                    // 处理激活响应
                    dealDeviceActive(msgData, deviceId);
                }
            case SdmpConstant.REQUESTID_COMMAND:
                if (actionType != 101) {
                    log.info("－－－－－－－－－－收到控制指令　　actionType is: " + actionType);
                }
                int msgIdPre = cloudMsgIdMap.containsKey(deviceId) ? cloudMsgIdMap.get(deviceId) : 0;
                if (msgId < msgIdPre) {
                    log.error("msgId验证错误，当前sdmp包msgId：" + msgId + ",之前的msgId：" + msgIdPre);
                    return;
                }
                if (actionType == SdmpConstant.SDMP_COMMAND_RELAY) {  // 控制继电器指令
                    dealOnOff(msgData, deviceId);
                } else if (actionType == SdmpConstant.SDMP_COMMAND_POWER_PERCENT_CONTROL) { // 变频柜 频率控制
                	dealPowerControl(msgData, deviceId);
                } else if (actionType == SdmpConstant.SDMP_COMMAND_FREQUENCY_CONVERTER_CONTROL) { // 变频柜 启停控制
                    dealFrequencyConversionOnOFF(msgData, deviceId);
                } else if (actionType == SdmpConstant.SDMP_COMMNAD_IPC_UPDATE) {
                    dealUpdate(msgData, deviceId);
                } else if (actionType == SdmpConstant.SDMP_COMMNAD_CONVERT_LOCAL_REMOTE_CONTROL) { // 本地控制与远程控制切换
                    dealCenterAirSystemLocalRemoteControlConvert(msgData, deviceId);
                }
                else if (actionType == SdmpConstant.SDMP_COMMNAD_CONVERT_MANUAL_AUTO_CONTROL) { // 自动控制与手动控制切换
                	int autoOrManual = StringUtil.parseInt(msgData.getValueByKey(SdmpConstant.PDU_IS_AUTO_CONTROL));
                    String controlType = autoOrManual == Constant.AUTO_CONTROL ? ControlType.AUTO_CONTROL.getName() : ControlType.MANUAL_CONTROL.getName();
                    dealCommonControl(controlType, msgData, deviceId, 0);
                }else if (actionType == SdmpConstant.SDMP_COMMNAD_AIR_MODEL_CONTROL) { // 空调系统启动模式控制 
                    dealCommonControl(ControlType.AIR_CONDITION_MODEL.getName(), msgData, deviceId, StringUtil.parseInt(msgData.getValueByKey(SdmpConstant.PDU_AIR_MODEL)));
                } else if (actionType == SdmpConstant.SDMP_COMMNAD_AIR_TERMINAL_MODEL_CONTROL) { // 末端制冷制热切换
                    int model = StringUtil.parseInt(msgData.getValueByKey(SdmpConstant.PDU_AIR_TERMINAL_COLD_HOT_MODEL));
                    String controlType = model == Constant.MODEL_COLD ? ControlType.COLD.getName() : ControlType.HOT.getName();
                    dealCommonControl(controlType, msgData, deviceId, 0);
                } else if (actionType == SdmpConstant.SDMP_COMMNAD_TEMPERATURE) { // 温度设定
                    dealCommonControl(ControlType.TEMPERATURE.getName(), msgData, deviceId, StringUtil.parseFloat(msgData.getValueByKey(SdmpConstant.PDU_TEMPERATURE)));
                } else if (actionType == SdmpConstant.SDMP_COMMNAD_OPEN_VALUE) { // 阀门开度
                    dealCommonControl(ControlType.OPEN_VALUE.getName(), msgData, deviceId, StringUtil.parseFloat(msgData.getValueByKey(SdmpConstant.PDU_OPEN_VALUE)));
                }

                // 升级指令
                updateCloudMsgId(deviceId, msgId);
                break;
            default:
                break;
        }
    }

    /**
     * 处理激活请求
     *
     * @param msgData
     * @throws Exception
     */
    private void dealDeviceActive(PDU msgData, String deviceId) {
        log.info("{}收到激活返回", deviceId);
        int activeStatus = Integer.parseInt(msgData.getValueByKey(SdmpConstant.PDU_S_ISACTIVE));
        if (activeStatus == 1) {
            // 激活成功,上线通知
            Device device = DeviceContext.deviceMap.get(deviceId);
            cloudService.sendOnline(device);
        }
    }

    private synchronized void updateCloudMsgId(String deviceId, int msgId) {
        cloudMsgIdMap.put(deviceId, msgId);
    }

    /**
     * @Description: 收到云端升级固件指令
     * @auther: wll
     * @date: 13:52 2018/9/26
     * @param: [msgData]
     * @return: void
     */
    private void dealUpdate(PDU msgData, String deviceId) {
        Device device = DeviceContext.deviceMap.get(deviceId);
        int version = Integer.parseInt(msgData.getValueByKey(SdmpConstant.PDU_PROGRAM_DOWN_FIRMWAREVERSION));
        if (version == SdmpConstant.DEVICE_VERSION) {
            log.error("【固件升级】当前固件已经是需要升级的版本");
            return;
        }
        String md5Check = msgData.getValueByKey(SdmpConstant.PDU_IPC_FIRMWARE_MD5);
        String downloadKey = msgData.getValueByKey(SdmpConstant.PDU_IPC_FIRMWARE_DOWNLOAD_KEY);
        ThreadPoolUtil.exceuteFirmwareUpdate(new FirmwareUpdateServer(md5Check, downloadKey));
        cloudService.sendDataFeedback(SdmpConstant.SDMP_RESPONSE_SUCCESS, new Vector<VarBind>(), device, msgData.getMsgID().toInt());
    }

    /**
     * 收到云端控制继电器指令方法
     *
     * @param msgData
     * @throws Exception
     */
    private void dealOnOff(PDU msgData, String deviceId) throws Exception {
        Device device = DeviceContext.deviceMap.get(deviceId);
        int sStatus = Integer.parseInt(msgData.getValueByKey(SdmpConstant.PDU_C_STATE));
 
        sendDataFeedback(msgData, device, SdmpConstant.PDU_C_STATE, sStatus);
        
        String controlType = Constant.ON == sStatus ? ControlType.ON.getName() : ControlType.OFF.getName();
        ServiceFactory.getIControlService(device.getControlType()).control(controlType, device, 0F);
    }
    
    /**
     * 反馈给云端信息
     * @Author:kds 
     * @Date：2019年9月3日上午11:01:40 
     * @Describe:sendDataFeedback  
     * @throws    
     * @param msgData
     * @param device
     * @param key
     * @param value
     */
    private void sendDataFeedback(PDU msgData, Device device, int key, int value) {
    	Vector<VarBind> varBinds = new Vector<>();
    	varBinds.add(new VarBind(key, new Integer32(value)));
        cloudService.sendDataFeedback(SdmpConstant.SDMP_RESPONSE_SUCCESS, varBinds, device, msgData.getMsgID().toInt());
    }

    /**
     * 控制频率指令方法
     *
     * @param msgData
     * @throws Exception
     */
    private void dealPowerControl(PDU msgData, String deviceId) throws Exception {
        Device device = DeviceContext.deviceMap.get(deviceId);
        int powerPercent = Integer.parseInt(msgData.getValueByKey(SdmpConstant.PDU_POWER_PERCENT));
        
        sendDataFeedback(msgData, device, SdmpConstant.PDU_POWER_PERCENT, powerPercent);
        
        String controlType = ControlType.FREQUENCY.getName();
        ServiceFactory.getIControlService(device.getControlType()).control(controlType, device, new Float(powerPercent));
    }

    /**
     * 变频柜 启停控制
     *
     * @param msgData
     * @throws Exception
     */
    private void dealFrequencyConversionOnOFF(PDU msgData, String deviceId) throws Exception {
        Device device = DeviceContext.deviceMap.get(deviceId);
        int sStatus = Integer.parseInt(msgData.getValueByKey(SdmpConstant.PDU_STATE));
  
        sendDataFeedback(msgData, device, SdmpConstant.PDU_STATE, sStatus);
        
        String controlType = Constant.ON == sStatus ? ControlType.ON.getName() : ControlType.OFF.getName();
        ServiceFactory.getIControlService(device.getControlType()).control(controlType, device, 0F);
        
    }

    /**
     * 处理中央空调系统本地控制与远程控制切换
     *
     * @param msgData
     * @param deviceId
     * @throws
     * @throws Exception
     * @Author:kds
     * @Date：2019年7月2日下午3:28:24
     * @Describe:dealCenterAirSystemConvertLocalRemoteControl
     */
    private void dealCenterAirSystemLocalRemoteControlConvert(PDU msgData, String deviceId) throws Exception {
        Device device = DeviceContext.deviceMap.get(deviceId);
        int sStatus = Integer.parseInt(msgData.getValueByKey(SdmpConstant.PDU_IS_REMOTE_CONTROL));
      
        sendDataFeedback(msgData, device, SdmpConstant.PDU_IS_REMOTE_CONTROL, sStatus);
        
        String controlType = sStatus == Constant.REMOTE_CONTROL ? ControlType.REMOTE_CONTROL.getName() : ControlType.LOCAL_CONTROL.getName();
        ServiceFactory.getIControlService(device.getControlType()).control(controlType, device, 0F);
    }

    /**
     * 通用控制
     *
     * @param msgData
     * @param deviceId
     * @throws
     * @throws Exception
     * @Author:kds
     * @Date：2019年7月18日下午4:57:22
     * @Describe:dealControl
     */
    private void dealCommonControl(String controlType, PDU msgData, String deviceId, float value) throws Exception {
        Device device = DeviceContext.deviceMap.get(deviceId);
        // 反馈给云端信息
        Vector<VarBind> varBinds = new Vector<VarBind>();
        cloudService.sendDataFeedback(SdmpConstant.SDMP_RESPONSE_SUCCESS, varBinds, device, msgData.getMsgID().toInt());
        
        ServiceFactory.getIControlService(device.getControlType()).control(controlType, device, value);
    }

    public void netWorkInit() {
        Constant.LAST_NET_TIME = System.currentTimeMillis();
        Constant.CHANGE_PORT_TIMES = 0;
        Constant.FORCE_RESET_NETWORK = 0;
        // 如果原先系统处于离线状态，则需要开启电量上报线程进行补报电量
        if (Constant.SERVER_ONLINE == false) {
            ThreadPoolUtil.executeElecSupply(new ElecSupplyServer());
            Constant.SERVER_ONLINE = true;
        }
    }
}
