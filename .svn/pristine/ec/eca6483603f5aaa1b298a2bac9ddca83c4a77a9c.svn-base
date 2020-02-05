package com.telek.hemsipc.protocol.daqm4300.request;

import java.io.ByteArrayOutputStream;

import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.protocol.IRequest;
import com.telek.hemsipc.protocol.daqm4300.DAQM4300constant;
import com.telek.hemsipc.protocol.modbus.response.ModbusReadResponse;
import com.telek.hemsipc.util.CRC16M;

public class DAQM4300Request implements IRequest {
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public DAQM4300Request(String deviceId, int slaveIndex, int status) {
        // 获取device信息，获取slave地址
        Device device = DeviceContext.deviceMap.get(deviceId);
        baos.write(device.getSlaveMachineNum() & 0xff); //添加从机地址
        
        if (status == Constant.OFF) {
            baos.write(DAQM4300constant.BOILER_CABINET_OFF[slaveIndex], 0, DAQM4300constant.BOILER_CABINET_OFF[slaveIndex].length);
        } else if (status == Constant.ON) {
            baos.write(DAQM4300constant.BOILER_CABINET_ON[slaveIndex], 0, DAQM4300constant.BOILER_CABINET_ON[slaveIndex].length);
        } else {
            throw new RuntimeException("设备" + deviceId + "控制指令错误，要控制的目标状态是： " + status);
        }
         
        // 写入crc校验
        byte[] crc16 = CRC16M.getCrc16(baos.toByteArray());
        baos.write(crc16, 0, 2);
    }

    @Override
    public byte[] getMessageData() {
        return baos.toByteArray();
    }
    
    @Override
	public String getSyncKey() {
		return ModbusReadResponse.class.getName();
	}
}
