package com.telek.hemsipc.protocol.dl645.request;

import com.telek.hemsipc.protocol.IRequest;

public class TempRequest  implements IRequest{

	byte[] aaa = { /* (byte) 0xFE,(byte) 0xFE,(byte) 0xFE, */(byte) 0x68,
			(byte) 0xAA,(byte) 0xAA,(byte) 0xAA,
			(byte) 0xAA,(byte) 0xAA,(byte) 0xAA,(byte) 0x68,
			(byte) 0x13,(byte) 0x00,(byte) 0xDF,(byte) 0x16};
	
	@Override
	public byte[] getMessageData() {
		return aaa;
	}

	@Override
	public String getSyncKey() {
		return null;
	}

}
  
