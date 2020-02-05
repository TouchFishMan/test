package com.telek.hemsipc.protocol.modbus.request;

import java.io.ByteArrayOutputStream;

import com.telek.hemsipc.protocol.IRequest;
import com.telek.hemsipc.protocol.modbus.response.ModbusReadResponse;
import com.telek.hemsipc.util.ByteUtil;
import com.telek.hemsipc.util.CRC16M;

/**
 * @Auther: wll
 * @Date: 2018/9/17 11:16
 * @Description:
 */
public class RtuReadRequest implements IRequest {
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

 
    public RtuReadRequest(int slaveMachineNum, int registerStartAddress, int registersNum) {
    	baos.write(slaveMachineNum & 0xff); //添加从机地址
        baos.write((byte) 0x03); //添加 读指令
        baos.write(ByteUtil.integer2Bytes(registerStartAddress), 2, 2);//添加 开始寄存器地址
        baos.write(ByteUtil.integer2Bytes(registersNum), 2, 2);//添加 寄存器个数
   
        //写入crc校验
        byte[] crc16 = CRC16M.getCrc16(baos.toByteArray()); //添加校验
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
