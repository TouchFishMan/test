package com.telek.hemsipc.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.model.ModbusWriteDataConfig;
import com.telek.hemsipc.netty.init.SerialPoint;
import com.telek.hemsipc.protocol.daqm4300.request.DAQM4300Request;
import com.telek.hemsipc.protocol.modbus.request.RtuControlRequest;
import com.telek.hemsipc.protocol.modbus.request.RtuReadRequest;
import com.telek.hemsipc.protocol.modbus.response.ModbusReadResponse;
import com.telek.hemsipc.service.IControlService;

/**
 * 电器控制类（这里没有实现根据不同电器类型和不同厂家组装指令，后续需改进）
 * 
 * @Class Name：ControlServiceImpl
 * @Class Description：
 * @Creater：telek
 * @Create Time：2019年5月8日上午10:37:09
 * @Modifier：telek
 * @Modification Time：2019年5月8日上午10:37:09 @Remarks：
 */
@Component("controlServiceByWriteRegister")
public class ControlServiceByWriteRegister implements IControlService {
    private static final Log log = LogFactory.getLog(ControlServiceByWriteRegister.class);
    // @Autowired
    private SerialPoint serialPoint;

    @Override
    public void controlBoiler(String deviceID, int status) {
        try {
            log.info("开始控制锅炉 " + deviceID + " 开关： " + status);
            for (int i = 0; i < 8; i++) {
                DAQM4300Request request = new DAQM4300Request(deviceID, i, status);
                serialPoint.sendMsgByClientChannel(DeviceContext.deviceMap.get(deviceID).getChannelId(), request);
                Thread.sleep(3000);
            }
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
 
    private synchronized void  doControl(ModbusWriteDataConfig config, Float value) {
    	String deviceID = config.getDeviceID();
		try {
            log.info("开始控制电器 " + deviceID);
            
            if(config.getIsOnlyModifyBit() == Constant.YES) {
            	controlByBit(config);
            }
            else {
            	RtuControlRequest request = new RtuControlRequest(config, value);
                serialPoint.sendMsgByClientChannel(DeviceContext.deviceMap.get(deviceID).getChannelId(), request);
            }
            log.info("控制电器 " + deviceID + " 指令发送成功");
        } catch (Exception e) {
            log.error("", e);
        }
    	 
    }
    
     
    private void controlByBit(ModbusWriteDataConfig config) {
    	String deviceID = config.getDeviceID();
    	Device device = DeviceContext.deviceMap.get(deviceID);
    	
    	RtuReadRequest request = new RtuReadRequest(device.getSlaveMachineNum(), config.getModifyBitRegisterAdd(), 1);
    	ModbusReadResponse readResponse = (ModbusReadResponse) serialPoint.syncSendMsgByClientChannel(device.getChannelId(), request);
    	if(readResponse!= null) {
    		byte[] value = new byte[2];
    		System.arraycopy(readResponse.getResult(), 3, value, 0, 2);
    		
    		log.info(deviceID + "控制前读取得到的数据为" + HexBin.encode(value));
            int bitPosition = config.getModifyBitPosition();
            if (config.getNewBitValue() == 1) {
            	value[Math.abs(bitPosition / 8 - 1)] = (byte) (value[Math.abs(bitPosition / 8 - 1)] | (0x01 << (bitPosition > 7 ? bitPosition - 8 : bitPosition)));
            } else if (config.getNewBitValue() == 0) {
            	value[Math.abs(bitPosition / 8 - 1)] = (byte) (value[Math.abs(bitPosition / 8 - 1)] & ~(0x01 << (bitPosition > 7 ? bitPosition - 8 : bitPosition)));
            }
            else {
            	log.error("设备" + deviceID +  "  错误的写数据配置 " + config.getNewBitValue());
            }
            
            RtuControlRequest writeRequest = new RtuControlRequest(config, value);
             
            serialPoint.sendMsgByClientChannel(DeviceContext.deviceMap.get(deviceID).getChannelId(), writeRequest);
    	}else {
    		log.error("读取不到设备" + deviceID +  " 的数据，不能进行控制 ");
    	}
    }
 

    
    /**
     * 根据配置发送控制指令，对于没有占位符的指令，参数value无用
     * @Author:kds 
     * @Date：2019年7月18日下午4:44:58 
     * @Describe:controlOnOffByConfig  
     * @throws    
     * @param controlType
     * @param device
     * @param value
     */
    public void control(String controlType, Device device, Float value) {
    	log.info("控制设备" + device.getDeviceId() + " controlType is: " + controlType + " value is: "  + value);
    	// TODO 要去掉
//    	SimulationData tempSimulationData = DeviceContext.simulationDataMap.get(device.getDeviceId());
//    	if(tempSimulationData != null) {
//    		if(controlType.equals(ControlType.ON.getCode())) {
//    			tempSimulationData.setState(1);
//        	}else if(controlType.equals(ControlType.OFF.getCode())) {
//        		tempSimulationData.setState(0);
//        	}
//        	if(controlType.equals(ControlType.REMOTE_CONTROL.getCode()) || controlType.equals(ControlType.LOCAL_CONTROL.getCode())) {
//        		tempSimulationData.setIsRemoteControl(value.intValue());
//        	} 
//        	
//        	if(controlType.equals(ControlType.COLD.getCode())) {
//        		tempSimulationData.setModel(Constant.MODEL_COLD);
//        	}else if(controlType.equals(ControlType.HOT.getCode())) {
//        		tempSimulationData.setState(Constant.MODEL_HOT);
//        	}
//        	if(controlType.equals(ControlType.OPEN_VALUE.getCode())) {
//        		tempSimulationData.setOpenValue(value.intValue());
//        	}
//    	} 
    	// TODO 要去掉结束
    	
    	List<ModbusWriteDataConfig> configList = DeviceContext.getCommandConfigList(device.getDeviceId(), controlType);
    	if(configList != null) {
    		Collections.sort(configList);
    		for(ModbusWriteDataConfig config : configList) {
    			if(value == null && config.getExistsPlaceholder() == Constant.YES) {
    				sendCommand(config, config.getDefaultValue());
    			}
    			else {
    				sendCommand(config, value);
    			}
    		}
    	}
    }
    
    private void sendCommand(ModbusWriteDataConfig config, Float value) {
    	if(config.getDelayTime() > 0) {
			try {
				Thread.sleep(config.getDelayTime() * 1000);
			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
			}
		}
    	doControl(config, value);
    }
    
}
