package com.telek.hemsipc.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.contant.ProtocolType;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.model.CollectedData;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.model.ModbusReadDataConfig;
import com.telek.hemsipc.model.PointData;
import com.telek.hemsipc.netty.init.SerialPoint;
import com.telek.hemsipc.protocol.modbus.ModbusReadDecoder;
import com.telek.hemsipc.protocol.modbus.request.RtuReadRequest;
import com.telek.hemsipc.protocol.modbus.response.ModbusReadResponse;
import com.telek.hemsipc.sdmp.SdmpConstant;

/**
 * @Auther: wll
 * @Date: 2018/9/17 08:55
 * @Description:
 */
// @Component(value= "modbusQuartzManager")
public class ModbusQuartzManager {
    private Logger log = LoggerFactory.getLogger(this.getClass());
 
    // @Autowired
    private SerialPoint serialPoint;
    @Autowired
    private ModbusReadDecoder readDecoder;

    /**
     * @Description: PLC每分钟采集数据
     * @auther: wll
     * @date: 9:56 2018/9/13
     * @param: []
     * @return: void
     */
    @Scheduled(cron = "12 * * * * ?")
    public void collectioData() {
        List<Device> coolingWaterPumps = DeviceContext.getDeviceByProtocol(ProtocolType.MODBUS_RTU.getCode());
        for (Device device : coolingWaterPumps) {
            try {
            	collectioDataInner(device);
            } catch (Exception e) {
                log.error(device.getDeviceId() + "心跳采集/发送异常", e);
            }
        }
    }

    /**
     * 保存采集到的数据
     * @Author:kds 
     * @Date：2019年7月2日下午1:48:06 
     * @Describe:saveCollectedData  
     * @throws    
     * @param device
     * @param data
     */
	private void saveCollectedData(Device device, CollectedData data) {
		long time = System.currentTimeMillis();
               
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_APPARENT_POWER, time, data.getPower());
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_U, time, data.getVoltage());
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_I, time, data.getElectric());
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_FREQUENC, time, data.getFrequency());
		
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_WORKING_FAULT_STATE, time, data.getWorkingFaultState());
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_IS_REMOTE_CONTROL, time, data.getIsRemoteControl());
		
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_TEMPERATURE, time, data.getTemperature());
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_HUMIDITY, time, data.getHumidity());
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_OPEN_VALUE, time, data.getOpenValue());
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_FAULT_STATE_CODE, time, data.getFaultStateCode());
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_COOLING_WATER_INPUT_TEMPERATURE, time, data.getCoolingWaterInputTemperature());
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_COOLING_WATER_OUTPUT_TEMPERATURE, time, data.getCoolingWaterOutputTemperature());
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_CHILLED_WATER_INPUT_TEMPERATURE, time, data.getChilledWaterInputTemperature());
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_CHILLED_WATER_OUTPUT_TEMPERATURE, time, data.getChilledWaterOutputTemperature());
		
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_EVAPORATION_PRESSURE, time, data.getEvaporationPressure());
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_CONDENSE_PRESSURE, time, data.getCondensePressure());
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_ELECTRIC_PERCENTAGE, time, data.getElectricPercentage());
		saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_IS_AUTO_CONTROL, time, data.getIsAutoControl());
		
		if(data.getState() != null || data.getSecondOnOffState() != null || data.getThirdOnOffState() != null) {
			int state  = data.getState() != null && data.getState() == Constant.ON 
					|| data.getSecondOnOffState() != null && data.getSecondOnOffState() == Constant.ON 
					|| data.getThirdOnOffState() != null && data.getThirdOnOffState() == Constant.ON
					? 1:0;
			saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_C_STATE, time, state);
			saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_STATE, time, data.getState());
		}
          
		if(data.getApplianceFaultState() != null || data.getSecondApplianceFaultState() != null || data.getThirdApplianceFaultState() != null) {
			int appliandeFaultState  = data.getApplianceFaultState() != null && data.getApplianceFaultState() == Constant.IS_ERROR 
					|| data.getSecondApplianceFaultState() != null && data.getSecondApplianceFaultState() == Constant.IS_ERROR 
					|| data.getThirdApplianceFaultState() != null && data.getThirdApplianceFaultState() == Constant.IS_ERROR
					? 1:0;
			saveToDeviceContext(device.getDeviceId(), SdmpConstant.PDU_APPLIANCE_FAULT_STATE, time, appliandeFaultState);
		}
		
	}
    
    public void saveToDeviceContext(String deviceID, int sdmpKey, long time, Integer value) {
    	if(value != null) {
    		DeviceContext.setCurrentPointData(deviceID, sdmpKey, new PointData(time, value)); 
    	}
    }
    
    public void saveToDeviceContext(String deviceID, int sdmpKey, long time, Float value) {
    	if(value != null) {
    		DeviceContext.setCurrentPointData(deviceID, sdmpKey, new PointData(time, value)); 
    	}
    }
    
	public void collectioDataInner(Device device) {
		try {
			List<ModbusReadDataConfig> configList = DeviceContext.modbusReadDataConfigMap.get(device.getDeviceId());
			if(configList == null || configList.size() == 0) {
				return;
			}
			
			//根据寄存器开始地址分组
			Map<Integer, List<ModbusReadDataConfig>> groupsMap = grouping(configList);
			Iterator<Integer> keyIt = groupsMap.keySet().iterator();
			while(keyIt.hasNext()) {
				int startAdd = keyIt.next();
				try {
					List<ModbusReadDataConfig> innerList = groupsMap.get(startAdd);
					
					int maxRegisterNum = 0;
					for(ModbusReadDataConfig innerConfig : innerList) {
						if(innerConfig.getRegisterNum() > maxRegisterNum) {
							maxRegisterNum = innerConfig.getRegisterNum();
						}
					}
					log.info("查询寄存器数据"+ device.getDeviceId() + " startAdd:" +  startAdd + " maxRegisterNum: " + maxRegisterNum);
					//查询
					RtuReadRequest request = new RtuReadRequest(device.getSlaveMachineNum(), startAdd, maxRegisterNum);
					ModbusReadResponse response = (ModbusReadResponse) serialPoint.syncSendMsgByClientChannel(device.getChannelId(), request);
					if (response != null) {
						CollectedData data = readDecoder.dealReadDataByConfig(response.getResult(), innerList);
						saveCollectedData(device, data);
					}
				}catch (Exception e) {
					log.error(device.getDeviceId() + "地址" + startAdd + "采集数据错误", e);
				}
				Thread.sleep(50);
			}
 
		} catch (Exception e) {
			log.error(device.getDeviceId() + "心跳采集/发送异常", e);
		}

	}

	
	private Map<Integer, List<ModbusReadDataConfig>> grouping(List<ModbusReadDataConfig> configList) {
		Map<Integer, List<ModbusReadDataConfig>> map = new HashMap<Integer, List<ModbusReadDataConfig>>();
		for(ModbusReadDataConfig config : configList) {
			//判断map中有没有这个地址的配置参数,如果没有就把配置参数放到list并存入map
			List<ModbusReadDataConfig> list = map.get(config.getRegisterStartAddress());
			if(list == null) {
				list = new ArrayList<ModbusReadDataConfig>();
				map.put(config.getRegisterStartAddress(), list);
			}
			list.add(config);
		}
		return map;
	}
    
}
