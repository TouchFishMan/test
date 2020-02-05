package com.telek.hemsipc.service.impl;

import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.netty.init.CloudClientPoint;
import com.telek.hemsipc.repository.ICommonDAO;
import com.telek.hemsipc.sdmp.OctetString;
import com.telek.hemsipc.sdmp.PDU;
import com.telek.hemsipc.sdmp.SDMPv1;
import com.telek.hemsipc.sdmp.SdmpConstant;
import com.telek.hemsipc.sdmp.VarBind;
import com.telek.hemsipc.sdmp.resend.SendMap;
import com.telek.hemsipc.service.ICloudService;

/**
 * @Auther: wll
 * @Date: 2018/9/14 14:17
 * @Description:
 */
@Service("cloudService")
public class CloudServiceImpl implements ICloudService {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ICommonDAO commonDAO;
    @Autowired
    private CloudClientPoint clientPoint;

    /**
     * 组装sdmp
     *
     * @param requestType
     * @param actionType
     * @param varBinds
     * @return
     */
    private SDMPv1 getSdmpData(int requestType, int actionType, Vector<VarBind> varBinds, Device device) {
        SDMPv1 sdmpData = new SDMPv1();
        sdmpData.setMsgVersion(SdmpConstant.DEVICE_VERSION);
        sdmpData.setMsgAuthoritativeEngineID(device.getDeviceId().getBytes());
        //构建PDU
        PDU pdu = new PDU();
        pdu.setMsgID(updateMsgId(device));
        pdu.setRequestType(requestType);
        pdu.setActionType(actionType);
        pdu.setVariableBindings(varBinds);
        sdmpData.setMsgData(pdu);
        // 加密报文数据
        sdmpData.setKey(device.getGenkey().getBytes());
        return sdmpData;
    }

    /**
     * @Description: 更新msg_id并返回
     * @auther: wll
     * @date: 15:37 2018/9/14
     * @param: [device]
     * @return: int
     */
    private int updateMsgId(Device device) {
        int msgId = device.getMsgId();
        device.setMsgId(msgId + 1);
        DeviceContext.deviceMap.put(device.getDeviceId(), device);
        commonDAO.saveOrUpdate(device);
        return msgId + 1;
    }

    /**
     * @Description: 将sdmp包发送到云服务
     * @auther: wll
     * @date: 15:48 2018/9/14
     * @param: [sdmpData]
     * @return: void
     */
    private void sendMessage(SDMPv1 sdmpData) {
        log.info("deviceId={" + sdmpData.getMsgAuthoritativeEngineID().toString() + "};向云端发送报文:request:{" + sdmpData.getMsgData().getRequestType().getValue() 
        		+ "},actiontype:{" + sdmpData.getMsgData().getActionType().getValue() + "},msgId:{" + sdmpData.getMsgData().getMsgID() + "}" );
        // 往云端发送消息
        clientPoint.sendMsg(sdmpData);
    }

    /**
     * 向云端发送设备反馈包
     *
     * @param actionType
     * @param varBinds
     * @param device
     */
    @Override
    public void sendDataFeedback(int actionType, Vector<VarBind> varBinds, Device device, int msgID) {
        // 数据编码加密
        SDMPv1 sdmpData = getSdmpData(SdmpConstant.REQUESTID_RESPONSE, actionType, varBinds, device);
        sdmpData.getMsgData().setMsgID(msgID);
        sendMessage(sdmpData);
    }

    /**
     * @Description: 发送心跳包到云服务
     * @auther: wll
     * @date: 15:56 2018/9/14
     * @param: [varBinds, device]
     * @return: void
     */
    @Override
    public void sendHeartBeat(Vector<VarBind> varBinds, Device device) {
        SDMPv1 sdmpData = getSdmpData(SdmpConstant.REQUESTID_REPORT, 0, varBinds, device);
        sendMessage(sdmpData);
        SendMap.addRendMap(sdmpData);
    }

    @Override
    public void sendOnline(Device device) {
        Vector<VarBind> varBinds = new Vector<>();
        varBinds.add(new VarBind(SdmpConstant.PDU_MAC_ADD, new OctetString(device.getSlaveAdd())));
        SDMPv1 sdmpData = getSdmpData(SdmpConstant.REQUESTID_TRAP, SdmpConstant.DEVICE_ONLINE_NOTIFY, varBinds, device);
        sendMessage(sdmpData);
    }

    @Override
    public void sendActive(Device device) {
        SDMPv1 sdmpData = getSdmpData(SdmpConstant.REQUESTID_TRAP, SdmpConstant.DEVICE_REQUEST_ACTIVE, new Vector<VarBind>(), device);
        sendMessage(sdmpData);
    }
}
