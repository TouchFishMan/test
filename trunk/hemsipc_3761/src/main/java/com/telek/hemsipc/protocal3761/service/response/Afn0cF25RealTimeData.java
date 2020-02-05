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
import com.telek.hemsipc.util.MathUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 链路检测
 */
@Slf4j
public class Afn0cF25RealTimeData implements IResponse {

    /**
     * 水泥厂两个需要特别处理的集中器
     */
    private static final String TERMINAL_ADDRESS1 = "21920792";
    private static final String TERMINAL_ADDRESS2 = "21920764";

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
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm");
        String terminalAddress = receivedPacket.getTerminalAddress();
        Integer line = receivedPacket.getLine();
        String deviceId = Protocol3761Cache.TERMINAL_ADDRESS_LINE_DEVICE_ID.get(terminalAddress + line);
        if (deviceId == null) {
            log.error("3761协议保存采集数据错误，设备id不存在， terminalAddress:" + receivedPacket.getTerminalAddress() + "; line:" + receivedPacket.getLine());
            return;
        }
        long time = 0;
        Object t = data.get("time");
        if (t != null && !INVALID.equals(t)) {
            time = sdf.parse(data.get("time").toString()).getTime();
        }
        // 三相电压和电流
        verifyCurrentAndVoltage(data);

        Device device = DeviceContext.deviceMap.get(deviceId);
        log.info(device.toString());
        double voltageA = setValue(data, deviceId, time, "voltageA", SdmpConstant.PDU_U_A, device.getUrate());
        double voltageB = setValue(data, deviceId, time, "voltageB", SdmpConstant.PDU_U_B, device.getUrate());
        double voltageC = setValue(data, deviceId, time, "voltageC", SdmpConstant.PDU_U_C, device.getUrate());
        double currentA = setValue(data, deviceId, time, "currentA", SdmpConstant.PDU_I_A, device.getIrate());
        double currentB = setValue(data, deviceId, time, "currentB", SdmpConstant.PDU_I_B, device.getIrate());
        double currentC = setValue(data, deviceId, time, "currentC", SdmpConstant.PDU_I_C, device.getIrate());

        // 特殊处理功率
        if (TERMINAL_ADDRESS1.equals(terminalAddress) || TERMINAL_ADDRESS2.equals(terminalAddress)) {
            double invalid = Double.parseDouble(INVALID);
            if (voltageA != invalid && voltageB != invalid && voltageC != invalid
                    && currentA != invalid && currentB != invalid && currentC != invalid) {

                // 视在功率
                double appPowerA = voltageA * currentA;
                double appPowerB = voltageA * currentA;
                double appPowerC = voltageA * currentA;
                double totalAppPower = appPowerA + appPowerB + appPowerC;

                double activePowerA = appPowerA * 0.78;
                double activePowerB = appPowerB * 0.78;
                double activePowerC = appPowerC * 0.78;
                double totalActivePower = activePowerA + activePowerB + activePowerC;

                data.put("totalActivePower", MathUtil.divide(totalActivePower, 1000, 5));
                data.put("totalA_ActivePower", MathUtil.divide(activePowerA, 1000, 5));
                data.put("totalB_ActivePower", MathUtil.divide(activePowerB, 1000, 5));
                data.put("totalC_ActivePower", MathUtil.divide(activePowerC, 1000, 5));

                data.put("totalAppPower", MathUtil.divide(totalAppPower, 1000, 5));
                data.put("totalAppPowerA", MathUtil.divide(appPowerA, 1000, 5));
                data.put("totalAppPowerB", MathUtil.divide(appPowerB, 1000, 5));
                data.put("totalAppPowerC", MathUtil.divide(appPowerC, 1000, 5));
            }

        }

        setValue(data, deviceId, time, "totalActivePower", SdmpConstant.PDU_ACTIVE_POWER_TOTAL, device.getPowerRate());
        setValue(data, deviceId, time, "totalA_ActivePower", SdmpConstant.PDU_ACTIVE_POWER_A, device.getPowerRate());
        setValue(data, deviceId, time, "totalB_ActivePower", SdmpConstant.PDU_ACTIVE_POWER_B, device.getPowerRate());
        setValue(data, deviceId, time, "totalC_ActivePower", SdmpConstant.PDU_ACTIVE_POWER_C, device.getPowerRate());

        setValue(data, deviceId, time, "totalAppPower", SdmpConstant.PDU_APPARENT_POWER_TOTAL, device.getPowerRate());
        setValue(data, deviceId, time, "totalAppPowerA", SdmpConstant.PDU_APPARENT_POWER_A, device.getPowerRate());
        setValue(data, deviceId, time, "totalAppPowerB", SdmpConstant.PDU_APPARENT_POWER_B, device.getPowerRate());
        setValue(data, deviceId, time, "totalAppPowerC", SdmpConstant.PDU_APPARENT_POWER_C, device.getPowerRate());

        // deviceInfo
        setValue(data, deviceId, time, "totalActivePower", SdmpConstant.PDU_ACTIVE_POWER, device.getPowerRate());
        setValue(data, deviceId, time, "totalAppPower", SdmpConstant.PDU_APPARENT_POWER, device.getPowerRate());
    }

    /**
     * 电流电压矫正
     * @param data
     */
    private void verifyCurrentAndVoltage(Map<String, Object> data) {
        String va = data.get("voltageA").toString();
        String vb = data.get("voltageB").toString();
        String vc = data.get("voltageC").toString();
        String ca = data.get("currentA").toString();
        String cb = data.get("currentB").toString();
        String cc = data.get("currentC").toString();

        boolean flag = false;
        if (INVALID.equals(va) || Double.parseDouble(va) == 0) {
            va = vb;
            flag = true;
        }
        if (INVALID.equals(vb) || Double.parseDouble(vb) == 0) {
            vb = vc;
            flag = true;
        }
        if (INVALID.equals(vc) || Double.parseDouble(vc) == 0) {
            vc = va;
            flag = true;
        }
        if (flag) {
            data.put("voltageA", va);
            data.put("voltageB", vb);
            data.put("voltageC", vc);
        }

        flag = false;
        if (INVALID.equals(ca) || Double.parseDouble(ca) == 0) {
            ca = cb;
            flag = true;
        }
        if (INVALID.equals(cb) || Double.parseDouble(cb) == 0) {
            cb = cc;
            flag = true;
        }
        if (INVALID.equals(cc) || Double.parseDouble(cc) == 0) {
            cc = ca;
            flag = true;
        }
        if (flag) {
            data.put("currentA", ca);
            data.put("currentB", cb);
            data.put("currentC", cc);
        }
    }

    private double setValue(Map<String, Object> data, String deviceId, long time, String name, int key, Integer rate) {
        Object value = data.get(name);
        if (value != null && !INVALID.equals(value)) {
            if (rate == null) {
                rate = 1;
            }
            double v = Double.parseDouble(value.toString()) * rate;
            if (name.indexOf("Power") > 0) {
                v = v * 1000;
            }
            DeviceContext.setCurrentPointData(deviceId, key, new PointData(time, v));
            log.info(deviceId + "--> rate:" + rate + "  " + name + " : " + v);
            return v;
        } else {
            return Double.parseDouble(INVALID);
        }
    }
}
