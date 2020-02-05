package com.telek.hemsipc.quartz;

import java.util.Collection;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.contant.DeviceType;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.model.PointData;
import com.telek.hemsipc.model.SimulationData;
import com.telek.hemsipc.repository.ICommonDAO;
import com.telek.hemsipc.sdmp.SdmpConstant;

 
/**
 * 定时生成模拟数据
 * @Class Name：SimulationDataQuartz    
 * @Class Description：    
 * @Creater：kds    
 * @Create Time：2019年7月18日上午9:33:39    
 * @Modifier：kds    
 * @Modification Time：2019年7月18日上午9:33:39    
 * @Remarks：
 */
// @Component
public class SimulationDataQuartz {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ICommonDAO commonDAO;
    
   /**
    * 定时生层模拟数据
    * @Author:kds 
    * @Date：2019年7月18日上午10:55:17 
    * @Describe:createData  
    * @throws
    */
    @Scheduled(cron = "50 * * * * ?")
    public void createData() {
        try {
            doCreateData();
        } catch (Exception e) {
            log.error("发送给云端心跳异常", e);
        }
    }
 

    private void doCreateData() throws Exception {
    	Collection<SimulationData> simulationDataS = DeviceContext.simulationDataMap.values();
        if(simulationDataS == null || simulationDataS.size() == 0) {
            return;
        }
        
        for (SimulationData simulationData : simulationDataS) {
        	String deviceID = simulationData.getDeviceID();
          
            Device device = DeviceContext.deviceMap.get(deviceID);
            // 温度传感器温度
            if(device!= null && device.getType() == DeviceType.DEVICE_TYPE_TEMPERATURE_SENSOR.getCode()) {
				DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_TEMPERATURE, new PointData(
						System.currentTimeMillis(), simulationData.getTemperature()/* + new Random().nextInt(2) */));
            } else {
            	PointData onOffStateData = DeviceContext.getCurrentPointData(deviceID, SdmpConstant.PDU_C_STATE);
                //  if(onOffStateData != null) { // TODO 正式运行要去掉注释
                  	float currentPower = 0;
                  	float currentPowerA = 0;
                  	float currentPowerB = 0;
                  	float currentPowerC = 0;
                  	
                  	float currentVoltage = simulationData.getVoltage() - new Random().nextInt(20);
                  	float currentVoltageA = simulationData.getVoltage() - new Random().nextInt(20);
                  	float currentVoltageB = simulationData.getVoltage() - new Random().nextInt(20);
                  	float currentVoltageC = simulationData.getVoltage() - new Random().nextInt(20);
                  	
                  	float sumElec = simulationData.getSumElec();
                  	PointData powerData = new PointData(System.currentTimeMillis(), currentPower);
                  	PointData powerDataA = new PointData(System.currentTimeMillis(), currentPowerA);
                  	PointData powerDataB = new PointData(System.currentTimeMillis(), currentPowerB);
                  	PointData powerDataC = new PointData(System.currentTimeMillis(), currentPowerC);
                  	
                  	PointData IData = new PointData(System.currentTimeMillis(), 0);
                  	PointData IDataA = new PointData(System.currentTimeMillis(), 0);
                  	PointData IDataB = new PointData(System.currentTimeMillis(), 0);
                  	PointData IDataC = new PointData(System.currentTimeMillis(), 0);
              		
                  	PointData elecData = new PointData(System.currentTimeMillis(), sumElec);
                  	
                  	int state = Constant.OFF;
                  //	if(device.getType() == DeviceType.DEVICE_TYPE_AIR_CONDITIONER_TERMINAL.getCode()) { //空调末端用真实状态，其他用模拟状态
                  		if(onOffStateData != null) {
                  			state = new Float(onOffStateData.getFloatValue()).intValue();
                  		}
                  	//}
                  	//else {
                  	//	state = simulationData.getState();
                  	//}
                  	
              		if(state == Constant.ON) {
                  	//if(onOffStateData.getFloatValue() == Constant.ON) { // TODO 正式运行要去掉注释
                  		currentPower = simulationData.getPower() - new Random().nextInt(simulationData.getPower() / 33); //随机波动生成当前功率
                  		currentPowerA = simulationData.getPower() / 3 - new Random().nextInt(simulationData.getPower() / 3 / 33);  
                  		currentPowerB = simulationData.getPower() / 3 - new Random().nextInt(simulationData.getPower() / 3 / 33);  
                  		currentPowerC = simulationData.getPower() / 3 - new Random().nextInt(simulationData.getPower() / 3 / 33);  
                  		
                  		float detaElec = currentPower / 3600 /1000 * 60; //根据功率计算一分钟内电量
                  		sumElec = sumElec + detaElec;
                  		powerData.setValue(currentPower);
                  		powerDataA.setValue(currentPowerA);
                  		powerDataB.setValue(currentPowerB);
                  		powerDataC.setValue(currentPowerC);
                  		
                  		IData.setValue(currentPower / currentVoltage);
                  		IDataA.setValue(currentPowerA / currentVoltageA);
                  		IDataB.setValue(currentPowerB / currentVoltageB);
                  		IDataC.setValue(currentPowerC / currentVoltageC);
                  		
                  		elecData.setValue(sumElec);
                  		
                  		simulationData.setSumElec(sumElec);
                  		commonDAO.saveOrUpdate(simulationData);
                  	//} 
              		}
                  	// 保存对象
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_ACTIVE_POWER, powerData);
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_ACTIVE_POWER_A, powerDataA);
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_ACTIVE_POWER_B, powerDataB);
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_ACTIVE_POWER_C, powerDataC);
                  	
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_U, new PointData(System.currentTimeMillis(), currentVoltage));
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_U_A, new PointData(System.currentTimeMillis(), currentVoltageA));
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_U_B, new PointData(System.currentTimeMillis(), currentVoltageB));
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_U_C, new PointData(System.currentTimeMillis(), currentVoltageC));
                  	
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_I, new PointData(System.currentTimeMillis(), IData.getValue()));
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_I_A, new PointData(System.currentTimeMillis(), IDataA.getValue()));
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_I_B, new PointData(System.currentTimeMillis(), IDataB.getValue()));
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_I_C, new PointData(System.currentTimeMillis(), IDataC.getValue()));
                  	
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_ELEC, elecData);
                  	
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_ELEC_POSITIVE_REACTIVE_POWER, new PointData(System.currentTimeMillis(), 0));
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_ELEC_NEGATIVE_REACTIVE_POWER, new PointData(System.currentTimeMillis(), 0));
                  	
                  	//if(device.getType() != DeviceType.DEVICE_TYPE_AIR_CONDITIONER_TERMINAL.getCode()) { //空调末端用真实状态，其他用模拟状态
                  	//	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_C_STATE, new PointData(System.currentTimeMillis(), simulationData.getState()));
                  	//}
                  	
                  	DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_IS_REMOTE_CONTROL, new PointData(System.currentTimeMillis(), simulationData.getIsRemoteControl()));
                  	
                  }
          //  }
            
            //末端空调专用
//            if(device!= null && device.getType() == DeviceType.DEVICE_TYPE_AIR_CONDITIONER_TERMINAL.getCode()) {
//				DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_TEMPERATURE, new PointData(System.currentTimeMillis(), 27));
//				DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_OPEN_VALUE, new PointData(System.currentTimeMillis(), 100)); // TODO
//				DeviceContext.setCurrentPointData(deviceID, SdmpConstant.PDU_AIR_TERMINAL_COLD_HOT_MODEL, new PointData(System.currentTimeMillis(), simulationData.getModel()));
//            }
            
        }  // for 循环结束
        
    }

    
}

