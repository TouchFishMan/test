package com.telek.hemsipc.protocol.modbus.response;

import com.telek.hemsipc.protocol.IResponse;

/**
 * @Auther: wll
 * @Date: 2018/9/17 12:59
 * @Description:
 */
public class ModbusWriteResponse implements IResponse {

	@Override
	public String getSyncKey() {
		return ModbusWriteResponse.class.getName();
	}
	
}
