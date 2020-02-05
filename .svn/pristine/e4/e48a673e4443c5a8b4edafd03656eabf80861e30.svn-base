package com.telek.hemsipc.service;

import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.context.HemsipcSpringContext;

/**
 * Service 工厂
 * @Class Name：ServiceFactory    
 * @Class Description：    
 * @Creater：kds    
 * @Create Time：2019年10月18日上午9:58:22    
 * @Modifier：kds    
 * @Modification Time：2019年10月18日上午9:58:22    
 * @Remarks：
 */
public class ServiceFactory {
    /**
     * Service工厂
     * @Class Name：ProtocolFactory    
     * @Class Description：    
     * @Creater：kds    
     * @Create Time：2019年10月18日上午9:55:41    
     * @Modifier：kds    
     * @Modification Time：2019年10月18日上午9:55:41    
     * @Remarks：
     */
 
	public static IControlService getIControlService(String type) {
		if (Constant.WRITE_CONFIG2_TYPE_BY_WRITE_REGISTER.equals(type)) {
			return HemsipcSpringContext.getBean("controlServiceByWriteRegister", IControlService.class);
		} else if (Constant.WRITE_CONFIG_TYPE_BY_CODE.equals(type)) {
			return HemsipcSpringContext.getBean("controlServiceByCode", IControlService.class);
		} else {
			return null;
		}
	}
 
    
}
