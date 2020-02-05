package com.telek.hemsipc.protocol.modbus.response;

import com.telek.hemsipc.protocol.IResponse;

/**
 * @Auther: wll
 * @Date: 2018/9/17 11:17
 * @Description:
 */
public class ModbusReadResponse implements IResponse {
    private byte[] result;

    public byte[] getResult() {
        return result;
    }

    public ModbusReadResponse(byte[] result) {
        this.result = result;
    }

	@Override
	public String getSyncKey() {
		return ModbusReadResponse.class.getName();
	}
}
