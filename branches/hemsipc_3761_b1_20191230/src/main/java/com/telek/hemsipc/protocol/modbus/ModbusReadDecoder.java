package com.telek.hemsipc.protocol.modbus;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.contant.DataType;
import com.telek.hemsipc.model.CollectedData;
import com.telek.hemsipc.model.ModbusReadDataConfig;

/**
 * @Auther: wll
 * @Date: 2018/9/18 10:30
 * @Description:
 */
@Component
public class ModbusReadDecoder {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 根据配置解析查询到的数据
	 * @Author:kds 
	 * @Date：2019年7月2日下午1:52:12 
	 * @Describe:dealReadDataByConfig  
	 * @throws    
	 * @param data
	 * @param configList
	 * @return
	 */
	public CollectedData dealReadDataByConfig(byte[] realData, List<ModbusReadDataConfig> configList) {
		CollectedData converterData = new CollectedData();
		
		for(ModbusReadDataConfig config : configList) {
			int tempValue = 0;
			String meterName = config.getMeteringType();
			if(Constant.YES == config.getIsBitType()) { //如果是取某一位 
				tempValue = getNumByBitNew(realData, config.getStartPosition(), config.getBitPosition());
				if(Constant.YES == config.getNeedInvert()) {
					tempValue = tempValue == 1 ? 0:1;
				} 
			} else { //如果是数值 
				int startPosition = config.getStartPosition();
				int meteringDataLength = config.getMeteringDataLength();
				
				if(meteringDataLength == 4) {
					//if(Constant.YES == config.getNeedInvert()) {
					//	tempValue =byte4ToIntInvert(realData, startPosition);
					//} else {
						tempValue =byte4ToInt(realData, startPosition);
					//}
					
				}
				else if(meteringDataLength == 2) {
					//if(Constant.YES == config.getNeedInvert()) {
					//	tempValue =byte2ToIntInvert(realData, startPosition);
					//} else {
						tempValue =byte2ToInt(realData, startPosition);
					//} 
				}
				
				if(config.getDataType().equals(DataType.FLOAT.getCode())) { //处理数据类型
					tempValue = (int) Float.intBitsToFloat(tempValue);
				}
				
				tempValue = (int)(tempValue * config.getMultiplier()); //处理乘数
			}
			log.info(config.getDeviceID() + " ===============" + meterName + ":" + tempValue);
			converterData.setValue(meterName, tempValue);
		}
		
		return converterData;
	}
	
	private int byteToInt(byte[] array, int pos) {
		return (((array[numTranslate1(pos)] << 8) & 0xff00) + (array[numTranslate2(pos)] & 0x00ff));
	}
	
	private int byte2ToInt(byte[] array, int pos) {
		return (((array[pos * 2] << 8) & 0xff00) + (array[pos * 2 + 1] & 0x00ff));
	}

//	private int byte2ToIntInvert(byte[] array, int pos) {
//		byte[] intByte = new byte[2]; 
//		System.arraycopy(array, pos * 2, intByte, 0, 2); 
//		byte[] intByteInvert = MessageUtils.reverse(intByte);
//		
//		return (((intByteInvert[0] << 8) & 0xff00) + (intByteInvert[1] & 0x00ff));
//	}
	
	
	public static int byte4ToInt(byte[] arr, int pos) {
		return (int) (((arr[pos * 2] & 0xff) << 24) | ((arr[pos * 2 + 1] & 0xff) << 16) | ((arr[pos * 2 + 2] & 0xff) << 8)
				| ((arr[pos * 2 + 3] & 0xff)));
	}

//	public static int byte4ToIntInvert(byte[] arr, int pos) {
//		byte[] intByte = new byte[4]; 
//		System.arraycopy(arr, pos * 2, intByte, 0, 4); 
//		byte[] intByteInvert = MessageUtils.reverse(intByte);
//		
//		return (int) (((intByteInvert[0] & 0xff) << 24) | ((intByteInvert[1] & 0xff) << 16) | ((intByteInvert[2] & 0xff) << 8)
//				| ((intByteInvert[3] & 0xff)));
//	}
	
	
	private int numTranslate1(int i) {
		return 2 * i + 1;
	}

	private int numTranslate2(int i) {
		return 2 * i + 2;
	}

	public int getNumByBit(byte[] arry, int pos, int bit) {
		return (byteToInt(arry, pos) & (1 << bit)) == 0 ? 0 : 1;
	}
	
	public int getNumByBitNew(byte[] arry, int pos, int bit) {
		return (byte2ToInt(arry, pos) & (1 << bit)) == 0 ? 0 : 1;
	}
	
//	public int getNumByBitNew(byte[] arry, int pos, int bit) {
//		return ((arry[pos] & 0x00ff) & (1 << bit)) == 0 ? 0 : 1;
//	}

}
