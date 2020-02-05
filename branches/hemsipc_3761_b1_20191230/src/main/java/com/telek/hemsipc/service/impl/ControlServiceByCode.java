package com.telek.hemsipc.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.context.DeviceContext;
import com.telek.hemsipc.model.ControlConfig;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.netty.init.SerialPoint;
import com.telek.hemsipc.protocol.modbus.request.RtuControlByCodeRequest;
import com.telek.hemsipc.service.IControlService;

/**
 * 新版电器控制实现类
 * @Class Name：ControlServiceNew    
 * @Class Description：    
 * @Creater：kds    
 * @Create Time：2019年10月18日上午10:07:17    
 * @Modifier：kds    
 * @Modification Time：2019年10月18日上午10:07:17    
 * @Remarks：
 */
@Component("controlServiceByCode")
public class ControlServiceByCode implements IControlService {
	private static final Log log = LogFactory.getLog(ControlServiceByCode.class);
	 
	// @Autowired
	private SerialPoint serialPoint;
	 
	@Override
	public void controlBoiler(String deviceID, int status) {
		log.error("不支持锅炉继电器控制");
	}

	@Override
	public void control(String controlType, Device device, Float value) {
		log.info("控制设备" + device.getDeviceId() + " controlType is: " + controlType + " value is: "  + value);
		String deviceID = device.getDeviceId();
		try {
			ControlConfig config = DeviceContext.controlConfigMap.get(deviceID + Constant.SPLIT_SIGN_COMMA + controlType);
			RtuControlByCodeRequest request = new RtuControlByCodeRequest(config, controlType, value);
			serialPoint.sendMsgByClientChannel(DeviceContext.deviceMap.get(deviceID).getChannelId(), request);

			log.info("控制电器 " + deviceID + " " + controlType + " 指令发送成功");
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
  
