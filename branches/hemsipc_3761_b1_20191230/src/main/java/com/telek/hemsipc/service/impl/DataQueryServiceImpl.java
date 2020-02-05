package com.telek.hemsipc.service.impl;

import java.util.Iterator;

import org.springframework.stereotype.Service;

import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.model.CurrentDeviceData;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.model.PointData;
import com.telek.hemsipc.sdmp.SdmpConstant;
import com.telek.hemsipc.service.IDataQueryService;
import com.telek.hemsipc.util.CommonUtil;
import com.telek.hemsipc.util.StringUtil;

/**
 * 数据查询实现类
 * @Class Name：DataQueryServiceImpl    
 * @Class Description：    
 * @Creater：telek    
 * @Create Time：2019年5月7日下午4:28:19    
 * @Modifier：telek    
 * @Modification Time：2019年5月7日下午4:28:19    
 * @Remarks：
 */
@Service("dataQueryService")
public class DataQueryServiceImpl implements IDataQueryService{

	@Override
	public float queryCurrentSumPower() {
		float power = 0;
		Iterator<Device> deviceIt = DeviceContext.deviceMap.values().iterator();
		while(deviceIt.hasNext()) {
			Device device = deviceIt.next();
			CurrentDeviceData currentDeviceData = DeviceContext.getCurrentDeviceDataByDeviceID(device.getDeviceId());
			if(currentDeviceData != null) {
				PointData pointData = currentDeviceData.getValue(SdmpConstant.PDU_ACTIVE_POWER);
				if(pointData != null && CommonUtil.validateData(pointData.getTime())) {// 如果该采集点数据有效（当前时间-采集时间 < 设置的离线时间）
					power += pointData.getFloatValue();
				}
			}
		} 
		return power;
	}

	@Override
	public Float queryCurrentPowerByDevice(String deviceID) {
		CurrentDeviceData currentDeviceData = DeviceContext.getCurrentDeviceDataByDeviceID(deviceID);
		if(currentDeviceData != null) {
			PointData pointData = currentDeviceData.getValue(SdmpConstant.PDU_ACTIVE_POWER);
			if(pointData != null && CommonUtil.validateData(pointData.getTime())) {// 如果该采集点数据有效（当前时间-采集时间 < 设置的离线时间）
				return new Float(pointData.getFloatValue());
			}
		}
		return null;
	}

	@Override
	public Integer queryCurrentFrequenceByDevice(String deviceID) {
		CurrentDeviceData currentDeviceData = DeviceContext.getCurrentDeviceDataByDeviceID(deviceID);
		if(currentDeviceData != null) {
			PointData pointData = currentDeviceData.getValue(SdmpConstant.PDU_FREQUENC);
			if(pointData != null && CommonUtil.validateData(pointData.getTime())) {// 如果该采集点数据有效（当前时间-采集时间 < 设置的离线时间）
				return new Integer((int) StringUtil.parseFloat(pointData.getValue().toString()));
			}
		}
		return null;
	}

	@Override
	public Integer queryCurrentOnOffStatusByDevice(String deviceID) {
		CurrentDeviceData currentDeviceData = DeviceContext.getCurrentDeviceDataByDeviceID(deviceID);
		if(currentDeviceData != null) {
			PointData pointData = currentDeviceData.getValue(SdmpConstant.PDU_C_STATE);
			if(pointData != null && CommonUtil.validateData(pointData.getTime())) {// 如果该采集点数据有效（当前时间-采集时间 < 设置的离线时间）
				return new Integer((int)pointData.getValue());
			}
		}
		return null;
	}
	
}
  
