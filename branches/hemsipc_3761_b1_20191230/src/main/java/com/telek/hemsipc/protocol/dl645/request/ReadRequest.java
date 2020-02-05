package com.telek.hemsipc.protocol.dl645.request;


import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.telek.hemsipc.protocol.dl645.DL645Constant;
import com.telek.hemsipc.util.MessageUtils;

/**
 * @Auther: wll
 * @Date: 2018/7/8 11:05
 * @Description: 读取数据
 */
public class ReadRequest extends AbstractDL645Request {
	private String dataid;
	public ReadRequest(String slave, byte[] dataid) {
		super(slave, DL645Constant.REQUEST_READ_DATA);
		assert dataid.length == 4;
		baos.write(MessageUtils.enctryData(dataid),0,4);
		baos.write(MessageUtils.calculateCS(getMessageData()));
		baos.write(0x16);
		this.dataid = HexBin.encode(dataid);
	}

	@Override
	public String getSyncKey(){
		return slave + "-" + funCode + "-" + dataid;
	}
}
