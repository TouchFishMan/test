package com.telek.hemsipc.protocal3761.service.response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.model.PointData;
import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.service.request.ConfirmRequest;
import com.telek.hemsipc.sdmp.SdmpConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Afn0cF129ElecData implements IResponse {
    @Override
    public Object dealReceiveFromTerminal(Packet receivedPacket) {
        try {
            // 获取到的数据
            assembleData(receivedPacket);
            // 回复确认包
            ConfirmRequest confirmRequest = new ConfirmRequest();
            confirmRequest.dealSendToTerminal(receivedPacket.getTerminalAddress(), receivedPacket);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("编码失败");
        }
        return null;
    }

    private void assembleData(Packet receivedPacket) throws ParseException {
        Map<String, Object> data = receivedPacket.getData();
        if (data == null) {
            return;
        }
        String terminalAddress = receivedPacket.getTerminalAddress();
        Integer line = receivedPacket.getLine();
        String deviceId = Protocol3761Cache.TERMINAL_ADDRESS_LINE_DEVICE_ID.get(terminalAddress + line);
        if (deviceId == null) {
            log.error("3761协议保存采集数据错误，设备id不存在， terminalAddress:" + receivedPacket.getTerminalAddress() + "; line:" + receivedPacket.getLine());
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm");
        // {time=19/9/30 15:48, rate=4, totalActiveElec=0.0400, list=[{index=0.0400}, {index=0.0000}, {index=0.0000}, {index=0.0000}]}
        Object t = data.get("time");
        long time = 0L;
        if (t != null && !INVALID.equals(t)) {
            time = sdf.parse(data.get("time").toString()).getTime();
        }
        Object totalActiveElec = data.get("totalActiveElec");
        if (totalActiveElec != null && !INVALID.equals(totalActiveElec)) {
            Device device = DeviceContext.deviceMap.get(deviceId);
            Integer elecRate = device.getElecRate();
            if (elecRate == null) {
                elecRate = 1;
            }
            double tae = Double.parseDouble(totalActiveElec.toString()) * elecRate;
            DeviceContext.setCurrentPointData(deviceId, SdmpConstant.PDU_ELEC_ACTIVE_POWER, new PointData(time, tae));
        }
    }
}
