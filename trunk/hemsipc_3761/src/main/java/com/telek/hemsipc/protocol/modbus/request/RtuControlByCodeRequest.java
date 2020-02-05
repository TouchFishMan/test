package com.telek.hemsipc.protocol.modbus.request;


import java.io.ByteArrayOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.context.SysContext;
import com.telek.hemsipc.model.ControlConfig;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.protocol.IRequest;
import com.telek.hemsipc.protocol.modbus.response.ModbusWriteResponse;
import com.telek.hemsipc.util.ByteUtil;
import com.telek.hemsipc.util.CRC16M;
import com.telek.hemsipc.util.StringUtil;

/**
 * 通过写设备编号和控制编号进行控制请求 
 * @Class Name：ControlByCodeRequest    
 * @Class Description：    
 * @Creater：kds    
 * @Create Time：2019年10月18日上午11:24:15    
 * @Modifier：kds    
 * @Modification Time：2019年10月18日上午11:24:15    
 * @Remarks：
 */
public class RtuControlByCodeRequest implements IRequest {
	private static final Log log = LogFactory.getLog(RtuControlByCodeRequest.class);
	
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    //目前只支持modbusRTU协议，不支持modbusTCP
    public RtuControlByCodeRequest(ControlConfig config, String controlType, Float value) {
        //获取device信息，获取slave地址
        Device device = DeviceContext.deviceMap.get(config.getDeviceID());
        
        try {
        	baos.write(device.getSlaveMachineNum() & 0xff); //添加从机地址
			baos.write((byte)0X10);//指令类型
			baos.write(ByteUtil.integer2Bytes(StringUtil.parseInt(SysContext.getSysConfig(Constant.CONTROL_REGISTER_START_ADDRESS).getConfigValue())), 2, 2); //从配置表中读取寄存器开始地址
			baos.write(ByteUtil.integer2Bytes(5), 2, 2); // 几个寄存器   
			baos.write(ByteUtil.integer2Bytes(10), 3, 1); // 几字节数据
			
			baos.write(ByteUtil.integer2Bytes(config.getRegisterNum()), 2, 2); //第一个寄存器放 设备编号
			baos.write(ByteUtil.integer2Bytes(config.getControlCode()), 2, 2); //第二个放 功能码
			baos.write(ByteUtil.integer2Bytes(0), 2, 2); //第三个寄存器放 持续时间
			
			if(Constant.YES == config.getExistsPlaceholder()) {//第四五寄存器个放 值（比如：频率）
	            baos.write(ByteUtil.integer2Bytes(Float.floatToIntBits(value * config.getMultiplier())), 0, 4);
			}else {
				baos.write(ByteUtil.integer2Bytes(0), 0, 4);
			}
		} catch (Exception e) {
			log.error(config.getDeviceID() + "写控制指令失败", e);
		} 
        	 
        //写入crc校验
        byte[] crc16 = CRC16M.getCrc16(baos.toByteArray());
        baos.write(crc16, 0, 2);
        log.info("控制电器: " + device.getDeviceId() + " 指令是: " + HexBin.encode(baos.toByteArray()));
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
