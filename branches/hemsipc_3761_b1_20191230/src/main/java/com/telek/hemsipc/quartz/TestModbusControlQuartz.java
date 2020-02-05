package com.telek.hemsipc.quartz;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.telek.hemsipc.contant.ControlType;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.netty.init.SerialPoint;
import com.telek.hemsipc.protocol.IRequest;
import com.telek.hemsipc.protocol.dl645.ReadDataDecode;
import com.telek.hemsipc.protocol.dl645.response.ReadResponse;
import com.telek.hemsipc.repository.ICommonDAO;
import com.telek.hemsipc.service.ServiceFactory;
import com.telek.hemsipc.util.CRC16M;

/**
 * @Auther: wll
 * @Date: 2018/9/17 08:55
 * @Description:
 */
  //@Component
public class TestModbusControlQuartz {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private SerialPoint serialPoint;

    @Autowired
    private ReadDataDecode readDataDecode;

    @Autowired
    private ICommonDAO commonDAO;
    
    @Scheduled(cron = "13 * * * * ?")
    public void collectioData() {
    	String deviceID = "TC19071700000039";
    	float powerPercent = 39;
        Device device = DeviceContext.deviceMap.get(deviceID);
        try {
        	//controlService.controlBoiler(deviceID, 1);
        	//controlService.controlCenterAirSystemLocalRemoteControlConvert(deviceID, 0);
            // controlService.controlByConfig(ControlType.FREQUENCY.getCode(), device, 49F);
        	ServiceFactory.getIControlService(device.getControlType()).control(ControlType.TEMPERATURE.getName(), device, powerPercent);
        	
        	//TempRequest request = new TempRequest();
			//serialPoint.sendMsgByClientChannel(DeviceContext.deviceMap.get(deviceID).getChannelId(), request);
        } catch (Exception e) {
            log.error("控制" + deviceID + "异常", e);
        }
 
    }
    
    //@Scheduled(cron = "1/23 * * * * ?")
//    public void Test645() {
//		ReadResponse readResponse = (ReadResponse) serialPoint.syncSendMsgByClientChannel("192.168.0.233:8001", new TempRequest());
//        byte[] data = readResponse.getDatas();
//		System.out.println(Hex.encodeHex(data));
//    }
    
//	public static class TempRequest implements IRequest {
//
//		@Override
//		public byte[] getMessageData() {
//			byte[] data = new byte[] {(byte) 0X0C, (byte) 0X10, (byte) 0X00, (byte) 0X32, (byte) 0X00, (byte) 0X05, (byte) 0X10,
//					(byte) 0X00, (byte) 0X99,  //编号
//					(byte) 0X00, (byte) 0X78,  //功能码
//					
//					(byte) 0X00, (byte) 0X30, 
//					(byte) 0X00, (byte) 0X00, (byte) 0X00, (byte) 0X00};
//
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			
//			try {
//				baos.write(data);
//			} catch (IOException e) {
//			}
//			// 写入crc校验
//			byte[] crc16 = CRC16M.getCrc16(baos.toByteArray());
//			baos.write(crc16, 0, 2);
//
//			return baos.toByteArray();
//		}
//
//		@Override
//		public String getSyncKey() {
//			return null;
//		}
//
//	}

//    public static void main(String[] args) {
//    	byte[] ttt = { 
//    	(byte)2, (byte)3, (byte)32, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, 
//    	(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)1, (byte)-64, (byte)7, 
//    	(byte)-127, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, 
//    	(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0};
//    	byte[] crc16 = CRC16M.getCrc16(ttt);
//    	System.out.println(crc16);
//    }
    
}
