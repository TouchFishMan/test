package com.telek.hemsipc.protocol.modbus.request;

 
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.contant.DataType;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.model.ModbusWriteDataConfig;
import com.telek.hemsipc.protocol.IRequest;
import com.telek.hemsipc.protocol.modbus.response.ModbusWriteResponse;
import com.telek.hemsipc.util.ByteUtil;
import com.telek.hemsipc.util.CRC16M;

/**
 * 控制请求 
 * @Class Name：ControlRequest    
 * @Class Description：    
 * @Creater：kds    
 * @Create Time：2019年7月18日下午4:03:01    
 * @Modifier：kds    
 * @Modification Time：2019年7月18日下午4:03:01    
 * @Remarks：
 */
public class RtuControlRequest implements IRequest {
	private static final Log log = LogFactory.getLog(RtuControlRequest.class);
	
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public RtuControlRequest(ModbusWriteDataConfig config, Float value) {
        //获取device信息，获取slave地址
        Device device = DeviceContext.deviceMap.get(config.getDeviceID());
        
        baos.write(device.getSlaveMachineNum() & 0xff); //添加从机地址
        
        try {
			baos.write(HexBin.decode(config.getCommand().trim()));
		} catch (IOException e) {
			log.error(config.getDeviceID() + "写控制指令失败", e);
		} 
        
        if(config.getExistsPlaceholder() == Constant.YES) {
        	value = value / config.getMultiplier();
        	if(config.getPlaceholderType().equals(DataType.FLOAT.getCode())) { // 如果频率用浮点数表示
            	int tempValue = Float.floatToIntBits(value);
                baos.write(ByteUtil.integer2Bytes(tempValue), 0, 4);
            } else if(config.getPlaceholderType().equals(DataType.INT.getCode())){ // 如果频率用整数表示
            	baos.write(ByteUtil.integer2Bytes(new Float(value).intValue()), 4 - config.getPlaceholderLength(), config.getPlaceholderLength());   
            }
            else {
            	log.error(config.getDeviceID() + "占位符类型错误，传入类型是： " + config.getPlaceholderType());
            }
        }
        
        //写入crc校验
        byte[] crc16 = CRC16M.getCrc16(baos.toByteArray());
        baos.write(crc16, 0, 2);
        log.info("控制电器: " + device.getDeviceId() + "指令是: " + HexBin.encode(baos.toByteArray()));
    }
    
    public RtuControlRequest(ModbusWriteDataConfig config, byte[] value) {
        //获取device信息，获取slave地址
        Device device = DeviceContext.deviceMap.get(config.getDeviceID());
        
        baos.write(device.getSlaveMachineNum() & 0xff); //添加从机地址
        
        try {
        	baos.write(HexBin.decode("06")); //控制码
        	String registerAddHex = Integer.toHexString(config.getModifyBitRegisterAdd());
        	if(registerAddHex.length() == 1) {
        		registerAddHex = "000" + registerAddHex;
        	}
        	else if(registerAddHex.length() == 2){
        		registerAddHex = "00" + registerAddHex;
        	}
        	else if(registerAddHex.length() == 3) {
        		registerAddHex = "0" + registerAddHex;
        	}
        	baos.write(HexBin.decode(registerAddHex));//寄存器地址
        	baos.write(value); //值
		} catch (IOException e) {
			log.error(config.getDeviceID() + "写控制指令失败", e);
		} 
        
        //写入crc校验
        byte[] crc16 = CRC16M.getCrc16(baos.toByteArray());
        baos.write(crc16, 0, 2);
        log.info("控制电器: " + device.getDeviceId() + "指令是: " + HexBin.encode(baos.toByteArray()));
    }

    @Override
    public byte[] getMessageData() {
        return baos.toByteArray();
    }

	@Override
	public String getSyncKey() {
		return ModbusWriteResponse.class.getName();
	}
}
