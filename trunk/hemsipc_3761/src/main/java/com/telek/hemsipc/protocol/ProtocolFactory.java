package com.telek.hemsipc.protocol;

import com.telek.hemsipc.contant.ProtocolType;
import com.telek.hemsipc.context.HemsipcSpringContext;
import com.telek.hemsipc.context.NettyContext;
import com.telek.hemsipc.protocol.dl645.DL645Protocol;
import com.telek.hemsipc.protocol.modbus.ModbusRTUProtocol;

/**
 * @Auther: wll
 * @Date: 2018/9/11 14:15
 * @Description:
 */
public class ProtocolFactory {
    private static IProtocol dl645Protocol = HemsipcSpringContext.getBean(DL645Protocol.class);
   // private static IProtocol controlProtocol = HemsipcSpringContext.getBean(ControlProtocol.class);
    private static IProtocol modbusRTUProtocol = HemsipcSpringContext.getBean(ModbusRTUProtocol.class);

    /**
     * @Description: 根据channelId判断设备，再判断应该用哪种协议
     * @auther: wll
     * @date: 14:17 2018/9/11
     * @param: [byteBuf]
     * @return: com.telek.hemsipc.protocol.IProtocol
     */
	public static IProtocol getDecodeProtocol(String channelId) {
		String decodeProtocolCode = NettyContext.channelDeviceMapping.get(channelId);
		if (decodeProtocolCode.equals(ProtocolType.DL645.getCode())) {
			return dl645Protocol;
		}else if (decodeProtocolCode.equals(ProtocolType.MODBUS_RTU.getCode())) {
			return modbusRTUProtocol;
		}
		return null;
	}
}
