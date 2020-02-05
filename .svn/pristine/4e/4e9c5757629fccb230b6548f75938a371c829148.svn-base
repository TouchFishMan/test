package com.telek.hemsipc.protocol.modbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.telek.hemsipc.context.IoSession;
import com.telek.hemsipc.protocol.IProtocol;
import com.telek.hemsipc.protocol.IResponse;
import com.telek.hemsipc.protocol.modbus.response.ModbusWriteResponse;
import com.telek.hemsipc.protocol.modbus.response.ModbusReadResponse;
import com.telek.hemsipc.util.CRC16M;

import io.netty.buffer.ByteBuf;

/**
 * @Auther: wll
 * @Date: 2018/9/11 13:20
 * @Description:
 */
@Component
public class ModbusRTUProtocol implements IProtocol {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public IResponse decoder(ByteBuf buffer, IoSession session) {
		log.info("数据长度" + buffer.readableBytes());
		// 记录读指针
		buffer.markReaderIndex();
		// 跳过头帧
		buffer.readByte();
		// 反馈类型
		byte controlType = buffer.readByte();
		switch (controlType & 0xFF) {
		case 0x03: // 读数据指令
		case 0x02:
		case 0x01: 
			log.info("查询到寄存器数据 "  );
			// 重置读指针
			buffer.resetReaderIndex();
			byte[] data = new byte[buffer.readableBytes()];
			buffer.readBytes(data);
			
			if(validateCRC(data)) {
				log.info("接收到PLC数据======================" + HexBin.encode(data));
				byte[] realData = new byte[data.length - 3]; //去掉三个字节的头
				System.arraycopy(data, 3, realData, 0, data.length - 3);
				return new ModbusReadResponse(realData);
			}else {
				log.error("读数据CRC校验错误===================== "  + HexBin.encode(data)); 
				return null;
			}
		case 0x05:   
			buffer.skipBytes(buffer.readableBytes());
			buffer.clear();
			log.info("接收到PLC控制反馈" /* + HexBin.encode(c) */);
			break;
		case 0x06:  // 控制变频器启停
		case 0x10:
			log.info("控制成功");
			buffer.skipBytes(buffer.readableBytes());
			buffer.clear();
			return new ModbusWriteResponse();
		default:
			log.info("查询或控制返回类型错误" + controlType);
			buffer.resetReaderIndex();
			byte[] b2 = new byte[buffer.readableBytes()];
			buffer.readBytes(b2);
			buffer.clear();
			break;
		}
		return null;
	}

	/**
	 * CRC校验
	 * @Author:kds 
	 * @Date：2019年7月18日上午9:09:24 
	 * @Describe:validateCRC  
	 * @throws    
	 * @param data
	 * @return
	 */
	private boolean validateCRC(byte[] data) {
		byte[] realData = new byte[data.length - 2]; 
		System.arraycopy(data, 0, realData, 0, data.length - 2);
		byte[] crc = new byte[2];
		System.arraycopy(data, data.length - 2, crc, 0, 2);
		
		return HexBin.encode(CRC16M.getCrc16(realData)).equals(HexBin.encode(crc));
	}
	
}
